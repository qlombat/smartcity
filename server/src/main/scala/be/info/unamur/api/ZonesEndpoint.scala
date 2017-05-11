package be.info.unamur.api

import java.sql.{Time, Timestamp}
import java.util.{Calendar, Locale}

import be.info.unamur.persistence.entities.{BusSchedule, Zone}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._
import scalikejdbc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}


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
        val day = Calendar.getInstance().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        val currentMillis = System.currentTimeMillis()
        val now = new Time(currentMillis)

        BusSchedule.findAllBy(sqls"day = $day and opening_time < $now and closing_time > $now") match {

          case Nil =>
            Zone.create("BUS", "BUS", opened = true, new Timestamp(currentMillis))
            "zones" -> Zone.findAllLast().filter(!_.opened).map(_.name)

          case _ =>
            if (Zone.findAllLast().exists(z => z.name == "BUS" && z.opened))
              Zone.create("BUS", "BUS", opened = false, new Timestamp(currentMillis))
            "zones" -> Zone.findAllLast().filter(!_.opened).map(_.name)

        }
      }
    }
  }

  post("/:name/close") {
    new AsyncResult() {
      override val is = Future {
        Zone.create(params(ZonesEndpoint.NameParamIdentifier), params(ZonesEndpoint.NameFullParamIdentifier), opened = false, new Timestamp(System.currentTimeMillis()))
      }
    }
  }

  post("/:name/open") {
    new AsyncResult() {
      override val is = Future {
        Zone.create(params(ZonesEndpoint.NameParamIdentifier), params(ZonesEndpoint.NameFullParamIdentifier), opened = true, new Timestamp(System.currentTimeMillis()))
      }
    }
  }

  get("/history") {
    new AsyncResult() {
      override val is = Future {
        val currentTime = System.currentTimeMillis()
        val currentTimeHour = new Timestamp(currentTime - ZonesEndpoint.HourInMillis)
        val currentTimeDay = new Timestamp(currentTime - ZonesEndpoint.DayInMillis)
        val currentTimeMonth = new Timestamp(currentTime - ZonesEndpoint.MonthInMillis)

        (params.get(ZonesEndpoint.TakeParamIdentifier), params.get("order"), params.get("time")) match {
          case (Some(take), Some(order), Some(time)) =>
            try {
              order match {
                case "asc" => time match {
                  case "hour" => Zone.findAllAscBy(sqls"created_at > $currentTimeHour").take(Integer.parseInt(take))
                  case "day" => Zone.findAllAscBy(sqls"created_at > $currentTimeDay").take(Integer.parseInt(take))
                  case "month" => Zone.findAllAscBy(sqls"created_at > $currentTimeMonth").take(Integer.parseInt(take))
                  case "all" => Zone.findAllAsc().take(Integer.parseInt(take))
                  case _ => "error" -> "Invalid time"
                }
                case "desc" => time match {
                  case "hour" => Zone.findAllDescBy(sqls"created_at > $currentTimeHour").take(Integer.parseInt(take))
                  case "day" => Zone.findAllDescBy(sqls"created_at > $currentTimeDay").take(Integer.parseInt(take))
                  case "month" => Zone.findAllDescBy(sqls"created_at > $currentTimeMonth").take(Integer.parseInt(take))
                  case "all" => Zone.findAllDesc().take(Integer.parseInt(take))
                  case _ => "error" -> "Invalid time"
                }
                case _ => "error" -> "Invalid order"
              }
            }
            catch {
              case _: NumberFormatException => "error" -> "The take parameter is not a int"
            }

          case (None, Some(order), Some(time)) =>
            order match {
              case "asc" => time match {
                case "hour" => Zone.findAllAscBy(sqls"created_at > $currentTimeHour")
                case "day" => Zone.findAllAscBy(sqls"created_at > $currentTimeDay")
                case "month" => Zone.findAllAscBy(sqls"created_at > $currentTimeMonth")
                case "all" => Zone.findAllAsc()
                case _ => "error" -> "Invalid time"
              }
              case "desc" => time match {
                case "hour" => Zone.findAllDescBy(sqls"created_at > $currentTimeHour")
                case "day" => Zone.findAllDescBy(sqls"created_at > $currentTimeDay")
                case "month" => Zone.findAllDescBy(sqls"created_at > $currentTimeMonth")
                case "all" => Zone.findAllDesc()
                case _ => "error" -> "Invalid time"
              }
              case _ => "error" -> "Invalid order"
            }

          case (Some(take), None, None) =>
            try {
              Zone.findAllDesc().take(Integer.parseInt(take))
            }
            catch {
              case _: NumberFormatException => "error" -> "The take parameter is not a int"
            }

          case _ =>
            Zone.findAll()
        }
      }
    }
  }
}

object ZonesEndpoint {
  val NameParamIdentifier     = "name"
  val NameFullParamIdentifier = "full_name"
  val TakeParamIdentifier     = "take"

  val HourInMillis  = 3600000
  val DayInMillis   = 86400000
  val MonthInMillis = 2678400000L
}
