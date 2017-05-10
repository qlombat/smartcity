package be.info.unamur

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import be.info.unamur.actors.CityActor
import be.info.unamur.messages.{Initialize, Stop}
import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}


/** Controls the actors system, for now it just serves to initialize/stop the actors and displays
  * the index page.
  *
  * @author Noé Picard
  */
class ActorsServlet(system: ActorSystem) extends ScalatraServlet with ScalateSupport {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  // The master actor (normally, other actors will be children of this one)
  val cityActor: ActorRef = system.actorOf(Props[CityActor].withDispatcher("application-dispatcher"), name = "cityActor")

  // Timeout for the ask messages to actors
  implicit val timeout = Timeout(10 seconds)


  /* Starts the actors (for example, by opening the necessary connections to the phidgets) */
  get("/start") {
    contentType = "text"
    Await.ready(cityActor ? Initialize(), Duration.Inf).value.get match {
      case Success(_) =>
        logger.debug("Actors initialized")
        "Actors initialized..."
      case Failure(t) =>
        logger.error("Impossible to initialize the actors : ", t)
        "Impossible to initialize the actors, verify that all phidgets are connected..."
    }

  }

  /* Stops the actors (doesn't kill it but ask to do the necessary work to came back to the initial
   * state, like closing the connections to the phidgets)
   */
  get("/stop") {
    contentType = "text"
    Await.ready(cityActor ? Stop(), Duration.Inf).value.get match {
      case Success(_) =>
        logger.debug("Actors stopped")
        "Actors stopped..."
      case Failure(t) =>
        logger.error("Impossible to stop the actors : ", t)
        "Impossible to stop the actors, you should restart everything..."
    }
  }
}
