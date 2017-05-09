package be.info.unamur.api

import java.sql.Timestamp

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
class Fixtures extends ScalatraServlet with JacksonJsonSupport with FutureSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  override protected implicit def executor: ExecutionContext = ExecutionContext.global


  before() {
    contentType = formats("json")
  }

  get("/generate/rfidsubscription") {
        RfidSubscription.create("Justin", "Sirjacques", "2800b86b50", new Timestamp(System.currentTimeMillis()))
        RfidSubscription.create("Jérémy", "Duchesne", "5c005df656", new Timestamp(System.currentTimeMillis()))
        RfidSubscription.create("Noé", "Picard", "5c005db1b2", new Timestamp(System.currentTimeMillis()))
        RfidSubscription.create("Quentin", "Lombat", "5c005cd4e2", new Timestamp(System.currentTimeMillis()))
  }
}

object Fixtures {
}