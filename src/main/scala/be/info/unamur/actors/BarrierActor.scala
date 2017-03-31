package be.info.unamur.actors

import akka.actor.Actor
import com.phidgets.{AdvancedServoPhidget, RFIDPhidget}

/**
  * @author Noé Picard
  */
class BarrierActor(ik: RFIDPhidget) extends Actor with Messages {
  val sm = new AdvancedServoPhidget()

  override def receive: Receive = {
    case Init() =>
      sm openAny()
      sm waitForAttachment()

      sm setEngaged(0, false)
      sm setPosition(0, 100)
      sm setSpeedRampingOn (0, false)
      sm setEngaged(0, true)

    case OpenBarrier() =>
      // Open the barrier
      sm setPosition(0, 180)

      // Close the barrier after 5 seconds
      Thread.sleep(5000)

      sm setPosition(0, 100)
  }
}
