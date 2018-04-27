def c(alpha:Double, df: org.apache.spark.sql.Dataset[T1]) = {
  def x (i: Double) = {
      val tp: Double = df.where($"decision" === i).where($"decision" === $"label").count.toDouble
      val fp: Double = df.where($"decision" === i).where($"decision" !== $"label").count.toDouble
      val fn: Double = df.where($"decision" !== i).where($"label" === i).count.toDouble
      val tn: Double = df.where($"decision" !== i).where($"decision" === $"label").count.toDouble
      val p = tp/(tp+fp)
      val r = tp/(tp+fn)
      val tpr = r
      val fpr = fp/(tn+fp)
      val f1 = 2*p*r/(p+r)
      (p, r, fpr, f1)
    }
  val c0 = x(0.0)
  val c1 = x(1.0)
  val c2 = x(2.0)
  val pl = Array(c0._1, c1._1, c2._1)
  val rl = Array(c0._2, c1._2, c2._2)
  val fprl = Array(c0._3, c1._3, c2._3)
  val f1l = Array(c0._4, c1._4, c2._4)
  C_(alpha, pl, rl, fprl, f1l) 
}

val s_ = alpha_list.map(a => c(a, df1.map(i => T1(i.label, i.probability, i.prediction, i.decs(a, 0.0), {
  i.decs(a, 0.0) match {
    case 1.0 => i.prediction
    case 0.5 => 0.5
    case 0.0 => -1.0
    case _   => -2.0
  }
})))).toDF.as[C_]