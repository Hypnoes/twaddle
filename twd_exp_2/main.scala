import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Encoders

import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.linalg.Vectors

object Main {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("oil-field")
      .getOrCreate()
    import spark.implicits._

    val (input, dec, output) = (args(0), args(1), args(2))
    val schema = Encoders.product[Origin].schema
    val df = spark.read.option("header", true).schema(schema).csv(input).as[Origin]

    val labelFeature = df.map(i => (i.label, i.features)).toDF("label", "features")
    val (training, test) = { val t = labelFeature.randomSplit(Array(0.8, 0.2)); (t(0), t(1)) }

    val lr = new LogisticRegression()
      .setMaxIter(2000)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    val model = lr.fit(training)
    val pr = model.transform(training).select("label", "probability")

    val det = spark.read.option("header", true).csv(dec).as[Casualty]

    val ans = det.map(i => (i.o, i.alpha, i.beta, i.gamma, i.decs(pr)))
                .toDF("o", "alpha", "beta", "gamma", "decs")

    ans.show()
    ans.write.option("header", true).csv(output)
        
    spark.stop()
  }
  
  case class Origin(u: String, 
                    a1: Double, a2: Double, a3: Double, a4: Double, 
                    a5: Double, a6: Double, a7: Double, a8: Double, 
                    a9: Double, a10: Double, a11: Double, a12: Double, 
                    d: Double) {
    def label = d
    def features = Vectors.dense(Array[Double](a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12))
  }

  case class Casualty(label: String,
                  lambda_PP: Double, lambda_BP: Double, lambda_NP: Double,
                  lambda_PN: Double,lambda_BN: Double,lambda_NN: Double) {
    def alpha = (lambda_PN - lambda_BN) / 
        ((lambda_PN - lambda_BN) + (lambda_BP - lambda_PP))
    def beta  = (lambda_BN - lambda_NN) /
        ((lambda_BN - lambda_NN) + (lambda_NP - lambda_BP))
    def gamma = (lambda_PN - lambda_NN) /
        ((lambda_PN - lambda_NN) + (lambda_NP - lambda_PP))
    
    def decs(pr: Double): String = pr match {
      case _ if(pr >= alpha)                => "POS"
      case _ if(alpha > pr && pr >= beta)   => "BND"
      case _ if(beta > pr)                  => "NEG"
    }
  }
}
