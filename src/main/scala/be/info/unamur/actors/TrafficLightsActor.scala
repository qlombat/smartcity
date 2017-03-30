package be.info.unamur.actors

import akka.actor.Props

/**
  * @author jeremyduchesne
  */
class TrafficLightsActor extends CrossroadsActor {

  val blinkingTime = 4000

  val lightGreen = system.actorOf(Props[LightActor], name = "lightGreen")
  val lightRed = system.actorOf(Props[LightActor], name = "lightRed")

  override def receive: Receive = {
    case Init(1,2) => {
      lightGreen ! Init(1)
      lightRed ! Init(2)
    }
    case SwitchOn => {
      Thread.sleep(blinkingTime)
      lightRed ! SwitchOff
      lightGreen ! SwitchOn
    }
    case SwitchOff => {
      lightGreen ! Blink
      Thread.sleep(blinkingTime)
      lightRed ! SwitchOn
      lightGreen ! SwitchOff
    }
  }


}
