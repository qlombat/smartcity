package be.info.unamur.api

import java.sql.Timestamp

import be.info.unamur.persistence.entities._
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
  get("/database/clear") {
    RfidSubscription.destroyAll()
    RfidTag.destroyAll()
    Sensor.destroyAll()
    Zone.destroyAll()
    BusSchedule.destroyAll()
    Property.destroyAll()
  }

  //Generate
  get("/Subscription") {
    RfidSubscription.create("Justin", "Sirjacques", "2800b86b50", new Timestamp(System.currentTimeMillis()))
    RfidSubscription.create("Jérémy", "Duchesne", "5c005df656", new Timestamp(System.currentTimeMillis()))
    RfidSubscription.create("Noé", "Picard", "5c005db1b2", new Timestamp(System.currentTimeMillis()))
    RfidSubscription.create("Quentin", "Lombat", "5c005cd4e2", new Timestamp(System.currentTimeMillis()))
    "success"
  }

  get("/generate/properties") {
    try {
      Property.create("TemperatureMin", "3.0")
      Property.create("HumidityMax", "60.0")
      Property.create("LightMin", "70.0")
    } catch {
      case e:Throwable => "Propriétés déjà générées"
    }
  }

  get("/generate/sensors") {

    var offset = Timestamp.valueOf("2016-05-01 00:00:00").getTime
    val end = Timestamp.valueOf("2017-05-11 00:00:00").getTime
    var diff = end - offset + 1
    val rand = new Timestamp(offset + (Math.random * diff).toLong)

    for (i <- 0 to 200) {
      val temp = Random.nextInt(30)
      Sensor.create("Temperature", temp, temp, new Timestamp(offset + (Math.random * diff).toLong))
    }

    for (i <- 0 to 200) {
      val hum = Random.nextInt(90)
      Sensor.create("Humidity", hum, hum, new Timestamp(offset + (Math.random * diff).toLong))
    }

    for (i <- 0 to 200) {
      val light = Random.nextInt(900)
      Sensor.create("Light", light, light, new Timestamp(offset + (Math.random * diff).toLong))
    }

    for (i <- 0 to 200) {
      Sensor.create("AuxiliaryCarDetectorActorNorth", 1, 1, new Timestamp(offset + (Math.random * diff).toLong))
    }

    for (i <- 0 to 200) {
      Sensor.create("AuxiliaryCarDetectorActorSouth", 1, 1, new Timestamp(offset + (Math.random * diff).toLong))
    }

    for (i <- 0 to 200) {
      Sensor.create("mainCarDetectorActorEast", 1, 1, new Timestamp(offset + (Math.random * diff).toLong))
    }

    for (i <- 0 to 200) {
      Sensor.create("mainCarDetectorActorWest", 1, 1, new Timestamp(offset + (Math.random * diff).toLong))
    }

    offset = Timestamp.valueOf("2017-02-01 00:00:00").getTime
    diff = end - offset + 1

    for (i <- 0 to 200) {
      val temp = Random.nextInt(30)
      Sensor.create("Temperature", temp, temp, new Timestamp(offset + (Math.random * diff).toLong))
    }

    for (i <- 0 to 200) {
      val hum = Random.nextInt(90)
      Sensor.create("Humidity", hum, hum, new Timestamp(offset + (Math.random * diff).toLong))
    }

    for (i <- 0 to 200) {
      val light = Random.nextInt(900)
      Sensor.create("Light", light, light, new Timestamp(offset + (Math.random * diff).toLong))
    }

    for (i <- 0 to 200) {
      Sensor.create("AuxiliaryCarDetectorActorNorth", 1, 1, new Timestamp(offset + (Math.random * diff).toLong))
    }

    for (i <- 0 to 200) {
      Sensor.create("AuxiliaryCarDetectorActorSouth", 1, 1, new Timestamp(offset + (Math.random * diff).toLong))
    }

    for (i <- 0 to 200) {
      Sensor.create("mainCarDetectorActorEast", 1, 1, new Timestamp(offset + (Math.random * diff).toLong))
    }

    for (i <- 0 to 200) {
      Sensor.create("mainCarDetectorActorWest", 1, 1, new Timestamp(offset + (Math.random * diff).toLong))
    }

    for (i <- 0 to 20) {
      Zone.create("N", "North", false, new Timestamp(offset + (Math.random * diff).toLong))
      Zone.create("N", "North", true, new Timestamp(offset + (Math.random * diff).toLong))
      Zone.create("S", "South", false, new Timestamp(offset + (Math.random * diff).toLong))
      Zone.create("S", "South", true, new Timestamp(offset + (Math.random * diff).toLong))
      Zone.create("W", "West", false, new Timestamp(offset + (Math.random * diff).toLong))
      Zone.create("W", "West", true, new Timestamp(offset + (Math.random * diff).toLong))
      Zone.create("E", "East", false, new Timestamp(offset + (Math.random * diff).toLong))
      Zone.create("E", "East", true, new Timestamp(offset + (Math.random * diff).toLong))

      Zone.create("NW", "Northwest", false, new Timestamp(offset + (Math.random * diff).toLong))
      Zone.create("NW", "Northwest", true, new Timestamp(offset + (Math.random * diff).toLong))
      Zone.create("NE", "Northeast", false, new Timestamp(offset + (Math.random * diff).toLong))
      Zone.create("NE", "Northeast", true, new Timestamp(offset + (Math.random * diff).toLong))
      Zone.create("SW", "Southwest", false, new Timestamp(offset + (Math.random * diff).toLong))
      Zone.create("SW", "Southwest", true, new Timestamp(offset + (Math.random * diff).toLong))
      Zone.create("SE", "Southeast", false, new Timestamp(offset + (Math.random * diff).toLong))
      Zone.create("SE", "Southeast", true, new Timestamp(offset + (Math.random * diff).toLong))
    }

    Zone.create("N", "North", true, new Timestamp(System.currentTimeMillis()))
    Zone.create("S", "South", true, new Timestamp(System.currentTimeMillis()))
    Zone.create("W", "West", true, new Timestamp(System.currentTimeMillis()))
    Zone.create("E", "East", true, new Timestamp(System.currentTimeMillis()))
    Zone.create("NE", "Northeast", true, new Timestamp(System.currentTimeMillis()))
    Zone.create("NW", "Northwest", true, new Timestamp(System.currentTimeMillis()))
    Zone.create("SW", "Southwest", true, new Timestamp(System.currentTimeMillis()))
    Zone.create("SE", "Southeast", true, new Timestamp(System.currentTimeMillis()))
    Sensor.create("AuxiliaryCarDetectorActorNorth", 0, 0, new Timestamp(System.currentTimeMillis()))
    Sensor.create("AuxiliaryCarDetectorActorSouth", 0, 0, new Timestamp(System.currentTimeMillis()))
    Sensor.create("mainCarDetectorActorEast", 0, 0, new Timestamp(System.currentTimeMillis()))
    Sensor.create("mainCarDetectorActorWest", 0, 0, new Timestamp(System.currentTimeMillis()))
    "Success"
  }
}

object Fixtures {
}