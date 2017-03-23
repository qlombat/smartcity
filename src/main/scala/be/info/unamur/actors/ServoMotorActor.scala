package be.info.unamur.actors

import akka.actor.Actor
import com.phidgets.AdvancedServoPhidget

/**
  * Created by Noé Picard on 3/23/17.
  *
  * Servo motor actor.
  */
class ServoMotorActor extends Actor {
  override def receive: Receive = {
    case "OPEN_THE_BARRIER" =>
      val servo = new AdvancedServoPhidget()

      servo openAny()
      servo waitForAttachment()

      servo setEngaged(0, false)
      servo setSpeedRampingOn(0, false)
      servo setPosition(0, 100)
      servo setEngaged(0, true)

      // Open the barrier
      servo setSpeedRampingOn(0, true)
      servo setAcceleration(0, 100)
      servo setVelocityLimit(0, 200)
      servo setPosition(0, 190)

      // Close the barrier after 5 seconds
      Thread sleep 5000
      servo setPosition(0, 100)

      servo close()
  }
}
