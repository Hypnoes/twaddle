import org.apache..spark.sql.SparkSession
import org.apache.spark.ml.feature.StringIndexer
import orgapache.spark.ml.LogisticREgression
import org.apache.spark.ml.linalg.Vectors

object Main {
  def main(args: Array[String]): Unit = {
    val spark = sparkSession
      .builder
      .appName("Connect-4")
      .getOrCreate
    import spark.implicits._

    val (ifile, ofile) = (args(0), args(1))
    val df = spark.read.csv(file)

    val si = new StringIndexer()

    val sidf = df.columns.foldLeft(df)((x, i) => si
                          .setInputCol(i)
                          .setOutputCol(i + "x")
                          .fit(x)
                          .transform(x))
                          .select(df.columns.map(c => col(c + "x")): _*)

    val training = sidf.map(i => T1(Vecotrs.dense(i
                        .toSeq
                        .dropRight(1)
                        .map(_.toString.toDouble)
                        .toArray).toSparse, i.toSeq.last.toString.toDouble))

    val lr = new LogisticRegression()
                  .setMaxiter(100)
                  .setRegParam(0.0)
                  .setElasticNetParam(0.0)

    val model = lr.fit(training)
    val lroa = lr.transform(training)

    val alpha = 0.8
    val beta = 0.2

    spark.stop
  }

  case class T1(features: org.apache.spark.ml.linalg.Vector, label: Double)
  case class Twd(alpha: Double, beta: Double) {
    def decs(pr: Double): Double = pr match {
      case _ if (pr >= alpha)             => 0.0
      case _ if (alpha > pr && pr > beta) => 0.5
      case _ if (beta >= pr)              => 1.0
    }
  }
}
