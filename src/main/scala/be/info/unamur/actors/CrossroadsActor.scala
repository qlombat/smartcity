package be.info.unamur.actors

import akka.actor.Props

/**
  * @author jeremyduchesne
  */
class CrossroadsActor extends CityActor {

  val trafficLights1 = system.actorOf(Props[TrafficLightsActor], name = "trafficLightsActor1")
  val trafficLights2 = system.actorOf(Props[TrafficLightsActor], name = "trafficLightsActor2")
  val trafficLights3 = system.actorOf(Props[TrafficLightsActor], name = "trafficLightsActor3")
  val trafficLights4 = system.actorOf(Props[TrafficLightsActor], name = "trafficLightsActor4")

  override def receive: Receive = {
    case Init(0,1,2,3,4,5,6,7) => {
      trafficLights1 ! Init(0,1)
      trafficLights2 ! Init(2,3)
      trafficLights3 ! Init(4,5)
      trafficLights4 ! Init(6,7)

      trafficLights1 ! SwitchOn
      trafficLights2 ! SwitchOff
      trafficLights3 ! SwitchOn
      trafficLights4 ! SwitchOff
    }
  }

}
