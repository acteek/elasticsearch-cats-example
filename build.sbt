name := "elasticsearch-cats-example"

scalaVersion := "2.13.4"
lazy val elasticVersion = "7.11.0"
lazy val catsVersion    = "2.3.1"
lazy val circeVersion   = "0.13.0"

libraryDependencies ++= Seq(
    "com.sksamuel.elastic4s" % "elastic4s-core_2.13"          % elasticVersion
  , "com.sksamuel.elastic4s" % "elastic4s-client-esjava_2.13" % elasticVersion
  , "com.sksamuel.elastic4s" % "elastic4s-json-circe_2.13"    % elasticVersion
  , "com.sksamuel.elastic4s" % "elastic4s-effect-cats_2.13"   % elasticVersion
  , "org.typelevel"          %% "cats-effect"                 % catsVersion
  , "org.typelevel"          %% "cats-core"                   % catsVersion
  , "io.circe"               %% "circe-core"                  % circeVersion
  , "io.circe"               %% "circe-generic"               % circeVersion
  , "io.circe"               %% "circe-parser"                % circeVersion
)
