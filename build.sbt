import au.id.tmm.sbt.DependencySettings

ThisBuild / sonatypeProfile := "au.id.tmm"
ThisBuild / baseProjectName := "digest4s"
ThisBuild / githubProjectName := "digest4s"
ThisBuild / githubWorkflowJavaVersions := List("adopt@1.11")

lazy val root = project
  .in(file("."))
  .settings(settingsForRootProject)
  .settings(console := (console in Compile in core).value)
  .aggregate(
    core,
  )

lazy val core = project
  .in(file("core"))
  .settings(settingsForSubprojectCalled("core"))
  .settings(
    libraryDependencies += "commons-codec"        % "commons-codec"          % "1.15",
    libraryDependencies += "au.id.tmm.tmm-utils" %% "tmm-utils-testing-core" % "0.6.2" % Test,
  )
