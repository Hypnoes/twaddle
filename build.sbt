lazy val root = (project in file("."))
    .settings(
        name := "twaddle",
        version := "dev-0.00",
        scalaVersion := "2.12.3",
        libraryDependencies ++= Seq(
            "org.apache.spark" % "spark-core-2.11" % "2.2.0",
            "org.apache.spark" % "spark-sql_2.11" % "2.2.0"
        )
    )
