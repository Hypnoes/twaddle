lazy val root = (project in file("."))
    .settings(
        name := "twaddle",
        version := "dev-0.00",
        scalaVersion in ThisBuild := "2.11.3",
        libraryDependencies ++= Seq(
            "org.apache.spark" %% "spark-core" % "2.2.0",
            "org.apache.spark" %% "spark-sql" % "2.2.0"
        )
    )
