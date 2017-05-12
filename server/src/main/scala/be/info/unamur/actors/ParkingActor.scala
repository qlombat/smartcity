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


  val rfidAttachListener = new AttachListener {
    override def attached(attachEvent: AttachEvent): Unit ={
      logger.debug("RFID plugged")
      self ! Start()
    }
  }
  val rfidDetachListener = new DetachListener {
    override def detached(detachEvent: DetachEvent): Unit = {
      logger.debug("RFID unplugged")
      barrierActor ! OpenBarrier()
    }
  }
  var listenerAdded = false

  override def receive: Receive = {

    /*
     * Initializes the RFID listeners and the BarrierActor
     */
    case Initialize() =>
      rfid open ParkingActor.RfidPhidgetId
      rfid waitForAttachment()

      val initBarrierActor = barrierActor ? Initialize()

      if(!listenerAdded) {
        listenerAdded = true
        rfid.addAttachListener(rfidAttachListener)
        rfid.addDetachListener(rfidDetachListener)
      }

      val results = for {
        resultInitBarrierActor <- initBarrierActor
      } yield resultInitBarrierActor

      // Sends the "open barrier" message when a RFID tag is read.
      this.tagGainListener = new TagGainListener {
        override def tagGained(tagGainEvent: TagGainEvent): Unit = {
          logger.debug("Tag gain - " + context.self.path.name)
          var taken = 0
          var subscription = RfidSubscription.findAll()
          for (e <- subscription) {
            RfidTag.findAllBy(sqls"tag = ${e.tag} ORDER BY created_at DESC LIMIT 1") match {
              case Nil =>
              case l => if (l.head.entry == 1) taken += 1
            }
          }
          if (RfidSubscription.findAllBy(sqls"tag = ${tagGainEvent.getValue}").nonEmpty && (taken < ParkingActor.totalPlaces ||
            RfidTag.findAllBy(sqls"tag = ${tagGainEvent.getValue} ORDER BY created_at DESC LIMIT 1").head.entry == 1)) {
            rfid removeTagGainListener ParkingActor.this.tagGainListener
            rfid addTagLossListener ParkingActor.this.tagLossListener
            barrierActor ! OpenBarrier()
            RfidTag.create(context.self.path.name, tagGainEvent.getValue, new Timestamp(System.currentTimeMillis()))
          }
        }
      }

      // Sends the "close barrier" message when a RFID tag is lost.
      this.tagLossListener = new TagLossListener {
        override def tagLost(tagLossEvent: TagLossEvent): Unit = {
          logger.debug("Tag loss - " + context.self.path.name)
          if (RfidSubscription.findAllBy(sqls"tag = ${tagLossEvent.getValue}").nonEmpty) {
            barrierActor ! CloseBarrier()
            rfid removeTagLossListener ParkingActor.this.tagLossListener
          }
        }
      }

      rfid setAntennaOn true

      results pipeTo sender


    case Closed() =>
      rfid addTagGainListener this.tagGainListener


    case Start() =>
      barrierActor ! Start()
      rfid addTagGainListener this.tagGainListener
      rfid removeTagLossListener this.tagLossListener


    /*
     * Stops the BarrierActor.
     */
    case Stop() =>
      listenerAdded = false
      rfid.removeAttachListener(rfidAttachListener)
      rfid.removeDetachListener(rfidDetachListener)

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
  var totalPlaces = 3
}
