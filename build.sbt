name := """play-web-simple"""

version := "1.0.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

// Dependencies
libraryDependencies ++= Seq(
  "com.ejisan" %% "play-pagemeta" % "1.2.1",
  "com.ejisan" %% "play-form" % "2.0.2",
  "org.typelevel" %% "cats" % "0.8.1",
  "com.typesafe.slick" %% "slick" % "3.1.1",
  "com.typesafe.play" %% "play-slick" % "2.0.2",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.2",
  "org.postgresql" % "postgresql" % "9.4.1212",
  "com.github.tminglei" %% "slick-pg" % "0.14.6",
  "com.github.tminglei" %% "slick-pg_date2" % "0.14.6"
)

routesImport ++= Seq(
  "java.util.UUID",
  "utils.extension.PathBinders._")

// Scala compiler options
scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-feature",
  "-optimise",
  "-explaintypes",
  "-encoding",
  "UTF-8",
  "-Xlint"
)
