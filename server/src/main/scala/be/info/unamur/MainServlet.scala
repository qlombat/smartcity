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
      ("scripts", List("/resources/vendor/momentjs/moment.min.js", "/resources/js/dashboard.js", "/resources/vendor/jquery/jquery.mousewheel.min.js", "/resources/vendor/snapsvg/snap.svg-min.js", "/resources/js/map.js")))
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

  get("/charts/presence") {
    layoutTemplate("/WEB-INF/views/charts/presence-charts.ssp",
      ("styles", List("/resources/css/presence-chart.css")),
      ("scripts", List("/resources/vendor/chartjs/chart.bundle.min.js","/resources/js/charts/presence-charts/presence-chart-utils.js",
        "/resources/js/charts/presence-charts/all-bar-chart.js", "/resources/js/charts/presence-charts/all-doughnut-chart.js",
        "/resources/js/charts/presence-charts/all-line-chart.js")))
  }

  get("/charts/zones") {
    layoutTemplate("/WEB-INF/views/charts/zones-charts.ssp",
      ("styles", List("/resources/vendor/datatables-plugins/dataTables.bootstrap.css",
        "/resources/vendor/datatables-responsive/dataTables.responsive.css",
        "/resources/css/zones-charts.css")),
      ("scripts", List("/resources/vendor/datatables/js/jquery.dataTables.min.js",
        "/resources/vendor/datatables-plugins/dataTables.bootstrap.min.js",
        "/resources/vendor/datatables-responsive/dataTables.responsive.js",
        "/resources/vendor/momentjs/moment.min.js",
        "/resources/vendor/momentjs/moment-duration-format.js",
        "/resources/vendor/chartjs/chart.bundle.min.js",
        "/resources/js/charts/zones-charts.js")))
  }

  get("/charts/luminosity") {
    layoutTemplate("/WEB-INF/views/charts/luminosity-charts.ssp",
      ("styles", Nil),
      ("scripts", List("/resources/vendor/chartjs/chart.bundle.min.js", "/resources/js/charts/charts-utils.js",
        "/resources/js/charts/luminosity-charts.js")))
  }

  get("/charts/humidity") {
    layoutTemplate("/WEB-INF/views/charts/humidity-charts.ssp",
      ("styles", Nil),
      ("scripts", List("/resources/vendor/chartjs/chart.bundle.min.js","/resources/js/charts/charts-utils.js",
        "/resources/js/charts/humidity-charts.js")))
  }

  get("/charts/temperature") {
    layoutTemplate("/WEB-INF/views/charts/temperature-charts.ssp",
      ("styles", Nil),
      ("scripts", List("/resources/vendor/chartjs/chart.bundle.min.js","/resources/js/charts/charts-utils.js",
        "/resources/js/charts/temperature-charts.js")))
  }

}