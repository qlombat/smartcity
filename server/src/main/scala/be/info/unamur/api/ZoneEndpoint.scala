package be.info.unamur.api

import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._


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
    //TODO : Implement this
  }
}
