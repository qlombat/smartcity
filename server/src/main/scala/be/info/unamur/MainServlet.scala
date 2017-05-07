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
    layoutTemplate("/WEB-INF/views/index.ssp", ("styles", Nil), ("scripts", Nil))
  }

  get("/api/doc") {
    contentType = "text/html"
    layoutTemplate("/WEB-INF/views/api-doc.ssp",
      ("styles", List("/resources/css/swagger-ui.css",
        "/resources/css/swagger.css")),
      ("scripts", List("/resources/js/swagger-ui.js",
        "/resources/js/swagger-ui-bundle.js",
        "/resources/js/swagger-ui-standalone-preset.js",
        "/resources/js/swagger.js")))
  }

  get("/api/doc.json") {
    contentType = "application/json"
    servletContext.getResourceAsStream("/WEB-INF/api-doc.json")
  }

  get("/chart/test") {
    contentType = "text/html"
    layoutTemplate("/WEB-INF/views/chart-test.ssp", ("styles", Nil), ("scripts",List("/resources/js/chart.bundle.min.js",
    "/resources/js/chart.js") ))
  }
}