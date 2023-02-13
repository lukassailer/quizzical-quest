import org.scalajs.linker.interface.ModuleSplitStyle

lazy val quizzical = (project in file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "quizzical-quest",
    scalaVersion := "3.2.2",
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withModuleSplitStyle(ModuleSplitStyle.SmallModulesFor(List("quizzical")))
    }
  )

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "2.1.0",
  "com.github.japgolly.scalajs-react" %%% "core" % "2.1.1"
)
