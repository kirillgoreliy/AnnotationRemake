package spark_config

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.apache.commons.io.IOUtils
import org.apache.http.{HttpHeaders, HttpResponse}
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.spark.{SparkConf, SparkContext}
import org.forome.core.struct.{Assembly, Interval}
import play.api.Configuration
import play.api.libs.json.{JsObject, JsString, JsValue, Json}
import spark_config.AstorageResponseScheme

class PostCollectClass(config: Configuration, val interval: Interval, val assembly: Assembly) {
  var variants: List[JsValue] = List()
  var sparkContext: SparkContext = SparkContext.getOrCreate(new SparkConf().setAppName("astorage_http_request").setMaster("local"))
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val reads = Json.reads[AstorageResponseScheme]

  var responseCodes: JsString = {
    val uri: String = config.get[String]("astorage_url")
    val client = new DefaultHttpClient()
    val post = new HttpPost(uri + "/collect")
    post.addHeader(HttpHeaders.CONTENT_TYPE, "application/json")
    val assemblyType = assembly match {
      case Assembly.GRCh37 => "hg19"
      case Assembly.GRCh38 => "hg38"
      case _ => throw new RuntimeException("Unknown assembly")
    }

    val json_str =
      s"""{
         |    "arrays":["SpliceAI"],
         |    "fasta": "$assemblyType",
         |    "variants": [
         |        {
         |            "chrom" : "${interval.chromosome.getChar.substring(1, interval.chromosome.getChar.length - 1)}",
         |            "pos":${interval.start},
         |            "last":${interval.end}
         |        }
         |    ]
         |}""".stripMargin

    post.setEntity(new StringEntity(json_str))
    val response = client.execute(post).getEntity.getContent
    val jsStr = JsString(IOUtils.toString(response))

    val obj = Json.parse(jsStr.value)
//    val jsEntity = new JsString(IOUtils.toString(response)).as[AstorageResponseScheme]

//    println(entity)
//    entity
    jsStr
  }

  def getResponse: JsString = responseCodes
}

case class PostRequest(arrays: List[String], fasta: String, variants: List[PostObjectVariants])

case class PostObjectVariants(chrom: String, pos: Integer, last: Integer)