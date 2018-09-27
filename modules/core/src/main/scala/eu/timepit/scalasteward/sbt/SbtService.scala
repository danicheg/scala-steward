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

package eu.timepit.scalasteward.sbt

import cats.effect.IO
import eu.timepit.scalasteward.application.WorkspaceService
import eu.timepit.scalasteward.dependency.Dependency
import eu.timepit.scalasteward.github.data.Repo
import eu.timepit.scalasteward.{dependency, ioLegacy}
import eu.timepit.scalasteward.sbtLegacy.sbtCmd

trait SbtService[F[_]] {
  def getDependencies(repo: Repo): F[List[Dependency]]
}

class IoSbtService(workspaceService: WorkspaceService[IO]) extends SbtService[IO] {
  override def getDependencies(repo: Repo): IO[List[Dependency]] =
    workspaceService.repoDir(repo).flatMap { dir =>
      ioLegacy
        .firejail(
          sbtCmd :+ ";libraryDependenciesAsJson ;reload plugins; libraryDependenciesAsJson",
          dir
        )
        .map(lines => lines.flatMap(dependency.parser.parseDependencies))
    }
}
