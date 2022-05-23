package spark_config

import org.apache.spark.sql._
import play.api.Configuration

import javax.inject.{Inject, Singleton}

// For implicit conversions like converting RDDs to DataFrames
@Singleton
class MySqlSparkSession @Inject()(config: Configuration) {
  val user = "spark"
  val password = "spark"

  var rddList: Seq[Dataset[String]] = List()

  val spark: SparkSession = SparkSession
    .builder()
    .appName("Spark SQL basic example")
    .config("spark.master", "local")
    .config("spark.sql.hive.metastore.sharedPrefixes", "com.mysql.jdbc")
    .config("spark.shuffle.useOldFetchProtocol", "true")
    .getOrCreate()

    MySqlDatabaseList.values.foreach( value => {
      val tables = config.get[Seq[String]](value.toString)
      tables.foreach(table => {
        val customRdd: DataFrame = spark.read.format("jdbc")
          .option("url", s"jdbc:mysql://localhost:3306/$value?autoReconnect=true&useSSL=false")
          .option("dbtable", table)
          .option("user", user)
          .option("password", password)
          .option("driver", "com.mysql.cj.jdbc.Driver")
          .load()
        rddList = rddList :+ customRdd.toJSON
      })
    })


  def getDataResult: Seq[Dataset[String]] = {
    rddList
  }

}
