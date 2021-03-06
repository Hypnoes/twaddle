val commonSetting = List(
  organization := "org.hypnoes",
  scalaVersion := "2.11.8",
  version      := "0.1.0-SNAPSHOT"
)

lazy val root = (project in file(".")).
  settings(
    inThisBuild(commonSetting),
    name := "twd_exp_2",
    description := "bank corruption",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "2.3.0",
      "org.apache.spark" %% "spark-sql" % "2.3.0",
      "org.apache.spark" %% "spark-mllib" % "2.3.0"
    )
  )
