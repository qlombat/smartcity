package be.info.unamur.actors

import akka.actor.{Actor, ActorRef, Props}
import com.phidgets.InterfaceKitPhidget

/**
  * @author jeremyduchesne
  */
class TrafficLightsActor(red: Int, green: Int, ik: InterfaceKitPhidget) extends Actor with SharedProperties{

  val blinkingTime = 4000
  val lightRedPin = red
  val lightGreenPin = green

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
  }


}
