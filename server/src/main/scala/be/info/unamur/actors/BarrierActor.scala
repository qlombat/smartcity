package be.info.unamur.actors

import java.sql.Timestamp

import akka.actor.Actor
import be.info.unamur.messages._
import be.info.unamur.persistence.entities.Sensor
import com.phidgets.{AdvancedServoPhidget, RFIDPhidget}


/** Implements the behaviour of the barrier that controls the parking access.
  *
  * @author Noé Picard
  * @author jeremyduchesne
  */
class BarrierActor(ik: RFIDPhidget) extends Actor {

  val sm = new AdvancedServoPhidget()

  override def receive: Receive = {
    /*
     * Initializes a basic situation. The barrier is closed by default.
     */
    case Initialize() =>
      sm open BarrierActor.MotorPhidgetId
      sm waitForAttachment()

      sm setServoType(BarrierActor.MotorIndex, AdvancedServoPhidget.PHIDGET_SERVO_HITEC_HS422)
      sm setEngaged(BarrierActor.MotorIndex, false)
      sm setSpeedRampingOn(BarrierActor.MotorIndex, false)
      sm setPosition(BarrierActor.MotorIndex, BarrierActor.ClosedPosition)
      sm setEngaged(BarrierActor.MotorIndex, true)

      sender ! Initialized()

    /*
     * Opens the barrier.
     */
    case OpenBarrier() =>
      Sensor.create("barrier", 1, 1, new Timestamp(System.currentTimeMillis()))
      sm setPosition(BarrierActor.MotorIndex, BarrierActor.OpenedPosition)


    /*
     *  Waits 5 seconds after the loss of the signal and closes the barrier.
     */
    case CloseBarrier() =>
      Thread sleep BarrierActor.WaitingTime
      Sensor.create("barrier", 0, 0, new Timestamp(System.currentTimeMillis()))
      sm setPosition(BarrierActor.MotorIndex, BarrierActor.ClosedPosition)
      sender ! Closed()


    /*
     * Stops the servo motor
     */
    case Stop() =>
      sm setPosition(BarrierActor.MotorIndex, BarrierActor.ClosedPosition)
      sm close()
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
  val MotorIndex    : Int = 0
  val OpenedPosition: Int = 0
  val ClosedPosition: Int = 66
  val WaitingTime   : Int = 5000 //in milliseconds
}
