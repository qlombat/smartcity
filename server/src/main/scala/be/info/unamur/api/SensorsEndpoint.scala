package be.info.unamur.api

import java.sql.Timestamp

import be.info.unamur.persistence.entities.Sensor
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{AsyncResult, FutureSupport, ScalatraServlet}

import scala.concurrent.{ExecutionContext, Future}


/** Api endpoint to retrieve sensors information.
  *
  * @author Noé Picard
  */
class SensorsEndpoint extends ScalatraServlet with JacksonJsonSupport with FutureSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  override protected implicit def executor: ExecutionContext = ExecutionContext.global


  before() {
    contentType = formats("json")
  }

  post("/") {
    new AsyncResult() {
      override val is = Future {
        (params.get(SensorsEndpoint.NameParamIdentifier),
          params.getAs[Double](SensorsEndpoint.ValueParamIdentifier),
          params.getAs[Double](SensorsEndpoint.GrossValueParamIdentifier)) match {
          case (Some(name), Some(value), Some(grossValue)) => Sensor.create(name, value, grossValue, new Timestamp(System.currentTimeMillis()))
          case (_, _, _) => halt(400, "error" -> "Missing parameter(s)")
        }
      }
    }
  }

  get("/:name") {
    new AsyncResult() {
      override val is = Future {
        Sensor.findLastByName(params("name")) match {
          case Some(s) => s
          case None => halt(400, "error" -> "Sensor not found")
        }
      }
    }
  }
}

object SensorsEndpoint {
  val NameParamIdentifier       = "name"
  val ValueParamIdentifier      = "value"
  val GrossValueParamIdentifier = "gross_value"
}
