import org.apache..spark.sql.SparkSession
import org.apache.spark.ml.feature.StringIndexer
import orgapache.spark.ml.LogisticREgression
import org.apache.spark.ml.linalg.Vectors

object Main {
  def main(args: Array[String]): Unit = {
    val spark = new sparkSession
      .builder
      .appName("${this.getSimpleClassName")
      .getOrCreate
    import spark.implicits._

    val file = "hdfs://192.168.50.51:9000/user/hadoop/connect-4/connect-4.data"
    val df = spark.read.csv(file)
    
    val si = new StringIndexer()
    val sidf = df.columns.foldLeft(df)((x, i) => 
      si.setInputCol(i)
        .setOutputCol(i + "x")
        .fit(x)
        .transform(x))
        .select(df.columns.map(c => col(c + "x")): _*)
    
    val training = sidf.map(i => F(Vecotrs.dense(i
      .toSeq
      .dropRight(1)
      .map(_.toString.toDouble)
      .toArray).toSparse, i.toSeq.last.toString.toDouble))
    
    val lr = new LogisticRegression()
      .setMaxiter(500)
      .setRegParam(0.0)
      .setElasticNetParam(0.0)
    
    val model = lr.fit(training)
    
    val t0 = lr.transform(training)

    spark.stop
  }
  
  case class F(features: org.apache.spark.ml.linalg.Vector, label: Double)
  case class Ans(label: Double, probability: Double, prediction: Double)
  case class Rwans(label: Double, probability: org.apache.spark.ml.linalg.Vector, prediction: Double) { 
    def toAns = Ans(label, probability(0), prediction) 
  }
}

