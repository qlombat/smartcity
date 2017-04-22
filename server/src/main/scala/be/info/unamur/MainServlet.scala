package be.info.unamur

import grizzled.slf4j.Logger
import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport

import scala.language.postfixOps

/** The main servlet.
  *
  * @author Noé Picard
  */
class MainServlet extends ScalatraServlet with ScalateSupport {
  val logger: Logger = Logger[MainServlet]


  /* Displays the index page */
  get("/") {
    contentType = "text/html"
    logger.debug("Main page loaded")
    layoutTemplate("/WEB-INF/views/index.ssp")
  }

}