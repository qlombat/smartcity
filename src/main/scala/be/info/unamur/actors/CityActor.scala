package be.info.unamur.actors

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.phidgets.InterfaceKitPhidget
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
  * @author jeremyduchesne
  * @author Noé Picard
  */
class CityActor extends Actor {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  val ik = new InterfaceKitPhidget()

  val crossroadsActor: ActorRef = context.actorOf(Props(new CrossroadsActor(ik)), name = "crossroadsActor")
  val parkingActor   : ActorRef = context.actorOf(Props(new ParkingActor()), name = "parkingActor")

  var stopped: Boolean = true

  implicit val timeout = Timeout(5 seconds)


  override def receive: Receive = {
    case Init() =>
      ik openAny()
      ik waitForAttachment()

      crossroadsActor ! Init()
      parkingActor ! Init()

      Thread.sleep(2000)

      crossroadsActor ! Start()

      stopped = true

    case Stop() =>
      if (!stopped) {
        val stopCrossroadsFuture = crossroadsActor ? Stop()
        val stopParkingFuture = parkingActor ? Stop()

        val results = for {
          crossroadsResult <- stopCrossroadsFuture
          parkingResult <- stopParkingFuture
        } yield (crossroadsResult, parkingResult)

        Await.ready(results, Duration.Inf).value.get match {
          /* Kill all the actors hierarchy */
          case Success(_) =>
            ik close()
            stopped = true

          case Failure(t) =>
            logger.debug("Impossible the stop the actors", t)
        }
      }
  }
}
