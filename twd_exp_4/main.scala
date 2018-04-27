import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.linalg.{ Vector, Vectors }

import org.apache.spark.sql.functions._

object Main {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("Connect-4")
      .getOrCreate

    import spark.implicits._

    //val (ifile, ofile) = (args(0), args(1))
    val df = spark.read.csv("hdfs://master:9000/user/root/data/connect-4.data/connect-4.data")

    val si = new StringIndexer()

    val sidf = df.columns.foldLeft(df)((x, i) => si
                          .setInputCol(i)
                          .setOutputCol(i + "x")
                          .fit(x)
                          .transform(x))
                          .select(df.columns.map(c => col(c + "x")): _*)

    val training = sidf.map(i => T1(Vectors.dense(i
                        .toSeq
                        .dropRight(1)
                        .map(_.toString.toDouble)
                        .toArray).toSparse, i.toSeq.last.toString.toDouble))

    val lr = new LogisticRegression()
                  .setMaxIter(100)
                  .setRegParam(0.0)
                  .setElasticNetParam(0.0)

    val alpha = 0.6
    val beta = 0.0

    val model = lr.fit(training)
    val lroa = model.transform(training)

    //lroa.write.json("hdfs://master:9000/user/root/connect-4/lroa.json")
    
    val df1 = lroa.select("label", "features", "probability", "prediction")
                  .map(i => T2(
                          i.getDouble(0),
                          i.getAs[Vector](1),
                          i.getAs[Vector](2).apply(0),
                          i.getDouble(3)))
                  .as[T2]

    val twda = df1.map(i => T3(i.label, i.features, i.probability, i.prediction, 
      i.decs(alpha, beta), {
        i.decs(alpha, beta) match {
          case 1.0 => i.prediction
          case 0.5 => 0.5
          case 0.0 => -1.0
          case _   => -2.0
        }
      })).as[T3]

    //twda.write.json("hdfs://master:9000/user/root/connect-4/twda.json")
    twda.show

    // val training2 = twda.where($"region" === 0.5).select("label", "features")

    // val lr2 = new LogisticRegression()
    //               .setMaxIter(500)
    //               .setRegParam(0.0)
    //               .setElasticNetParam(0.0)

    // val model2 = lr2.fit(training2)
    // val lroa2 = model2.transform(training2)

    // lroa2.write.json("hdfs://master:9000/user/root/connect-4/lroa2.json")
    // val df2 = lroa2.select("label", "features", "probability", "prediction")
    //               .map(i => T2(
    //                       i.getDouble(0),
    //                       i.getAs[Vector](1),
    //                       i.getAs[Vector](2).apply(0),
    //                       i.getDouble(3)))
    //               .as[T2]

    // val twda2 = df2.map(i => T3(i.label, i.features, i.probability, i.prediction, 
    //   i.decs(alpha, beta), {
    //     i.decs(alpha, beta) match {
    //       case 1.0 => i.prediction
    //       case 0.5 => 0.5
    //       case 0.0 => -1.0
    //       case _   => -2.0
    //     }
    //   })).as[T3]

    // twda2.write.json("hdfs://master:9000/user/root/connect-4/twda2.json")
    // twda2.show

    def ec(df: org.apache.spark.sql.Dataset[Lro]): Double = {
      df.where($"label" === $"prediction").count / df.count.toDouble
    }
    
    val ee = ec(lroa.as[Lro])
    println(ee)
    //val ee2 = ec(lroa2.as[Lro])

    //println(list(ee, ee2))
    
    spark.stop
  }
  
  case class Lro(label: Double, features: Vector, rawPrediction: Vector, 
                  probability: Vector, prediction: Double)

  case class T1(features: org.apache.spark.ml.linalg.Vector, label: Double)
  case class T2(label: Double, features: Vector, probability: Double, prediction: Double) {
    val pr = probability
    def decs(alpha: Double, beta: Double): Double = pr match {
      case _ if (pr >= alpha)             => 1.0
      case _ if (alpha > pr && pr > beta) => 0.5
      case _ if (beta >= pr)              => 0.0
    }
  }
  case class T3(label: Double, features: Vector, probability: Double, 
                prediction: Double, region: Double, decision: Double)
}
