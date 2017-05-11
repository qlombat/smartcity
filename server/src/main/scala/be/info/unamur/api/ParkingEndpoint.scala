package be.info.unamur.api

import be.info.unamur.persistence.entities
import be.info.unamur.persistence.entities.{RfidSubscription, RfidTag}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{AsyncResult, FutureSupport, ScalatraServlet}

import scala.concurrent.{ExecutionContext, Future}
import scalikejdbc._

/** Api endpoint to retrieve parking information.
  *
  * @author Justin Sirjacques
  */
class ParkingEndpoint extends ScalatraServlet with JacksonJsonSupport with FutureSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  override protected implicit def executor: ExecutionContext = ExecutionContext.global


  before() {
    contentType = formats("json")
  }

  get("/accessibility") {
    new AsyncResult() {
      override val is = Future {
        var taken = 0
        for (e <- RfidSubscription.findAll()) {
          RfidTag.findAllBy(sqls"tag = ${e.tag} ORDER BY created_at DESC LIMIT 1") match {
            case Nil =>
            case l => if (l.head.entry == 1) taken += 1
          }
        }
        ("taken" -> taken, "totalplaces" -> ParkingEndpoint.totalPlaces)
      }
    }
  }
  get("/history") {
    new AsyncResult() {
      override val is = Future {
        params get ParkingEndpoint.TakeParamIdentifier match {
          case Some(t) => RfidTag.findAllBy(sqls"true ORDER BY created_at DESC LIMIT ${t.toInt}") match {
            case Nil => halt(400, "error" -> "No data")
            case t => t.map(x => (x,RfidSubscription.findAllBy(sqls"tag=${x.tag}").head))
          }
        }
      }
    }
  }
}

object ParkingEndpoint {
  var totalPlaces = 3
  var TakeParamIdentifier = "take"
}