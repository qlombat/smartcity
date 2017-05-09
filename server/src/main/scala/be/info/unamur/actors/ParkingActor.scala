package be.info.unamur.actors

import java.sql.Timestamp

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import be.info.unamur.messages._
import be.info.unamur.persistence.entities.{RfidSubscription, RfidTag}
import com.phidgets.RFIDPhidget
import com.phidgets.event._
import org.slf4j.{Logger, LoggerFactory}
import scalikejdbc._

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._
import scala.language.postfixOps


/** Implements the behaviour of the parking. Uses the RFID tag reader to pilot the barrier.
  *
  * @author Noé Picard
  * @author jeremyduchesne
  */
class ParkingActor extends Actor {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  var tagGainListener: TagGainListener = _

  var tagLossListener: TagLossListener = _


  val rfid = new RFIDPhidget()

  // The BarrierActor that handles the RFID.
  val barrierActor: ActorRef = context.actorOf(Props(new BarrierActor(rfid)).withDispatcher("application-dispatcher"), name = "barrierActor")

  // Timeout for the asked messages to some actors.
  implicit val timeout = Timeout(5 seconds)

  implicit val executionContext: ExecutionContextExecutor = context.dispatcher


  override def receive: Receive = {

    /*
     * Initializes the RFID listeners and the BarrierActor
     */
    case Initialize() =>
      rfid open ParkingActor.RfidPhidgetId
      rfid waitForAttachment()

      val initBarrierActor = barrierActor ? Initialize()

      val results = for {
        resultInitBarrierActor <- initBarrierActor
      } yield resultInitBarrierActor

      // Sends the "open barrier" message when a RFID tag is read.
      this.tagGainListener = new TagGainListener {
        override def tagGained(tagGainEvent: TagGainEvent): Unit = {
          rfid removeTagGainListener ParkingActor.this.tagGainListener
          rfid addTagLossListener ParkingActor.this.tagLossListener
          if (!RfidSubscription.findAllBy(sqls"tag = ${tagGainEvent.getValue}").isEmpty) {
            barrierActor ! OpenBarrier()
            RfidTag.create(context.self.path.name, tagGainEvent.getValue, new Timestamp(System.currentTimeMillis()))
          }
        }
      }

      // Sends the "close barrier" message when a RFID tag is lost.
      this.tagLossListener = new TagLossListener {
        override def tagLost(tagLossEvent: TagLossEvent): Unit = {
          if (!RfidSubscription.findAllBy(sqls"tag = ${tagLossEvent.getValue}").isEmpty) {
            barrierActor ! CloseBarrier()
            rfid removeTagLossListener ParkingActor.this.tagLossListener
          }
        }
      }

      results pipeTo sender


    case Closed() =>
      rfid addTagGainListener this.tagGainListener


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
  val RfidPhidgetId: Int = 384608
}
