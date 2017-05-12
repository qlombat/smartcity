package be.info.unamur.api

import java.sql.Time
import java.text.SimpleDateFormat

import be.info.unamur.persistence.entities.BusSchedule
import org.json4s.JsonAST.JString
import org.json4s.jackson.Serialization
import org.json4s.{CustomSerializer, Formats, NoTypeHints}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{AsyncResult, FutureSupport, ScalatraServlet}
import scalikejdbc._

import scala.concurrent.{ExecutionContext, Future}


class TimeSerializer extends CustomSerializer[Time](format => ( {
  case JString(s) =>
    val sdf = new SimpleDateFormat("HH:mm")
    new Time(sdf.parse(s).getTime)
}, {
  case x: Time =>
    val sdf = new SimpleDateFormat("HH:mm")
    JString(x.toString)
}
))

/**
  * @author Noé Picard
  */
class BusScheduleEndpoint extends ScalatraServlet with JacksonJsonSupport with FutureSupport {
  protected implicit lazy val jsonFormats: Formats = Serialization.formats(NoTypeHints) + new TimeSerializer

  override protected implicit def executor: ExecutionContext = ExecutionContext.global

  before() {
    contentType = formats("json")
  }

  get("/") {
    new AsyncResult() {
      override val is = Future {
        BusSchedule.findAllBy(sqls"true")
      }
    }
  }

  get("/:day") {
    new AsyncResult() {
      override val is = Future {
        params.get("day") match {
          case (Some(day)) =>
            BusSchedule.findAllBy(sqls"day = $day")
          case _ =>
            "error" -> "Missing parameter(s)"
        }
      }
    }
  }

  post("/:day") {
    new AsyncResult() {
      override val is = Future {
        (params.get("day"), params.get("opening_time"), params.get("closing_time")) match {
          case (Some(day), Some(openingTime), Some(closingTime)) =>
            val sdf = new SimpleDateFormat("HH:mm")
            BusSchedule.create(day, new Time(sdf.parse(openingTime).getTime), new Time(sdf.parse(closingTime).getTime))
          case _ => halt(400, "error" -> "Missing parameter(s)")
        }
      }
    }
  }

  delete("/:id") {
    new AsyncResult() {
      override val is = Future {
        params.get("id") match {
          case (Some(id)) =>
            val sdf = new SimpleDateFormat("HH:mm")
            BusSchedule.find(id) match {
              case Some(bs) =>
                BusSchedule.destroy(bs)
                "ok" -> "Bus schedule deleted"
              case None => halt(400, "error" -> "Unable to find the specified bus schedule")
            }
          case _ => halt(400, "error" -> "Missing parameter(s)")
        }
      }
    }
  }
}
