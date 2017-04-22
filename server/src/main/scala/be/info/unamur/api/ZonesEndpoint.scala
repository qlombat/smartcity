package be.info.unamur.api

import be.info.unamur.persistence.entities.Zone
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}


/** Api endpoint to retrieve zones information.
  *
  * @author Noé Picard
  */
class ZonesEndpoint extends ScalatraServlet with JacksonJsonSupport with FutureSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  override protected implicit def executor: ExecutionContext = ExecutionContext.global


  before() {
    contentType = formats("json")
  }

  get("/closed") {
    new AsyncResult() {
      override val is = Future {
        "zones" ->
          Zone.findAllDistinct().filter(!_.opened).map(_.name)
      }
    }
  }
}
