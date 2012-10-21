/*
 * Copyright 2012 杨博 (Yang Bo)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance at the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dongxiguo.pttrt

import sbt._

object PttrtPlugin extends Plugin {
  final case class TypedExpression(val typeName: String, val expression: String)

  final object TypedExpression {
    final def apply[A <: java.io.Serializable: Manifest](a: A) = {
      import java.io._
      val buffer = new ByteArrayOutputStream()
      new ObjectOutputStream(buffer).writeObject(a)
      new TypedExpression(
        manifest[A].toString,
        "new java.io.ObjectInputStream(" +
          "new java.io.ByteArrayInputStream(\"" +
          buffer.toByteArray.view.map {
            case '\\' => "\\\\"
            case '"' => "\\\""
            case b if b < 0 || b.toChar.isControl => {
              "\\u00" +
                Character.forDigit((b >>> 4) & 0xF, 16) +
                Character.forDigit(b & 0xF, 16)
            }
            case b => b.toChar.toString
          }.mkString("") +
          "\".getBytes(\"ISO-8859-1\"))).readObject().asInstanceOf[" +
          manifest[A].toString + "]")
    }

    final def apply(a: String) =
      new TypedExpression(
        typeName = "String",
        expression = a.iterator.map {
          case '\\' => "\\\\"
          case '"' => "\\\""
          case c if c.isControl || c >= 128 => {
            "\\u" +
              Character.forDigit(c >>> 12, 16) +
              Character.forDigit((c >>> 8) & 0xF, 16) +
              Character.forDigit((c >>> 4) & 0xF, 16) +
              Character.forDigit(c & 0xF, 16)
          }
          case c => c.toString
        }.mkString("\"", "", "\""))

    final def apply(a: Long) =
      new TypedExpression(typeName = "Long", expression = a.toString + 'L')

    final def apply(a: Int) =
      new TypedExpression(typeName = "Int", expression = a.toString)

    final def apply(a: Char) =
      new TypedExpression(typeName = "Char", expression = a.toString)

    final def apply(a: Short) =
      new TypedExpression(typeName = "Short", expression = a.toString)

    final def apply(a: Byte) =
      new TypedExpression(typeName = "Byte", expression = a.toString)

    final def apply(a: Boolean) =
      new TypedExpression(typeName = "Boolean", expression = a.toString)

    final def apply(a: Double) =
      new TypedExpression(typeName = "Double", expression = a.toString + "D")

    final def apply(a: Float) =
      new TypedExpression(typeName = "Float", expression = a.toString + "F")

  }

  object PttrtKeys {

    type FullyQualifiedObjectName = String

    type FieldName = String

    val pttrtData =
      TaskKey[Map[FullyQualifiedObjectName, Map[FieldName, TypedExpression]]](
        "pttrt-data",
        "Data that will be passed to runtime.")

  }

  private val PackageClass = """^(?:((?:\w+\.)*\w+)\.)?(\w+)$""".r

  final def pttrtSettings: Seq[Setting[_]] = {
    import sbt.Keys._
    import PttrtKeys._
    Seq(
      pttrtData := Map.empty,
      sourceGenerators in Compile <+=
        (pttrtData in Compile, sourceManaged in Compile) map { (pttrtData, outputPath) =>
          import java.io._
          (for (
            (PackageClass(packageName, objectName), fields) <- pttrtData
          ) yield {
            val packagePath = if (packageName == null) {
              outputPath
            } else {
              outputPath / packageName.replace('.', '/')
            }
            packagePath.mkdirs()
            val generatedFile = packagePath / (objectName + ".scala")
            val writer =
              new OutputStreamWriter(
                new BufferedOutputStream(
                  new FileOutputStream(generatedFile)))
            try {
              if (packageName != null) {
                writer append
                  "package " append packageName append "\n\n"
              }
              writer append
                "object " append objectName append " {\n\n"
              for ((fieldName, typedData) <- fields) {
                writer append
                  "  val " append fieldName append ": " append
                  typedData.typeName append
                  " = " append typedData.expression append "\n\n"
              }
              writer append "}\n"
            } finally {
              writer.close()
            }
            generatedFile
          })(collection.breakOut(Seq.canBuildFrom[java.io.File]))
        })
  }

}

// vim: set ts=2 sw=2 et:
