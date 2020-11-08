val settingsHelper = ProjectSettingsHelper("au.id.tmm", "digest4s")(
  githubProjectName = "digest4s",
)

settingsHelper.settingsForBuild

lazy val root = project
  .in(file("."))
  .settings(settingsHelper.settingsForRootProject)
  .settings(console := (console in Compile in core).value)
  .aggregate(
    core,
  )

lazy val core = project
  .in(file("core"))
  .settings(settingsHelper.settingsForSubprojectCalled("core"))
  .settings(
    libraryDependencies += "commons-codec" % "commons-codec" % "1.15",
    libraryDependencies += "au.id.tmm.tmm-utils" %% "tmm-utils-testing-core"       % "0.6.2" % Test
  )

addCommandAlias("check", ";+test;scalafmtCheckAll")
