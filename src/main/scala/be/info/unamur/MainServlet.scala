package be.info.unamur

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import be.info.unamur.actors.{CityActor, Init, Stop}
import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class MainServlet(system: ActorSystem) extends ScalatraServlet with ScalateSupport {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  val cityActor: ActorRef = system.actorOf(Props[CityActor], name = "cityActor")

  implicit val timeout = Timeout(5 seconds)

  get("/") {
    contentType = "text/html"
    layoutTemplate("/WEB-INF/templates/views/index.ssp")
  }

  /* Initializes the actors (for example, by opening the necessary connections to the phidgets)
   */
  get("/init_actors") {
    contentType = "text"
    cityActor ! Init()
    logger.debug("Actors initialized")
    "Actors initialized..."
  }

  /* Stops the actors (doesn't kill it but ask to do the necessary work to came back to the initial
   * state, like closing the connections to the phidgets)
   */
  get("/stop_actors") {
    contentType = "text/html"
    Await.ready(cityActor ? Stop(), Duration.Inf).value.get match {
      case Success(_) => "Actors stopped..."
      case Failure(t) =>
        logger.error("Impossible to stop the actors", t)
        "Impossible to stop the actors, you can try to restart..."
    }
  }
}