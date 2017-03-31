package be.info.unamur.actors

import akka.actor.{Actor, ActorRef, Props}
import com.phidgets.InterfaceKitPhidget

/**
  * @author jeremyduchesne
  */
class TrafficLightsActor(ik: InterfaceKitPhidget) extends Actor with SharedProperties {

  val blinkingTime = 4000

  val lightGreen: ActorRef = context.actorOf(Props(new LightActor(ik)), name = "lightGreen")
  val lightRed: ActorRef = context.actorOf(Props(new LightActor(ik)), name = "lightRed")

  override def receive: Receive = {
    case Init(x: Int, y: Int) =>
      lightGreen ! Init(x)
      lightRed ! Init(y)

    case SwitchOn =>
      Thread.sleep(blinkingTime)
      lightRed ! SwitchOff
      lightGreen ! SwitchOn

    case SwitchOff =>
      lightGreen ! Blink
      Thread.sleep(blinkingTime)
      lightRed ! SwitchOn
      lightGreen ! SwitchOff
  }


}
