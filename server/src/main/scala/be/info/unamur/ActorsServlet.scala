package be.info.unamur

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import be.info.unamur.actors.CityActor
import be.info.unamur.messages.{Initialize, Stop}
import grizzled.slf4j.Logger
import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}

/** Controls the actors system, for now it just serves to initialize/stop the actors and displays
  * the index page.
  *
  * @author Noé Picard
  * @param system the actor system
  */
class ActorsServlet(system: ActorSystem) extends ScalatraServlet with ScalateSupport {
  val logger: Logger = Logger[ActorsServlet]

  // The master actor (normally, other actors will be children of this one)
  val cityActor: ActorRef = system.actorOf(Props[CityActor], name = "cityActor")

  // Timeout for the ask messages to actors
  implicit val timeout = Timeout(10 seconds)


  /* Initializes the actors (for example, by opening the necessary connections to the phidgets) */
  get("/init") {
    contentType = "text"
    Await.ready(cityActor ? Initialize(), Duration.Inf).value.get match {
      case Success(_) =>
        logger.debug("Actors initialized")
        "Actors initialized..."
      case Failure(t) =>
        logger.error("Impossible to initialize the actors : ", t)
        "Impossible to initialize the actors, verify that all phidgets are connected...\n" +
          "More info : " + t.toString
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
        "Impossible to stop the actors, you should restart everything...\n" +
          "More info : " + t.getMessage
    }
  }
}
