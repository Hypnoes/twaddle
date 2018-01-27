import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.featrue.StringIndexer
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.linalg.Vectors

object Main {
  def main(args: Array[String]): Unit = {
    val spark = new SparkSession()
                .builder
                .appName("${this.getSimpleClassName}")
                .getOrCreate
    import spark.impilicits._
    
    val root = "hdfs://192.168.50.51:9000/user/hadoop/mushroom/expanded.1"
    val raw = spark.read.csv(root)

    val cols = raw.columns
    val si = new StringIndexer()
    
    val mix = cols.foldLeft(raw)((x, i) => { si.setInputCol(x).setOutputCol(i + "x").fit(x).transform(x) })
    val mic = mix.select("_c0x", ..., "_c22x").as[Tba].map(i => (i.label, i.features)).toDF("label", "feature")
    
    val lr = new LogisticRegression().setMaxIter(4000).setRegParam(0.0).setElasticNetParam(0.0)
    val model = lr.fit(data)
    val o = model.transform(data)
    
    o.write.json(root + ".out")

    spark.stop()
  }

  case class Tba(_c0x: Double, _c1x: Double, _c2x: Double, _c3x: Double,
    _c4x: Double, _c5x: Double, _c6x: Double, _c7x: Double,
    _c8x: Double, _c9x: Double, _c10x: Double, _c11x: Double,
    _c12x: Double, _c13x: Double, _c14x: Double, _c15x: Double,
    _c16x: Double, _c17x: Double, _c18x: Double, _c19x: Double,
    _c20x: Double, _c21x: Double, _c22x: Double) {
      def label = _c0x
      def features = Vectors.dense(Array[Double](_c1x: Double, _c2x: Double, _c3x: Double,
    _c4x: Double, _c5x: Double, _c6x: Double, _c7x: Double,
    _c8x: Double, _c9x: Double, _c10x: Double, _c11x: Double,
    _c12x: Double, _c13x: Double, _c14x: Double, _c15x: Double,
    _c16x: Double, _c17x: Double, _c18x: Double, _c19x: Double,
    _c20x: Double, _c21x: Double, _c22x: Double))
  }
}
