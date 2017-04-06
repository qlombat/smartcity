package be.info.unamur.actors

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.phidgets.RFIDPhidget
import com.phidgets.event.{TagGainEvent, TagGainListener}
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}

/** Implements the behavior of the parking. Uses the RFID tag reader to pilot the barrier.
  *
  * @author Noé Picard
  */
class ParkingActor extends Actor {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  val rfid = new RFIDPhidget()

  val barrierActor: ActorRef = context.actorOf(Props(new BarrierActor(rfid)), name = "barrierActor")

  /*
   * Sends the "open barrier" message when a RFID tag is read.
   */
  val tagGainListener = new TagGainListener {
    override def tagGained(tagGainEvent: TagGainEvent): Unit = {
      barrierActor ! OpenBarrier()
    }
  }

  implicit val timeout = Timeout(5 seconds)


  override def receive: Receive = {
    case Init() =>
      rfid openAny()
      rfid waitForAttachment()
      rfid setAntennaOn true

      rfid addTagGainListener this.tagGainListener

      barrierActor ! Init()

    case Stop() =>
      val stopBarrierActor = barrierActor ? Stop()

      val results = for {
        resultStopBarrierActor <- stopBarrierActor
      } yield resultStopBarrierActor

      Await.ready(results, Duration.Inf).value.get match {
        case Success(_) =>
          rfid removeTagGainListener this.tagGainListener
          rfid close()

        case Failure(t) =>
          logger.debug("Impossible to stop the actors", t)
      }

      sender ! StopFinished()

  }
}
