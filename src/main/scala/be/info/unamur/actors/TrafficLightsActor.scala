package be.info.unamur.actors

import akka.actor.{Actor, ActorRef, Props}
import com.phidgets.InterfaceKitPhidget

/**
  * @author jeremyduchesne
  */
class TrafficLightsActor(ik: InterfaceKitPhidget) extends Actor with SharedProperties{

  val blinkingTime = 4000

  val lightGreen: ActorRef = context.actorOf(Props(new LightActor(ik)), name = "lightGreen")
  val lightRed: ActorRef = context.actorOf(Props(new LightActor(ik)), name = "lightRed")


  override def receive: Receive = {
    case Init(x: Int, y: Int) =>
      lightGreen ! Init(y)
      lightRed ! Init(x)

    case SetGreen() =>
      lightRed ! SwitchOff
      lightGreen ! SwitchOn

    case SetRed() =>
      lightRed ! SwitchOn
      lightGreen ! SwitchOff
  }


}
