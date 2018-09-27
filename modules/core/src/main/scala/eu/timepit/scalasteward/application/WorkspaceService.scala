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

package eu.timepit.scalasteward.application

import cats.implicits._
import better.files.File
import cats.effect.IO
import eu.timepit.scalasteward.github.data.Repo

trait WorkspaceService[F[_]] {
  def root: F[File]

  def repoDir(repo: Repo): F[File]
}

class IoWorkspaceService(workspace: File) extends WorkspaceService[IO] {
  override def root: IO[File] = IO(workspace)
  override def repoDir(repo: Repo): IO[File] =
    root.flatMap { r =>
      val dir = r / "repos" / repo.owner / repo.repo
      if (dir.exists)
        IO.pure(dir)
      else
        IO(dir.createDirectories()) >> IO.pure(dir)
    }
}
