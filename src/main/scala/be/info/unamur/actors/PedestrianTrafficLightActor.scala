package be.info.unamur.actors

import akka.actor.{Actor, ActorRef, Props}
import com.phidgets.InterfaceKitPhidget

/**
  * Created by jsirjacq on 31/03/17.
  */
class PedestrianTrafficLightActor(yellow: Int, ik: InterfaceKitPhidget) extends Actor with Messages {

  val lightYellowPin = yellow

  override def receive: Receive = {
    case Init() =>
      ik.setOutputState(lightYellowPin, true)

    case SetOn() =>
      ik.setOutputState(lightYellowPin, true)

    case SetOff() =>
      ik.setOutputState(lightYellowPin, false)
  }

}
