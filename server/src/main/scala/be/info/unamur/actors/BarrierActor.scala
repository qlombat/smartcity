package be.info.unamur.actors

import be.info.unamur.messages._
import be.info.unamur.utils.FailureSpreadingActor
import com.phidgets.{AdvancedServoPhidget, RFIDPhidget}

/** Implements the behaviour of the barrier that controls the parking access.
  *
  * @author Noé Picard
  * @author jeremyduchesne
  */
class BarrierActor(ik: RFIDPhidget) extends FailureSpreadingActor {

  val sm = new AdvancedServoPhidget()

  override def receive: Receive = {
    /*
     * Initializes a basic situation. The barrier is closed by default.
     */
    case Initialize() =>
      sm open 305869
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
      sm setEngaged(BarrierActor.MotorIndex, true)
      sm setPosition(BarrierActor.MotorIndex, BarrierActor.OpenedPosition)
      sender ! Opened()

    /*
     *  Waits 5 seconds after the loss of the signal and closes the barrier.
     */
    case CloseBarrier() =>
      sm setEngaged(BarrierActor.MotorIndex, true)
      Thread sleep BarrierActor.WaitingTime
      sm setPosition(BarrierActor.MotorIndex, BarrierActor.ClosedPosition)
      sender ! Opened()

    /*
     * Stops the servo motor
     */
    case Stop() =>
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
  val MotorIndex    : Int = 0
  val OpenedPosition: Int = 0
  val ClosedPosition: Int = 66
  val WaitingTime   : Int = 5000 //in milliseconds
}
