package be.info.unamur.api

import java.sql.Timestamp

import be.info.unamur.persistence.entities.Sensor
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{AsyncResult, FutureSupport, ScalatraServlet}

import scala.concurrent.{ExecutionContext, Future}
import scalikejdbc._
import java.util.{Calendar, Date}

import org.json4s.{DefaultFormats, Formats}


/** Api endpoint to retrieve sensors information.
  *
  * @author Noé Picard
  * @author Justin Sirjacques
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
        params get SensorsEndpoint.TimeParamIdentifier match {
          case Some(time) => getSensorsList(params(SensorsEndpoint.NameParamIdentifier), time) match {
            case Nil => halt(400, "error" -> "No data for this sensor or this time")
            case s => "all" -> s
          }
          case _ => halt(400, "error" -> "Bad time : please use : 'hour' or 'day' or 'month'")

        }
      }
    }
  }

  //TODO add this shit in the API doc
  get("/all/evolution/:name") {

    new AsyncResult() {
      override val is = Future {
        params get SensorsEndpoint.NameParamIdentifier match {
          case Some(n) => params get SensorsEndpoint.PeriodParamIdentifier match {
            case Some(p) => params get SensorsEndpoint.TimeParamIdentifier match {
              case Some(t) => t match {
                case "hour" => "result" -> (getPeriodsArray(p.toInt, SensorsEndpoint.HourInMillis, t), getValuesArray(n, p.toInt, SensorsEndpoint.HourInMillis))
                case "day" => "result" -> (getPeriodsArray(p.toInt, SensorsEndpoint.DayInMillis, t), getValuesArray(n, p.toInt, SensorsEndpoint.DayInMillis))
                case "month" => "result" -> (getPeriodsArray(p.toInt, SensorsEndpoint.MonthInMillis, t), getValuesArray(n, p.toInt, SensorsEndpoint.MonthInMillis))
              }
              case _ => halt(400, "error" -> "Time parameter missing")
            }
            case _ => halt(400, "error" -> "Period parameter missing")
          }
          case _ => halt(400, "error" -> "Sensor name missing")
        }
      }
    }
  }


  def getSensorsList(sensor: String, time: String): List[Sensor] = {
    val currentTime = System.currentTimeMillis()
    val currentTimeHour = new Timestamp(currentTime - SensorsEndpoint.HourInMillis)
    val currentTimeDay = new Timestamp(currentTime - SensorsEndpoint.DayInMillis)
    val currentTimeMonth = new Timestamp(currentTime - SensorsEndpoint.MonthInMillis)

    val constraintSensorsValue = sensor match {
      case s if s == "mainCarDetectorActorWest" || s == "mainCarDetectorActorWest" || s == "auxiliaryCarDetectorActorSouth" || s == "auxiliaryCarDetectorActorNorth" =>
        " and value != 0"
      case _ => ""
    }

    time match {
      case "hour" => Sensor.findAllBy(sqls"name = $sensor and created_at > $currentTimeHour $constraintSensorsValue")
      case "day" => Sensor.findAllBy(sqls"name = $sensor and created_at > $currentTimeDay $constraintSensorsValue")
      case "month" => Sensor.findAllBy(sqls"name = $sensor and created_at > $currentTimeMonth $constraintSensorsValue")
      case "all" => Sensor.findAllBy(sqls"name = $sensor $constraintSensorsValue")
    }
  }


  def getValuesArray(sensor: String, period: Integer, interval: Long): List[List[Sensor]] = {
    var upperTime = System.currentTimeMillis()
    var valuesList: Array[List[Sensor]] = new Array[List[Sensor]](period)
    for (p <- 0 until period) {
      valuesList(p) = Sensor.findAllBy(sqls"name = $sensor and created_at > ${new Timestamp(upperTime - interval)} and created_at < ${new Timestamp(upperTime)}").toList
      upperTime = upperTime - interval

    }
    valuesList.reverse.toList
  }

  def getPeriodsArray(period: Integer, interval: Long, time: String): Array[String] = {
    var upperTime = System.currentTimeMillis()
    var periodsList: Array[String] = new Array[String](period)
    for (p <- 0 until period) {
      time match {
        case "hour" => periodsList(p) = convertIntToHour(new Timestamp(upperTime))
        case "day" => periodsList(p) = convertIntToDay(new Timestamp(upperTime))
        case "month" => periodsList(p) = convertIntToMonth(new Timestamp(upperTime))
      }
      upperTime = upperTime - interval
    }
    periodsList.reverse
  }

  def convertIntToMonth(time: Timestamp): String = {
    val cal = Calendar.getInstance
    cal.setTime(new Date(time.getTime))
    val month: Int = cal.get(Calendar.MONTH)
    val year: Int = cal.get(Calendar.YEAR)
    month match {
      case 0 => "January ".concat(year.toString)
      case 1 => "February ".concat(year.toString)
      case 2 => "March ".concat(year.toString)
      case 3 => "April ".concat(year.toString)
      case 4 => "May ".concat(year.toString)
      case 5 => "June ".concat(year.toString)
      case 6 => "July ".concat(year.toString)
      case 7 => "August ".concat(year.toString)
      case 8 => "September ".concat(year.toString)
      case 9 => "October ".concat(year.toString)
      case 10 => "November ".concat(year.toString)
      case 11 => "December ".concat(year.toString)
    }
  }

  def convertIntToDay(time: Timestamp): String = {

    val cal = Calendar.getInstance
    cal.setTime(new Date(time.getTime))
    val day: Int = cal.get(Calendar.DAY_OF_MONTH)
    val month: Int = cal.get(Calendar.MONTH)
    val year: Int = cal.get(Calendar.YEAR)
    month match {
      case 0 => day.toString.concat(" January ").concat(year.toString)
      case 1 => day.toString.concat(" February ").concat(year.toString)
      case 2 => day.toString.concat(" March ").concat(year.toString)
      case 3 => day.toString.concat(" April ").concat(year.toString)
      case 4 => day.toString.concat(" May ").concat(year.toString)
      case 5 => day.toString.concat(" June ").concat(year.toString)
      case 6 => day.toString.concat(" July ").concat(year.toString)
      case 7 => day.toString.concat(" August ").concat(year.toString)
      case 8 => day.toString.concat(" September ").concat(year.toString)
      case 9 => day.toString.concat(" October ").concat(year.toString)
      case 10 => day.toString.concat(" November ").concat(year.toString)
      case 11 => day.toString.concat(" December ").concat(year.toString)
    }
  }

  def convertIntToHour(time: Timestamp): String = {

    val cal = Calendar.getInstance
    cal.setTime(new Date(time.getTime))
    val hour: Int = cal.get(Calendar.HOUR_OF_DAY)
    hour.toString.concat("h")
  }

}

object SensorsEndpoint {
  val NameParamIdentifier = "name"
  val ValueParamIdentifier = "value"
  val TimeParamIdentifier = "time"
  val PeriodParamIdentifier = "periods"
  val GrossValueParamIdentifier = "gross_value"
  val HourInMillis = 3600000
  val DayInMillis = 86400000
  val MonthInMillis = 2678400000L

}