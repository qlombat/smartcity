package be.info.unamur.api

import be.info.unamur.persistence.entities.{Property, Sensor}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{AsyncResult, FutureSupport, ScalatraServlet}

import scala.concurrent.{ExecutionContext, Future}


/** Api endpoint to retrieve the speed information
  *
  * @author jeremyduchesne
  */
class SpeedEndpoint extends ScalatraServlet with JacksonJsonSupport with FutureSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  override protected implicit def executor: ExecutionContext = ExecutionContext.global

  before() {
    contentType = formats("json")
  }

  get("/") {
    new AsyncResult() {
      override val is = Future {
        (Sensor.findLastByName("Temperature"), Sensor.findLastByName("Humidity"), Sensor.findLastByName("Light")) match {
          case (Some(temperature), Some(humidity), Some(light)) =>
            (Property.find("TemperatureMin"), Property.find("HumidityMax"), Property.find("LightMin")) match {
              case (Some(tempMin), Some(humidMax), Some(lightMin)) =>
                if (((temperature.value < tempMin.value.toDouble) && (humidity.value > humidMax.value.toDouble)) || (light.value < lightMin.value.toDouble))
                  "speed" -> 30
                else
                  "speed" -> 50
              case _ => halt(400, "error" -> "Properties not found")
            }
          case _ => halt(400, "error" -> "Sensors not found")
        }
      }
    }
  }
}