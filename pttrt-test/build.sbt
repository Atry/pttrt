// Copyright 2012 杨博 (Yang Bo)
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance at the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

pttrtSettings

name := "pttrt-test"

PttrtKeys.pttrtData +=
  "Foo" -> Map[String, TypedExpression](
    "Bar" -> TypedExpression(100, "Hello", "World", true),
    "Baz" -> TypedExpression(42))

PttrtKeys.pttrtData <+= name map { name =>
  "com.dongxiguo.pttrt.test.BuildingInfo" -> Map[String, TypedExpression](
    "Date" -> TypedExpression(new java.util.Date),
    "ProjectName" -> TypedExpression(name),
    "GitBranch" -> TypedExpression("git branch"!!))
}

// vim: et sts=2 sw=2
