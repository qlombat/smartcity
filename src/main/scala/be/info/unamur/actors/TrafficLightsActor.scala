package be.info.unamur.actors

import akka.actor.{ActorSystem, Props}

/**
  * @author jeremyduchesne
  */
class TrafficLightsActor(system: ActorSystem) extends CrossroadsActor(system) {

  val blinkingTime = 4000

  val lightGreen = system.actorOf(Props(new LightActor(system)), name = "lightGreen")
  val lightRed = system.actorOf(Props(new LightActor(system)), name = "lightRed")

  override def receive: Receive = {
    case Init(x:Int,y:Int) => {
      lightGreen ! Init(x)
      lightRed ! Init(y)
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
