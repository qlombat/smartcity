package be.info.unamur.actors

import akka.actor.Actor
import com.phidgets.InterfaceKitPhidget

/**
  * @author Justin Sirjacques
  * @author Noé Picard
  */
class PedestrianTrafficLightActor(ik: InterfaceKitPhidget, yellowPin: Int) extends Actor {

  val lightYellowPin: Int = yellowPin


  override def receive: Receive = {
    case Init() =>
      ik.setOutputState(lightYellowPin, true)

    case SetOn() =>
      ik.setOutputState(lightYellowPin, true)

    case SetOff() =>
      ik.setOutputState(lightYellowPin, false)

    /* Makes the lights blinking 3 times and stops */
    case Stop() =>
      1 to 3 foreach { _ =>
        ik.setOutputState(lightYellowPin, true)
        Thread.sleep(500)
        ik.setOutputState(lightYellowPin, false)
        Thread.sleep(500)
      }
      sender ! StopFinished()
  }

}
