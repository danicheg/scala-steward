/*
 * Copyright 2018 scala-steward contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
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

package eu.timepit.scalasteward.io

import better.files.File
import cats.effect.IO
import cats.implicits._

object IoFileService extends FileService[IO] {
  override def readFile(file: File): IO[String] =
    IO(file.contentAsString)

  override def writeFile(file: File, content: String): IO[Unit] =
    IO(file.writeText(content)).void
}
