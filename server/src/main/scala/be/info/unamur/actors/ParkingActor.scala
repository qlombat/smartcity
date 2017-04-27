package be.info.unamur.actors

import java.sql.Timestamp

import akka.actor.FSM.Failure
import akka.actor.Status.Success
import akka.actor.{ActorRef, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import be.info.unamur.messages._
import be.info.unamur.persistence.entities.RfidTag
import be.info.unamur.utils.FailureSpreadingActor
import com.phidgets.RFIDPhidget
import com.phidgets.event._
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

/** Implements the behaviour of the parking. Uses the RFID tag reader to pilot the barrier.
  *
  * @author Noé Picard
  * @author jeremyduchesne
  */
class ParkingActor extends FailureSpreadingActor {

  var tagGainListener: TagGainListener = _

  var tagLossListener: TagLossListener = _

  val logger: Logger = LoggerFactory.getLogger(getClass)

  val rfid = new RFIDPhidget()

  // The BarrierActor that handles the RFID.
  val barrierActor: ActorRef = context.actorOf(Props(new BarrierActor(rfid)), name = "barrierActor")

  // Timeout for the asked messages to some actors.
  implicit val timeout = Timeout(5 seconds)


  override def receive: Receive = {

    /*
     * Initializes the RFID listeners and the BarrierActor
     */
    case Initialize() =>
      rfid open 384608
      rfid waitForAttachment()

      val initBarrierActor = barrierActor ? Initialize()

      val results = for {
        resultInitBarrierActor <- initBarrierActor
      } yield resultInitBarrierActor

      // Sends the "open barrier" message when a RFID tag is read.
      this.tagGainListener = new TagGainListener {
        override def tagGained(tagGainEvent: TagGainEvent): Unit = {
          RfidTag.create(context.self.path.name, rfid.getLastTag, new Timestamp(System.currentTimeMillis()))
          barrierActor ! OpenBarrier()
        }
      }

      // Sends the "close barrier" message when a RFID tag is lost.
      this.tagLossListener = new TagLossListener {
        override def tagLost(tagLossEvent: TagLossEvent): Unit = {
          barrierActor ! CloseBarrier()
        }
      }

      results pipeTo sender

    case Opened() =>
      rfid setAntennaOn true
      rfid removeTagGainListener this.tagGainListener
      rfid addTagLossListener this.tagLossListener

    case Closed() =>
      rfid setAntennaOn true
      rfid addTagGainListener this.tagGainListener
      rfid removeTagLossListener this.tagLossListener

    case Start() =>
      rfid setAntennaOn true
      rfid addTagGainListener this.tagGainListener
      rfid removeTagLossListener this.tagLossListener


    /*
     * Stops the BarrierActor.
     */
    case Stop() =>
      val stopBarrierActor = barrierActor ? Stop()

      val results = for {
        resultStopBarrierActor <- stopBarrierActor
      } yield resultStopBarrierActor

      results pipeTo sender


  }
}

/** Companion object for the AuxiliaryCarDetectorActor
  *
  * @author Justin SIRJACQUES
  */
object ParkingActor {
  /* Constants */

  // Minimum delay between two DB updates (seconds)
  val delayDBUpdate = 3
}
