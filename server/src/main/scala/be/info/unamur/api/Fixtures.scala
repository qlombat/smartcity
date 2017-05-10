package be.info.unamur.api

import java.sql.Timestamp

import be.info.unamur.persistence.entities.{Property, RfidSubscription}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{FutureSupport, ScalatraServlet}

import scala.concurrent.ExecutionContext

/** Api endpoint to generate default data.
  *
  * @author Justin Sirjacques
  * @author jeremyduchesne
  */
class Fixtures extends ScalatraServlet with JacksonJsonSupport with FutureSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  override protected implicit def executor: ExecutionContext = ExecutionContext.global

  //Generate
  get("/Subscription") {
    RfidSubscription.create("Justin", "Sirjacques", "2800b86b50", new Timestamp(System.currentTimeMillis()))
    RfidSubscription.create("Jérémy", "Duchesne", "5c005df656", new Timestamp(System.currentTimeMillis()))
    RfidSubscription.create("Noé", "Picard", "5c005db1b2", new Timestamp(System.currentTimeMillis()))
    RfidSubscription.create("Quentin", "Lombat", "5c005cd4e2", new Timestamp(System.currentTimeMillis()))
    "success"
  }

  get("/generate/properties") {
    Property.create("TemperatureMin", "0.0")
    Property.create("HumidityMax", "50.0")
    Property.create("LightMin", "190.0")
  }
}

object Fixtures {
}