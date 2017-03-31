package be.info.unamur.actors

import akka.actor.Actor
import com.phidgets.{AdvancedServoPhidget, RFIDPhidget}

/** Implements the behavior of the barrier that controls the parking access.
  *
  * @author Noé Picard
  */
class BarrierActor(ik: RFIDPhidget) extends Actor with Messages {

  val sm = new AdvancedServoPhidget()


  override def receive: Receive = {
    /*
     * Initializes the motor by opening it and setting up the parameters.
     */
    case Init() =>
      sm openAny()
      sm waitForAttachment()

      sm setEngaged(BarrierActor.MotorIndex, false)
      sm setPosition(BarrierActor.MotorIndex, BarrierActor.MinPosition)
      sm setSpeedRampingOn(BarrierActor.MotorIndex, false)
      sm setEngaged(BarrierActor.MotorIndex, true)

    /*
     * Opens the barrier and wait for 5 seconds before closing it.
     */
    case OpenBarrier() =>
      sm setPosition(BarrierActor.MotorIndex, BarrierActor.MaxPosition)

      Thread.sleep(BarrierActor.WaitingTime)

      sm setPosition(BarrierActor.MotorIndex, BarrierActor.MinPosition)
  }
}

/**
  * Companion object for the barrier actor
  */
object BarrierActor {
  /* Constants */
  val MotorIndex: Int = 0
  val MinPosition: Int = 100
  val MaxPosition: Int = 180
  val WaitingTime:Int = 5000 //in milliseconds
}
