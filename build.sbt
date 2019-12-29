import com.lightbend.sbt.SbtAspectj.aspectjUseInstrumentedClasses

val akkaVersion         = "2.5.26"
val currentScalaVersion = "2.13.1"
val scalaTestVersion    = "3.1.0"

val Organization = "org.mdedetrich"
val RootName     = "sbt-aspectj-issue"
val Version      = "1.0.0-SNAPSHOT"

resolvers in ThisBuild += Resolver.mavenLocal

scalaVersion in ThisBuild := currentScalaVersion
crossScalaVersions in ThisBuild := Seq("2.12.10", currentScalaVersion)
organization in ThisBuild := Organization

lazy val root = (project in file("."))
  .enablePlugins(SbtAspectj)
  .settings(
    name := RootName,
    version := Version,
    // add akka-actor as an aspectj input (find it in the update report)
//    aspectjInputs in Aspectj ++= update.value.matching(
//      moduleFilter(organization = "com.typesafe.akka", name = "akka-actor*")),
    // replace the original akka-actor jar with the instrumented classes in runtime
//    fullClasspath in Runtime := aspectjUseInstrumentedClasses(Runtime).value,
    // only compile the aspects (no weaving)
    aspectjCompileOnly in Aspectj := true,
    // ignore warnings (we don't have the target classes at this point)
    aspectjLintProperties in Aspectj += "invalidAbsoluteTypeName = ignore",
    // replace regular products with compiled aspects
    products in Compile ++= (products in Aspectj).value,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion
    )
  )

lazy val test = (project in file("test"))
  .enablePlugins(SbtAspectj)
  .settings(
    aspectjBinaries in Aspectj ++= update.value.matching(
      moduleFilter(organization = Organization, name = s"$RootName*")),
    aspectjInputs in Aspectj ++= update.value.matching(
      moduleFilter(organization = "com.typesafe.akka", name = "akka-actor*")),
    fullClasspath in Runtime := aspectjUseInstrumentedClasses(Runtime).value,
    // weave this project's classes
    aspectjInputs in Aspectj += (aspectjCompiledClasses in Aspectj).value,
    products in Compile := (products in Aspectj).value,
    products in Runtime := (products in Compile).value,
    libraryDependencies ++= Seq(
      Organization %% RootName % Version
    )
  )
