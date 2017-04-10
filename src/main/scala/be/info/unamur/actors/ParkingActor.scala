package be.info.unamur.actors

import akka.actor.{ActorRef, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import be.info.unamur.messages._
import be.info.unamur.utils.FailureSpreadingActor
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
class ParkingActor extends FailureSpreadingActor {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  val rfid = new RFIDPhidget()

  val barrierActor: ActorRef = context.actorOf(Props(new BarrierActor(rfid)), name = "barrierActor")

  // Sends the "open barrier" message when a RFID tag is read.
  val tagGainListener = new TagGainListener {
    override def tagGained(tagGainEvent: TagGainEvent): Unit = {
      barrierActor ! OpenBarrier()
    }
  }

  implicit val timeout = Timeout(5 seconds)


  override def receive: Receive = {
    case Initialize() =>
      rfid openAny()
      rfid waitForAttachment()

      rfid setAntennaOn true
      rfid addTagGainListener this.tagGainListener

      val initBarrierActor = barrierActor ? Initialize()

      val results = for {
        resultInitBarrierActor <- initBarrierActor
      } yield resultInitBarrierActor

      results pipeTo sender


    case Stop() =>
      val stopBarrierActor = barrierActor ? Stop()

      val results = for {
        resultStopBarrierActor <- stopBarrierActor
      } yield resultStopBarrierActor

      results pipeTo sender

  }
}
