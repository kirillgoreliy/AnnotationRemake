package spark_config
import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.ReadConfig
import org.apache.spark.sql._

import java.util.Properties
import javax.inject.{Inject, Singleton}
// For implicit conversions like converting RDDs to DataFrames
@Singleton
class MongoSparkSession @Inject()() {
  val spark: SparkSession = SparkSession
    .builder()
    .appName("Spark SQL basic example")
    .config("spark.master", "local")
    .config("spark.mongodb.input.uri", "mongodb://127.0.0.1/main_note.user")
    .config("spark.mongodb.output.uri", "mongodb://127.0.0.1/main_note.user")
    .config("spark.mongodb.input.collection", "user")
    .getOrCreate()
  val dbProperties = new Properties()
  dbProperties.setProperty("driver", "mongodb.jdbc.MongoDriver")
  val readConfig: ReadConfig = ReadConfig(Map(
    "uri" -> "mongodb://localhost:27017/main_note.user",
    "readPreference.name" -> "secondaryPreferred"))
  val customRdd: DataFrame = MongoSpark.load(spark, readConfig)

  def getDataResult: String = {
    customRdd.first.json
  }

}
