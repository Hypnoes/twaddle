import org.apache.spark.sql.Dataset

object C {
  val regs = { val x = (0.0 to 1.0) by 0.05 toList; x cross x toList}

  def c(alpha: Double, beta: Double, df: org.apache.spark.sql.Dataset[T3]) = {
    def cc (i: Double) = {
        val tp: Double = df.where($"decision" === i).where($"decision" === $"label").count.toDouble
        val fp: Double = df.where($"decision" === i).where($"decision" !== $"label").count.toDouble
        val fn: Double = df.where($"decision" !== i).where($"label" === i).count.toDouble
        val tn: Double = df.where($"decision" !== i).where($"decision" === $"label").count.toDouble
        val p = tp/(tp+fp)
        val r = tp/(tp+fn)
        val tpr = r
        val fpr = fp/(tn+fp)
        val f1 = 2*p*r/(p+r)
        (p, r, tpr, fpr, f1)
      }
    val c0 = cc(0.0)
    val c1 = cc(1.0)
    val pl = Array(c0._1, c1._1)
    val rl = Array(c0._2, c1._2)
    val tprl = Array(c0._3, c1._3)
    val fprl = Array(c0._4, c1._4)
    val f1 = Array(c0._5, c1._5)
    C_(alpha, beta, pl, rl, tprl, fprl, f1) 
  }

  val s_ = regs.map(r => c(r._1, r._2, t2s.map(i => T3(i.label, i.features, i.probability, i.prediction, i.decs(r._1, r._2), {
    i.decs(r._1, r._2) match {
      case 1.0 => i.prediction
      case 0.5 => 0.5
      case 0.0 => -1.0
      case _   => -2.0
    }
  })))).toDF.as[C_]

  case class T2(label: Double, features: Vector, probability: Double, prediction: Double) {
    val pr = probability
    def decs(alpha: Double, beta: Double): Double = pr match {
      case _ if (pr > alpha)                => 1.0
      case _ if (alpha >= pr && pr >= beta) => 0.5
      case _ if (beta > pr)                 => 0.0
    }
  }

  case class T3(label: Double, featrues: Vector, probability: Double, prediction: Double, region: Double, decision: Double)
  case class C_(alpha: Double, beta: Double, precision: Array[Double], recall: Array[Double],
                  truePositiveRate: Array[Double], falsePositiveRate: Array[Double], fMeasurement: Array[Double])

  implicit class Crossable[X](xs: Traversable[X]) {
    def cross[Y](ys: Traversable[Y]) = for { x <- xs; y <- ys } yield (x, y)
  }
}