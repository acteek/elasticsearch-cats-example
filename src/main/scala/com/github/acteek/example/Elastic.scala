package com.github.acteek.example

import cats.Monad
import cats.effect.{Resource, Sync}
import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties, Executor, Functor, Handler, Response}
import com.sksamuel.elastic4s.http.JavaClient

trait Elastic[F[_]] {
  def execute[T, U](req: T)(implicit h: Handler[T, U], m: Manifest[U]): F[Response[U]]
  def show[T](t: T)(implicit h: Handler[T, _]): F[String]
  def close: F[Unit]
}

object Elastic {

  def resource[F[_]: Sync: Executor: Functor: Monad](host: String): Resource[F, Elastic[F]] =
    Resource.make {
      Sync[F].delay {
        val properties = ElasticProperties(host)
        val client     = ElasticClient(JavaClient(properties))

        new Elastic[F] {
          def execute[T, U](req: T)(implicit h: Handler[T, U], m: Manifest[U]): F[Response[U]] = client.execute(req)
          def show[T](req: T)(implicit h: Handler[T, _]): F[String]                            = Sync[F].delay(client.show(req))
          def close: F[Unit]                                                                   = Sync[F].delay(client.close())

        }

      }

    }(_.close)
}
