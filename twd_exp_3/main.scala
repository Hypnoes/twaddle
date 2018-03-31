import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.linalg.{Vector, Vectors}

object Main {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
                .builder
                .appName(f"${this.getSimpleClassName}")
                .getOrCreate
    import spark.implicits._

    val (ifile, ofile) = (args(0), args(1))
    val raw = spark.read.csv(ifile)

    val si = new StringIndexer()

    val sied = raw.columns.foldLeft(raw)((x, i) => si
                  .setInputCol(i)
                  .setOutputCol(i + "x")
                  .fit(x)
                  .transform(x))
    val data = sied.select(sied.colRegex("`_c[0-9]{1,2}x`"))
                    .map(i => (i.getDouble(0), Vectors.dense(i.toSeq.drop(1).map(_.toString.toDouble).toArray)))
                    .toDF("label", "features")

    val lr = new LogisticRegression()
                .setMaxIter(100)
                .setRegParam(0.0)
                .setElasticNetParam(0.0)
                .setStandardization(false)

    val model = lr.fit(data)
    val lroa = model.transform(data)

    //val a1 = lroa.map(i => (i.getDouble(0), i.getAs[Vector](3).apply(0)))
    //              .collect
    //              .zipWithIndex
    //              .map(i => (i._2, i._1.getDouble(0), i._1.getDouble(1)))
    //              .toList
    //              .toDF("i", "label", "probability")

    //val lam = spark.read.option("header", true).csv(la).as[Lambda].map(x => (x.i, x.alpha, x.beta))
    //val mix = a1.join(lam, "i")
    //val twda = mix.map(x => (x(0), x(1), x(2), x(3), x(4), decs(x.getDouble(2), x.getDouble(3), x.getDouble(4)))

    val alpha = 0.935
    val beta = 0.085

    val twda = lroa.map(i => T1(i.getDouble(0), i.getAs[Vector](3).apply(0)))
                    .map(i => (i.param_1, i.param_2, decs(i.param_2, alpha, beta)))
                    .toDF("label", "probability", "region")

    twda.write.csv(ofile)

    spark.stop()
  }

  case class Lambda(i: String,
                  theta_PP: Double, theta_BP: Double, theta_NP: Double,
                  theta_PN: Double,theta_BN: Double,theta_NN: Double,
                  phi_PP: Double, phi_BP: Double, phi_NP: Double,
                  phi_PN: Double, phi_BN: Double, phi_NN: Double) {
    def alpha = ((theta_BN - theta_PN) + (phi_PN - phi_BN)) /
      ((theta_BN - theta_PN) + (phi_PN - phi_BN) + (theta_PP - theta_BP) + (phi_BP - phi_PP))
    def beta = ((theta_NN - theta_BN) + (phi_BN - phi_NN)) /
      ((theta_NN - theta_BN) + (phi_BN - phi_NN) + (theta_BN - theta_NP) + (phi_NP - phi_BN))
    def gamma = ((theta_NN - theta_PN) + (phi_PN - phi_NN)) /
      ((theta_NN - theta_PN) + (phi_PN - phi_NN) + (theta_PP - theta_NP) + (phi_NP - phi_PP))
  }

  case class T1(param_1: Double, param_2: Double)

  def decs(pr: Double, alpha: Double, beta: Double): Double = pr match {
    case _ if(pr >= alpha)                => 1.0
    case _ if(alpha > pr && pr >= beta)   => 0.5
    case _ if(beta > pr)                  => 0.0
  }
}
