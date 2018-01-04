import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Encoders

object Main {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("oil-field")
      .getOrCreate()
    import spark.implicits._

    val (input, output) = (args(0), args(1))
    val schema = Encoders.product[Table].schema
    val df = spark.read.option("header", true).schema(schema).csv(input).as[Table]
  
    val pr = 0.35
    val ans = df.map(i => (i.o, i.alpha, i.beta, i.gamma, i.decs(pr)))
                .toDF("o", "alpha", "beta", "gamma", "decs")

    ans.show()
    ans.write.option("header", true).csv(output)
        
    spark.stop()
  }
    
  case class Table(o: String,
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
    
    def decs(pr: Double): String = pr match {
      case _ if(pr >= alpha)                => "POS"
      case _ if(alpha > pr && pr >= beta)   => "BND"
      case _ if(beta > pr)                  => "NEG"
    }
  }
}
