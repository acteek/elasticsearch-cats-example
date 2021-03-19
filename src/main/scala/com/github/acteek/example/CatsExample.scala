package com.github.acteek.example

import cats.effect._
import cats.implicits._
import com.sksamuel.elastic4s.{
  ArrayFieldValue, NestedFieldValue, RequestFailure, RequestSuccess, Response, SimpleFieldValue
}
import com.sksamuel.elastic4s.fields._
import com.sksamuel.elastic4s.requests.common.RefreshPolicy
import com.sksamuel.elastic4s.requests.searches.{SearchRequest, SearchResponse}
import com.sksamuel.elastic4s.requests.indexes.{CreateIndexRequest, IndexRequest}
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.cats.effect.instances._
import io.circe.generic.auto._
import com.sksamuel.elastic4s.circe._
import Types._

// https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-types.html
object CatsExample extends IOApp {

  val catsIndex: CreateIndexRequest = createIndex("cats")
    .mapping(
        properties(
          KeywordField("id")
        , TextField("name", store = Some(true), fielddata = Some(true), fields = List(KeywordField("keyword")))
        , IntegerField("age")
        , TextField("food")
        , ObjectField(
            name = "owner"
          , properties = List(TextField("firstName"), TextField("secondName"))
        )
      )
    )
    .replicas(2)

  val searchIndex: SearchRequest = search("cats")
    .query(
        boolQuery().must(
          termsQuery("food", "fish")
        , rangeQuery("age").lte(5)
        , matchQuery("owner.firstName", "bob")
      )
    )
    .sortByFieldAsc("name")

  def insertCat(cat: Cat): IndexRequest =
    indexInto("cats")
//      .fieldValues(
//          SimpleFieldValue("id", cat.id)
//        , SimpleFieldValue("name", cat.name)
//        , SimpleFieldValue("age", cat.age)
//        , ArrayFieldValue("food", cat.food.map(SimpleFieldValue.apply(_)))
//        , SimpleFieldValue("owner", Map("firstName" -> cat.owner.firstName, "secondName" -> cat.owner.secondName))
//      )
            .source(cat)
      .id(cat.id)
      .refresh(RefreshPolicy.Immediate)

  def consoleLog[F[_]: Sync](msg: String): F[Unit] = Sync[F].delay(println(msg))
  def report[F[_]: Sync](res: Response[SearchResponse]): F[Unit] = res match {
    case RequestFailure(_, _, _, e) => consoleLog(s"We failed: $e")
    case RequestSuccess(_, _, _, r) =>
      for {
        _ <- Sync[F].delay(r.hits.hits.toList.foreach(println))
        _ <- consoleLog("-------------------------------")
        _ <- r.to[Cat].toList.map(r => consoleLog(r.toString)).sequence
        _ <- consoleLog(s"There were ${r.totalHits} total hits")
      } yield ()
  }

  def run(args: List[String]): IO[ExitCode] =
    Elastic
      .resource[IO]("http://localhost:9200")
      .use { elastic =>
        for {
//          _    <- elastic.execute(deleteIndex("cats"))
//
//          created <- elastic.execute(catsIndex)
//          _       <- consoleLog[IO](created.result.toString)
//          insert <- elastic.execute(bulk(insertCat(cat1), insertCat(cat2), insertCat(cat3)))
//          _      <- consoleLog[IO](insert.result.toString)
          resp   <- elastic.execute(searchIndex)
          _      <- report[IO](resp)
        } yield ()

      }
      .as(ExitCode.Success)

}
