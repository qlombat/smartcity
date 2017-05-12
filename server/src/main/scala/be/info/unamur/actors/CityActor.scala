package be.info.unamur.actors

import akka.actor.{Actor, ActorRef, PoisonPill, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import be.info.unamur.messages.{Initialize, Start, Stop}
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event.{AttachEvent, AttachListener, DetachEvent, DetachListener}
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}


/** Master actor that controls the other city sub-actors. It is directly called by the servlet.
  * This actor begins the initialization of all the sub-actors, and stops them when it is needed.
  *
  * @author jeremyduchesne
  * @author Noé Picard
  */
class CityActor extends Actor {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  val ik = new InterfaceKitPhidget()

  // The sub-actor that handles the crossroads.
  val crossroadsActor: ActorRef = context.actorOf(Props(new CrossroadsActor(ik)).withDispatcher("application-dispatcher"), name = "crossroadsActor")

  // The sub-actor that handles the parking.
  val parkingActor: ActorRef = context.actorOf(Props(new ParkingActor()).withDispatcher("application-dispatcher"), name = "parkingActor")

  // The sub-actor that handles the parking.
  val publicLightingActor: ActorRef = context.actorOf(Props(new PublicLightingActor(ik, 4, 5, 6, 7)).withDispatcher("application-dispatcher"), name = "publicLightningActor")

  // To know if the city is already stopped.
  var stopped: Boolean = true

  // Timeout for the asked messages to some actors.
  implicit val timeout = Timeout(5 seconds)

  implicit val executionContext: ExecutionContextExecutor = context.dispatcher

  val ikAttachListener = new AttachListener {
    override def attached(attachEvent: AttachEvent): Unit = {
      logger.debug("Interface Kit plugged")
      // The master actor (normally, other actors will be children of this one)
      Await.ready(self ? Stop(), Duration.Inf).value.get match {
        case Success(_) =>
          logger.debug("Actors stopped")
          self ! Start()
        case Failure(t) =>
          logger.debug("Impossible to stop the actors, may be there is no actor to stop.")
          self ! Start()
      }
    }
  }
  val ikDetachListener = new DetachListener {
    override def detached(detachEvent: DetachEvent): Unit = {
      logger.debug("Interface Kit unplugged")
    }
  }

  var listenerAdded = false

  override def receive: Receive = {

    /*
     * Initializes the interface kit and the sub-actors (crossroads, parking and publicLightning).
     */
    case Initialize() =>
      logger.debug("Initialize city")
      if (stopped) {
        stopped = false
        ik open CityActor.IKPhidgetId
        ik waitForAttachment()

        if(!listenerAdded){
          listenerAdded = true
          ik.addAttachListener(ikAttachListener)
          ik.addDetachListener(ikDetachListener)
        }

        val initCrossroads = crossroadsActor ? Initialize()
        val initParking = parkingActor ? Initialize()
        val initPublicLightning = publicLightingActor ? Initialize()

        val results = for {
          resultInitCrossroads <- initCrossroads
          resultInitParking <- initParking
          resultInitPublicLightning <- initPublicLightning
        } yield (resultInitCrossroads, resultInitParking, resultInitPublicLightning)

        Thread sleep 2000

        self ! Start()

        results pipeTo sender
      }


    case Start() =>
      logger.debug("Start city")
      crossroadsActor ! Start()
      publicLightingActor ! Start()
      parkingActor ! Start()

    /*
     * Stops the entire system by sending the Stop message to all the sub-actors.
     */
    case Stop() =>
      if (!stopped) {
        stopped = true

        val stopCrossroads = crossroadsActor ? Stop()
        val stopParking = parkingActor ? Stop()
        val stopPublicLightning = publicLightingActor ? Stop()

        val results = for {
          resultStopCrossroads <- stopCrossroads
          resultStopParking <- stopParking
          resultStopPublicLightning <- stopPublicLightning
        } yield (resultStopCrossroads, resultStopParking, resultStopPublicLightning)

        results pipeTo sender
      }
  }
}

object CityActor {
  val IKPhidgetId: Int = 445876
}