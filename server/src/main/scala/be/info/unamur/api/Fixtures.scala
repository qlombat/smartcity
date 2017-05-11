package be.info.unamur.api

import java.sql.Timestamp

import be.info.unamur.persistence.entities.{Property, RfidSubscription, Sensor}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{FutureSupport, ScalatraServlet}
import scala.concurrent.ExecutionContext
import scala.util.Random

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
    Property.create("TemperatureMin", "3.0")
    Property.create("HumidityMax", "60.0")
    Property.create("LightMin", "190.0")
  }

  get("/generate/sensors") {

    for (i <- 0 to 200) {
      val temp = Random.nextInt(30)
      Sensor.create("Temperature", temp, temp, new Timestamp(System.currentTimeMillis() - Random.nextInt(2147483647)))
    }

    for (i <- 0 to 200) {
      val hum = Random.nextInt(90)
      Sensor.create("Humidity", hum, hum, new Timestamp(System.currentTimeMillis() - Random.nextInt(2147483647)))
    }

    for (i <- 0 to 200) {
      val light = Random.nextInt(900)
      Sensor.create("Light", light, light, new Timestamp(System.currentTimeMillis() - Random.nextInt(2147483647)))
    }

    for (i <- 0 to 200) {
      Sensor.create("AuxiliaryCarDetectorActorNorth", 1, 1, new Timestamp(System.currentTimeMillis() - Random.nextInt(2147483647)))
    }

    for (i <- 0 to 200) {
      Sensor.create("AuxiliaryCarDetectorActorSouth", 1, 1, new Timestamp(System.currentTimeMillis() - Random.nextInt(2147483647)))
    }

    for (i <- 0 to 200) {
      Sensor.create("MainRoadCarDetectorActorEast", 1, 1, new Timestamp(System.currentTimeMillis() - Random.nextInt(2147483647)))
    }

    for (i <- 0 to 200) {
      Sensor.create("MainRoadCarDetectorActorWest", 1, 1, new Timestamp(System.currentTimeMillis() - Random.nextInt(2147483647)))
    }
  }
}

object Fixtures {
}