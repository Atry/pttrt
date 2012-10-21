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

import com.dongxiguo.pttrt.test.BuildingInfo

object Main {
  def main(args: Array[String]) {

    println("[" + BuildingInfo.ProjectName + "] Foo.Bar: " + Foo.Bar)
    println("[" + BuildingInfo.ProjectName + "] Foo.Baz: " + Foo.Baz)
    println(
      "[" + BuildingInfo.ProjectName + "] BuildingInfo.GitBranch: " +
      BuildingInfo.GitBranch.trim)
    println(
      "[" + BuildingInfo.ProjectName + "] This project is built at " +
      BuildingInfo.Date)

  }
}

// vim: set ts=2 sw=2 et:
