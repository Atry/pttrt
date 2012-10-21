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

organization := "com.dongxiguo"

organizationHomepage := None

name := "pttrt"

crossScalaVersions :=
  Seq("2.8.0", "2.8.1", "2.8.2",
      "2.9.0", "2.9.0-1", "2.9.1", "2.9.1-1", "2.9.2",
      "2.10.0-M1", "2.10.0-M2", "2.10.0-M4", "2.10.0-M5", "2.10.0-M6", "2.10.0-M7",
      "2.10.0-RC1")

version := "0.1.0"

sbtPlugin := true

scalacOptions += "-unchecked"

scalacOptions += "-deprecation"

publishTo <<= (isSnapshot) { isSnapshot: Boolean =>
  if (isSnapshot)
    Some("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots") 
  else
    Some("releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2")
}

licenses := Seq(
  "Apache License, Version 2.0" ->
  url("http://www.apache.org/licenses/LICENSE-2.0.html"))

homepage := Some(url("https://code.google.com/p/pttrt/"))