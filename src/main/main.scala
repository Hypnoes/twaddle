import org.apache.spark.sql.SparkSession

object Main {
    def main(args: Array[String]) {
        val spark = SparkSession
            .builder
            .appName("Simple Application")
            .getOrCreate()

        val logFile = "path/to/logfile"
        val logData = spark.read.textFile(logFile).cache()

        val numAs = logData.filter(line => line.contains("a")).count()
        val numBs = logData.filter(line => line.contains("b")).count()

        println(s"Lines with a: $numAs, Lines with b: $numBs")
        
        spark.stop()
    }
}
