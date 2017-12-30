import Dependencies._

val commonSetting = List(
  organization := "org.hypnoes",
  scalaVersion := "2.11.8",
  version      := "0.1.0-SNAPSHOT"
)

lazy val root = (project in file(".")).
  settings(
    inThisBuild(commonSetting),
    name := "twaddle",
    libraryDependencies += scalaTest % Test,
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "2.2.0",
      "org.apache.spark" %% "spark-sql" % "2.2.0"
    )
  )

lazy val subject_malware = (project in file("subject_malware")).
  settings(
    inThisBuild(commonSetting),
    name := "subject_malware",
    libraryDependencies += scalaTest % Test,
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "2.2.0",
      "org.apache.spark" %% "spark-sql" % "2.2.0",
      "org.apache.spark" %% "spark-mllib" % "2.2.0"
    )
  )

lazy val subject_mashroom = (project in file("subject_mashroom")).
  settings(
    inThisBuild(commonSetting),
    name := "subject_mashroom",
    libraryDependencies += scalaTest % Test,
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "2.2.0",
      "org.apache.spark" %% "spark-sql" % "2.2.0",
      "org.apache.spark" %% "spark-mllib" % "2.2.0"
    )
  )

lazy val subject_HIV = (project in file("subject_HIV")).
  settings(
    inThisBuild(commonSetting),
    name := "subject_HIV",
    libraryDependencies += scalaTest % Test,
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "2.2.0",
      "org.apache.spark" %% "spark-sql" % "2.2.0",
      "org.apache.spark" %% "spark-mllib" % "2.2.0"
    )
  )

lazy val subject_retuers = (project in file("subject_retuers")).
  settings(
    inThisBuild(commonSetting),
    name := "subject_retuers",
    libraryDependencies += scalaTest % Test,
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "2.2.0",
      "org.apache.spark" %% "spark-sql" % "2.2.0",
      "org.apache.spark" %% "spark-mllib" % "2.2.0"
    )
  )
