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
    /**
      * Initializes a basic situation. The barrier is closed by default.
      */
    case Initialize() =>
      sm openAny()
      sm waitForAttachment()

      sm setEngaged(BarrierActor.MotorIndex, false)
      sm setPosition(BarrierActor.MotorIndex, BarrierActor.OpenedPosition)
      sm setSpeedRampingOn(BarrierActor.MotorIndex, false)
      sm setEngaged(BarrierActor.MotorIndex, true)

      sender ! Initialized()

    /**
      * Opens the barrier.
      */
    case OpenBarrier() =>
      sm setPosition(BarrierActor.MotorIndex, BarrierActor.ClosedPosition)

    /**
      *  Waits 5 seconds after the loss of the signal and closes the barrier.
      */
    case CloseBarrier() =>
      Thread sleep BarrierActor.WaitingTime
      sm setPosition(BarrierActor.MotorIndex, BarrierActor.OpenedPosition)

    /**
      * Stops the servo motor
      */
    case Stop() =>
      sm close()
      sender ! Stopped()
  }
}

/**
  * Companion object for the barrier actor
  */
object BarrierActor {
  /* Constants */
  val MotorIndex    : Int = 0
  val OpenedPosition: Int = 100
  val ClosedPosition: Int = 0
  val WaitingTime   : Int = 5000 //in milliseconds
}
