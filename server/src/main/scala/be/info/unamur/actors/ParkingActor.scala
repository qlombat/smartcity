package be.info.unamur.actors

import akka.actor.{ActorRef, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import be.info.unamur.messages._
import be.info.unamur.utils.FailureSpreadingActor
import com.phidgets.RFIDPhidget
import com.phidgets.event.{TagGainEvent, TagGainListener, TagLossEvent, TagLossListener}
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

/** Implements the behaviour of the parking. Uses the RFID tag reader to pilot the barrier.
  *
  * @author Noé Picard
  * @author jeremyduchesne
  */
class ParkingActor extends FailureSpreadingActor {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  val rfid = new RFIDPhidget()

  // The BarrierActor that handles the RFID.
  val barrierActor: ActorRef = context.actorOf(Props(new BarrierActor(rfid)), name = "barrierActor")

  // Sends the "open barrier" message when a RFID tag is read.
  val tagGainListener = new TagGainListener {
    override def tagGained(tagGainEvent: TagGainEvent): Unit = {
      barrierActor ! OpenBarrier()
    }
  }

  // Sends the "close barrier" message when a RFID tag is lost.
  /*val tagLostListener = new TagLossListener {
    override def tagLost(tagLossEvent: TagLossEvent): Unit = {
      barrierActor ! CloseBarrier()
    }
  }*/

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

      results pipeTo sender

    case Start()=>
      rfid setAntennaOn true
      rfid addTagGainListener this.tagGainListener
      //rfid addTagLossListener this.tagLostListener

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
