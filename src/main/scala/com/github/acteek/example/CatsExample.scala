package com.github.acteek.example

import cats.effect._
import cats.implicits._
import com.sksamuel.elastic4s.{RequestFailure, RequestSuccess, Response}
import com.sksamuel.elastic4s.fields._
import com.sksamuel.elastic4s.requests.common.RefreshPolicy
import com.sksamuel.elastic4s.requests.searches.{SearchRequest, SearchResponse}
import com.sksamuel.elastic4s.requests.indexes.{CreateIndexRequest, IndexRequest}
import com.sksamuel.elastic4s.circe._
import io.circe.generic.auto._

object CatsExample extends IOApp {
  import com.sksamuel.elastic4s.ElasticDsl._
  import com.sksamuel.elastic4s.cats.effect.instances._
  import Types._

  val catsIndex: CreateIndexRequest = createIndex("cats").mapping(
      properties(
        TextField("id")
      , TextField("name")
      , IntegerField("age")
      , ObjectField("food")
      , ObjectField(
          name = "owner"
        , properties = Seq(TextField("firstName"), TextField("secondName"))
      )
    )
  )

  val searchIndex: SearchRequest = search("cats").query(
      boolQuery().must(
        termsQuery("food", "fish")
      , termsQuery("owner.firstName", "bob")
    )
  )

  def insertCat(cat: Cat): IndexRequest            = indexInto("cats").source(cat).withId(cat.id).refresh(RefreshPolicy.Immediate)
  def consoleLog[F[_]: Sync](msg: String): F[Unit] = Sync[F].delay(println(msg))

  def report[F[_]: Sync](res: Response[SearchResponse]): F[Unit] = res match {
    case RequestFailure(_, _, _, e) => consoleLog(s"We failed: $e")
    case RequestSuccess(_, _, _, r) =>
      for {
        _ <- r.to[Cat].toList.map(r => consoleLog(r.toString)).sequence
        _ <- consoleLog(s"There were ${r.totalHits} total hits")
      } yield ()
  }

  def run(args: List[String]): IO[ExitCode] =
    Elastic
      .resource[IO]("http://localhost:9200")
      .use { elastic =>
        for {
          _    <- elastic.execute(catsIndex)
          _    <- elastic.execute(bulk(insertCat(cat1), insertCat(cat2), insertCat(cat3)))
          resp <- elastic.execute(searchIndex)
          _    <- report[IO](resp)
        } yield ()

      }
      .as(ExitCode.Success)

}
