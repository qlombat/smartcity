package be.info.unamur.actors

import java.sql.Timestamp

import akka.actor.Actor
import be.info.unamur.messages._
import be.info.unamur.persistence.entities.Sensor
import com.phidgets.event.{AttachEvent, AttachListener, DetachEvent, DetachListener}
import com.phidgets.{AdvancedServoPhidget, RFIDPhidget}
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}


/** Implements the behaviour of the barrier that controls the parking access.
  *
  * @author Noé Picard
  * @author jeremyduchesne
  */
class BarrierActor(ik: RFIDPhidget) extends Actor {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  val sm = new AdvancedServoPhidget()

  val smAttachListener = new AttachListener {
    override def attached(attachEvent: AttachEvent): Unit = {
      logger.debug("Barrier plugged")
      self ! Start()
    }
  }
  val smDetachListener = new DetachListener {
    override def detached(detachEvent: DetachEvent): Unit = {
      logger.debug("Barrier unplugged")
    }
  }

  var listenerAdded = false
  var started = false
  var engaged = false

  override def receive: Receive = {
    /*
     * Initializes a basic situation. The barrier is closed by default.
     */
    case Initialize() =>
      sm open BarrierActor.MotorPhidgetId
      sm waitForAttachment()

      if(!listenerAdded){
        listenerAdded = true
        sm.addAttachListener(smAttachListener)
        sm.addDetachListener(smDetachListener)
      }

      self ! Start()
      sender ! Initialized()

    /*
     * Opens the barrier.
     */
    case OpenBarrier() =>
      logger.debug("OpenBarrier - " + context.self.path.name)
      Sensor.create("barrier", 1, 1, new Timestamp(System.currentTimeMillis()))
      if (sm.isAttached) {
        if(started && !sm.getEngaged(BarrierActor.MotorIndex)){
          self ! Start()
        }
        sm setPosition(BarrierActor.MotorIndex, BarrierActor.OpenedPosition)
      }
    /*
     *  Waits 5 seconds after the loss of the signal and closes the barrier.
     */
    case CloseBarrier() =>

      logger.debug("CloseBarrier - " + context.self.path.name)
      Thread sleep BarrierActor.WaitingTime
      Sensor.create("barrier", 0, 0, new Timestamp(System.currentTimeMillis()))
      if (sm.isAttached) {
        if(started && !sm.getEngaged(BarrierActor.MotorIndex)){
          self ! Start()
        }
        sm setPosition(BarrierActor.MotorIndex, BarrierActor.ClosedPosition)
      }
      sender ! Closed()


    /*
     * Start the servo motor
     */
    case Start() =>
      started = true
      if(!listenerAdded){
        listenerAdded = true
        sm.addAttachListener(smAttachListener)
        sm.addDetachListener(smDetachListener)
      }
      if(sm.isAttached){
        sm setServoType(BarrierActor.MotorIndex, AdvancedServoPhidget.PHIDGET_SERVO_TOWERPRO_MG90)
        sm setEngaged(BarrierActor.MotorIndex, false)
        sm setSpeedRampingOn(BarrierActor.MotorIndex, false)
        sm setPosition(BarrierActor.MotorIndex, BarrierActor.ClosedPosition)
        sm setEngaged(BarrierActor.MotorIndex, true)
        Sensor.create("barrier", 0, 0, new Timestamp(System.currentTimeMillis()))
      }

      sender ! Stopped()

    /*
     * Stops the servo motor
     */
    case Stop() =>
      started = false
      listenerAdded = false
      sm.removeAttachListener(smAttachListener)
      sm.removeDetachListener(smDetachListener)

      if (sm.isAttached) {
        sm setPosition(BarrierActor.MotorIndex, BarrierActor.ClosedPosition)
        Sensor.create("barrier", 0, 0, new Timestamp(System.currentTimeMillis()))
        sm close()
      }
      sender ! Stopped()
  }
}

/** Companion object for the barrier actor
  *
  * @author Noé Picard
  */
object BarrierActor {
  /* Constants */
  val MotorPhidgetId: Int = 305869
  val MotorIndex: Int = 0
  val OpenedPosition: Int = 66
  val ClosedPosition: Int = 3
  val WaitingTime: Int = 5000 //in milliseconds
}
