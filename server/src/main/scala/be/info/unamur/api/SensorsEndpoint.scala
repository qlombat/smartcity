package be.info.unamur.api

import java.sql.Timestamp

import be.info.unamur.persistence.entities.Sensor
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{AsyncResult, FutureSupport, ScalatraServlet}
import scala.concurrent.{ExecutionContext, Future}
import scalikejdbc._


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
        Sensor.findLastByName(params(SensorsEndpoint.NameParamIdentifier)) match {
          case Some(s) => s
          case None => halt(400, "error" -> "Sensor not found")
        }
      }
    }
  }

  get("/all/:name") {
    new AsyncResult() {
      override val is = Future {
        params get (SensorsEndpoint.TimeParamIdentifier) match {
          case Some(time) => getSensorsData(params(SensorsEndpoint.NameParamIdentifier), time) match {
            case Nil => halt(400, "error" -> "No data for this sensor or this time")
            case s => params get (SensorsEndpoint.CountParamIdentifier) match {
              case Some("true") => "size" -> s.length
              case _ => s
            }
          }
          case _ => halt(400, "error" -> "Bad time : please use : 'hour' or 'day' or 'month'")

        }
      }
    }
  }


  def getSensorsData(sensor : String, time: String):List[Sensor]={
    val currentTime = System.currentTimeMillis()
    val currentTimeHour = new Timestamp(currentTime - SensorsEndpoint.HourInMillis)
    val currentTimeDay = new Timestamp(currentTime - SensorsEndpoint.DayInMillis)
    val currentTimeMonth = new Timestamp(currentTime - SensorsEndpoint.MonthInMillis)
    time match {
      case "hour" => Sensor.findAllBy(sqls"name = ${sensor} and created_at > ${currentTimeHour}")
      case "day" => Sensor.findAllBy(sqls"name = ${sensor} and created_at > ${currentTimeDay}")
      case "month" => Sensor.findAllBy(sqls"name = ${sensor} and created_at > ${currentTimeMonth}")
      case "all" => Sensor.findAllBy(sqls"name = ${sensor}")

    }
  }
}

object SensorsEndpoint {
  val NameParamIdentifier = "name"
  val CountParamIdentifier = "count"
  val ValueParamIdentifier = "value"
  val TimeParamIdentifier = "time"
  val GrossValueParamIdentifier = "gross_value"
  val HourInMillis = 3600000
  val DayInMillis = 86400000
  val MonthInMillis = 2678400000L

}
