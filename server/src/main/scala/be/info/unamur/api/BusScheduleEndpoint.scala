package be.info.unamur.api

import java.sql.Time
import java.text.SimpleDateFormat

import be.info.unamur.persistence.entities.BusSchedule
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{AsyncResult, FutureSupport, ScalatraServlet}

import scala.concurrent.{ExecutionContext, Future}


/**
  * @author Noé Picard
  */
class BusScheduleEndpoint extends ScalatraServlet with JacksonJsonSupport with FutureSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  override protected implicit def executor: ExecutionContext = ExecutionContext.global

  before() {
    contentType = formats("json")
  }

  post("/:day") {
    new AsyncResult() {
      override val is = Future {
        (params.get("day"), params.get("opening_time"), params.get("closing_time")) match {
          case (Some(day), Some(openingTime), Some(closingTime)) =>
            val sdf = new SimpleDateFormat("HH:mm")
            BusSchedule.create(day, new Time(sdf.parse(openingTime).getTime), new Time(sdf.parse(closingTime).getTime));
          case _ => "error" -> "Missing parameter(s)"
        }
      }
    }
  }
}
