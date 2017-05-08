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


  before() {
    contentType = "text/html"
  }

  /* Displays the index page */
  get("/") {
    logger.debug("Main page loaded")
    layoutTemplate("/WEB-INF/views/dashboard.ssp",
      ("styles", List("/resources/css/dashboard.css")),
      ("scripts", List("/resources/vendor/momentjs/moment.min.js",
        "/resources/js/dashboard.js")))
  }

  get("/api/doc") {
    layoutTemplate("/WEB-INF/views/api-doc.ssp",
      ("styles", List("/resources/vendor/swagger-ui/swagger-ui.css",
        "/resources/css/api-doc.css")),
      ("scripts", List("/resources/vendor/swagger-ui/swagger-ui-bundle.js",
        "/resources/vendor/swagger-ui/swagger-ui-standalone-preset.js",
        "/resources/js/api-doc.js")))
  }

  get("/api/doc.json") {
    contentType = "application/json"

    halt(status = 200,
      headers = Map("Access-Control-Allow-Origin" -> "*",
        "Access-Control-Allow-Methods" -> "GET, POST, DELETE, PUT",
        "Access-Control-Allow-Headers" -> "Content-Type, api_key, Authorization",
        "Connection" -> "close"),
      body = servletContext.getResourceAsStream("/WEB-INF/api-doc.json"))
  }

  get("/chart/test") {
    layoutTemplate("/WEB-INF/views/chart-test.ssp",
      ("styles", Nil),
      ("scripts", List("/resources/vendor/chartjs/chart.bundle.min.js",
        "/resources/js/chart.js")))
  }

  get("/charts/zones") {
    layoutTemplate("/WEB-INF/views/charts/zones-charts.ssp",
      ("styles", Nil),
      ("scripts", List("/resources/vendor/chartjs/chart.bundle.min.js",
        "/resources/js/charts/zones-charts.js")))
  }
}