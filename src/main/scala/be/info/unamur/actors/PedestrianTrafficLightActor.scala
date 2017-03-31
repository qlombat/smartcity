package be.info.unamur.actors

import akka.actor.{Actor, ActorRef, Props}
import com.phidgets.InterfaceKitPhidget

/**
  * Created by jsirjacq on 31/03/17.
  */
class PedestrianTrafficLightActor(ik: InterfaceKitPhidget) extends Actor with SharedProperties{

  val lightYellow: ActorRef = context.actorOf(Props(new LightActor(ik)), name = "lightYellow")

  override def receive: Receive = {
    case Init(x: Int) =>
      lightYellow ! Init(x)

    case SetOn() =>
      lightYellow ! SwitchOn()

    case SetOff() =>
      lightYellow ! SwitchOff()
  }

}
