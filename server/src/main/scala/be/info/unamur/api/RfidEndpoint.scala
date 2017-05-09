package be.info.unamur.api

import be.info.unamur.persistence.entities.{RfidSubscription, RfidTag}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{AsyncResult, FutureSupport, ScalatraServlet}

import scala.concurrent.{ExecutionContext, Future}
import scalikejdbc._
/** Api endpoint to retrieve Rfid information.
  *
  * @author Justin Sirjacques
  */
class RfidEndpoint extends ScalatraServlet with JacksonJsonSupport with FutureSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  override protected implicit def executor: ExecutionContext = ExecutionContext.global


  before() {
    contentType = formats("json")
  }

  get("/parking/accessibility") {
    new AsyncResult() {
      override val is = Future {
        var taken = 0
        for (e <- RfidSubscription.findAll()) {
           (RfidTag.findAllBy(sqls"tag = ${e.tag} ORDER BY created_at DESC LIMIT 1"))match{
             case Nil =>
             case l => if (l.head.entry == 1) taken += 1
          }
        }
        ("taken" -> taken, "totalplaces" -> RfidEndpoint.totalPlaces)
      }
    }
  }
}

object RfidEndpoint {
  var totalPlaces = 4
}