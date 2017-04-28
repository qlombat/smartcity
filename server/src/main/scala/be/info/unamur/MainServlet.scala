package be.info.unamur

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import org.slf4j.{Logger, LoggerFactory}

import scala.language.postfixOps


/** The main servlet.
  *
  * @author Noé Picard
  */
class MainServlet extends ScalatraServlet with ScalateSupport {
  val logger: Logger = LoggerFactory.getLogger(getClass)


  /* Displays the index page */
  get("/") {
    contentType = "text/html"
    logger.debug("Main page loaded")
    layoutTemplate("/WEB-INF/views/index.ssp",
      ("customScriptPath", "/resources/js/index.js"))
  }

}