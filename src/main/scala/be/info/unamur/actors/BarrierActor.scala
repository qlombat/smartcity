package be.info.unamur.actors

import akka.actor.Actor
import com.phidgets.{AdvancedServoPhidget, RFIDPhidget}

/**
  * @author Noé Picard
  */
class BarrierActor(ik: RFIDPhidget) extends Actor with SharedProperties {
  val sm = new AdvancedServoPhidget()

  override def receive: Receive = {
    case Init() =>
      sm openAny()
      sm waitForAttachment()

      sm setEngaged(0, false)
      sm setSpeedRampingOn(0, false)
      sm setPosition(0, 100)
      sm setEngaged(0, true)

    case OpenBarrier =>
      // Open the barrier
      sm setSpeedRampingOn(0, true)
      sm setAcceleration(0, 100)
      sm setVelocityLimit(0, 200)
      sm setPosition(0, 190)

      // Close the barrier after 5 seconds
      Thread sleep 5000
      sm setPosition(0, 100)
  }
}
