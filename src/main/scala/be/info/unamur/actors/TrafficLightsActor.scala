package be.info.unamur.actors

import akka.actor.Actor
import com.phidgets.InterfaceKitPhidget

/**
  * @author jeremyduchesne
  * @author Noé Picard
  */
class TrafficLightsActor(ik: InterfaceKitPhidget, red: Int, green: Int) extends Actor {

  val lightRedPin  : Int = red
  val lightGreenPin: Int = green

  override def receive: Receive = {
    case Init() =>
      ik.setOutputState(lightRedPin, true)
      ik.setOutputState(lightGreenPin, true)

    case SetGreen() =>
      ik.setOutputState(lightRedPin, false)
      ik.setOutputState(lightGreenPin, true)

    case SetRed() =>
      ik.setOutputState(lightRedPin, true)
      ik.setOutputState(lightGreenPin, false)

    case Stop() =>
      1 to 3 foreach { _ =>
        ik.setOutputState(lightRedPin, true)
        ik.setOutputState(lightGreenPin, true)
        Thread.sleep(500)
        ik.setOutputState(lightRedPin, false)
        ik.setOutputState(lightGreenPin, false)
        Thread.sleep(500)
      }
      sender ! StopFinished()
  }


}
