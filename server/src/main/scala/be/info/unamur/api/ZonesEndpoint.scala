package be.info.unamur.api

import java.sql.Timestamp

import be.info.unamur.persistence.entities.Zone
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scalikejdbc._


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
        "zones" -> Zone.findAllLast().filter(!_.opened).map(_.name)
      }
    }
  }

  get("/:name/close") {
    new AsyncResult() {
      override val is = Future {
        Zone.create(params(ZonesEndpoint.NameParamIdentifier), opened = false, new Timestamp(System.currentTimeMillis()))
      }
    }
  }

  get("/:name/open") {
    new AsyncResult() {
      override val is = Future {
        Zone.create(params(ZonesEndpoint.NameParamIdentifier), opened = true, new Timestamp(System.currentTimeMillis()))
      }
    }
  }

  get("/history") {
    new AsyncResult() {
      override val is = Future {
        Zone.findAllDesc().take(5)
      }
    }
  }
}

object ZonesEndpoint {
  val NameParamIdentifier = "name"
}
