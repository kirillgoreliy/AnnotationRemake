package controllers

import org.forome.core.struct.{Assembly, Chromosome, Interval}

import javax.inject._
import play.api._
import play.api.libs.json.{JsNumber, JsValue, Json, Writes}
import play.api.mvc._
import spark_config.{MongoSparkSession, MySqlSparkSession, PostCollectClass}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents,
                               val config: Configuration,
                               val textEx: Test,
                               val mongoSparkSession : MongoSparkSession,
                               val mySqlSparkSession: MySqlSparkSession) extends BaseController {
  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def main(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def getCustomList: Action[AnyContent] = Action { request =>
    Ok(Json.toJson(this.mySqlSparkSession.getDataResult.toString()))
  }

  def getAstorageResponce: Action[AnyContent] = Action { request =>{
    val queryAssembly = request.getQueryString("assembly")
    val assembly = queryAssembly.get match {
      case "GRCh37" => Assembly.GRCh37
      case "GRCh38" => Assembly.GRCh38
      case _ => throw new RuntimeException("Invalid assembly")
    }
    val intervalBody = request.body
    val interval = Interval.of(Chromosome.of(intervalBody.asJson.get("chromosome").toString()),
      intervalBody.asJson.get("start").asInstanceOf[JsNumber].value.intValue(),
      intervalBody.asJson.get("end").asInstanceOf[JsNumber].value.intValue())
    Ok(Json.toJson(new PostCollectClass(config, interval, assembly).getResponse))
  }

  }
}
