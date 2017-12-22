import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Encoders

object Main {
    def main(args: Array[String]): Unit = {
        val spark = SparkSession
            .builder
            .appName("Simple Application")
            .getOrCreate()
        import spark.implicits._

        val (input, output) = (args(0), args(1))
        val schema = Encoders.product[Table].schema
        val df = spark.read.option("header", true).schema(schema).csv(input).as[Table]
        
        val pr = 0.35
        val ans = df.map(i => (i.o, alpha(i), beta(i), gamma(i), decs(pr, i)))
                    .toDF("o", "alpha", "beta", "gamma", "decs")

        ans.show()
        ans.write.option("header", true).csv(output)
        
        spark.stop()
    }
    
    case class Table(o: String,
                    theta_PP: Double, theta_BP: Double, theta_NP: Double,
                    theta_PN: Double,theta_BN: Double,theta_NN: Double,
                    phi_PP: Double, phi_BP: Double, phi_NP: Double, 
                    phi_PN: Double, phi_BN: Double, phi_NN: Double)

    def alpha(tb: Table): Double = ((tb.theta_BN - tb.theta_PN) + (tb.phi_PN - tb.phi_BN)) / 
        ((tb.theta_BN - tb.theta_PN) + (tb.phi_PN - tb.phi_BN) + (tb.theta_PP - tb.theta_BP) + (tb.phi_BP - tb.phi_PP))
    def beta(tb: Table): Double = ((tb.theta_NN - tb.theta_BN) + (tb.phi_BN - tb.phi_NN)) /
        ((tb.theta_NN - tb.theta_BN) + (tb.phi_BN - tb.phi_NN) + (tb.theta_BN - tb.theta_NP) + (tb.phi_NP - tb.phi_BN))
    def gamma(tb: Table): Double = ((tb.theta_NN - tb.theta_PN) + (tb.phi_PN - tb.phi_NN)) /
        ((tb.theta_NN - tb.theta_PN) + (tb.phi_PN - tb.phi_NN) + (tb.theta_PP - tb.theta_NP) + (tb.phi_NP - tb.phi_PP))
    def decs(pr: Double, tb: Table): String = pr match {
        case _ if(pr >= alpha(tb))                    => "POS"
        case _ if(alpha(tb) > pr && pr >= beta(tb))   => "BND"
        case _ if(beta(tb) > pr)                      => "NEG"
    }
}
