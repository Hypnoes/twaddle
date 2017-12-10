import org.apache.spark.sql.SparkSession

object Main {
    def main(args: Array[String]): Unit = {
        val spark = SparkSession
            .builder
            .appName("Simple Application")
            .getOrCreate()

        val pr = 0.35

        val (input, output) = (args(0), args(1))
        val raw = spark.read.option("header", true).csv(input)
        val df = raw.as[Table].map(i => (i, i.alpha, i.beta, i.gamma, i.decs(pr)))
        val xt = df.select("o", "alpha", "beta", "gamma", "decs")
        
        xt.show()
        xt.write.text(output)
        
        spark.stop()
    }
    
    case class Table(o: String,
                    theta_PP: Double, 
                    theta_BP: Double, 
                    theta_NP: Double,
                    theta_PN: Double,
                    theta_BN: Double,
                    theta_NN: Double,
                    phi_PP: Double,
                    phi_BP: Double,
                    phi_NP: Double,
                    phi_PN: Double,
                    phi_BN: Double,
                    phi_NN: Double) {
        def alpha = ((theta_BN - theta_PN) + (phi_PN - phi_BN)) / 
            ((theta_BN -theta_PN) + (phi_PN - phi_BN) + (theta_PP - theta_BP) + (phi_BP - phi_PP))

        def beta = ((theta_NN - theta_BN) + (phi_BN - phi_NN)) /
            ((theta_NN - theta_BN) + (phi_BN - phi_NN) + (theta_BN - theta_NP) + (phi_NP - phi_BN))

        def gamma = ((theta_NN - theta_PN) + (phi_PN - phi_NN)) /
            ((theta_NN - theta_PN) + (phi_PN - phi_NN) + (theta_PP - theta_NP) + (phi_NP - phi_PP))

        def decs(pr: Double): String = pr match {
            case pr >= this.alpha              => "POS"
            case this.alpha >= pr >= this.beta => "BND"
            case this.beta >= pr               => "NEG"
        } 
    }
}
