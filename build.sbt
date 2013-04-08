scalaVersion := "2.10.0"

autoCompilerPlugins := true

addCompilerPlugin("org.scala-lang.plugins" % "continuations" % "2.10.0")

scalacOptions += "-P:continuations:enable"