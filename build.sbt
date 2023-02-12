enablePlugins(ScalaJSPlugin)
scalaJSUseMainModuleInitializer := true

ThisBuild / scalaVersion := "3.2.2"
lazy val root = (project in file("."))
  .settings(
    name := "quizzical-quest"
  )

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "2.1.0"
