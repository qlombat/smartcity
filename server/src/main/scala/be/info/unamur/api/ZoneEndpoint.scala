package be.info.unamur.api

import be.info.unamur.persistence.entities.Zone
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._
import scalikejdbc._


/**
  * @author Noé Picard
  */
class ZoneEndpoint extends ScalatraServlet with JacksonJsonSupport {
  // Sets up automatic case class to JSON output serialization
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  get("/") {
    "zones" ->
      Zone.findAllDistinct().filter(_.opened).map(_.name)
  }
}
