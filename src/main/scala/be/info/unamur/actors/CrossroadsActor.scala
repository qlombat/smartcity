package be.info.unamur.actors

import akka.actor.Props

/**
  * @author jeremyduchesne
  */
class CrossroadsActor extends CityActor {

  val pedestrianCrossingTime = 10000

  val trafficLights1 = system.actorOf(Props[TrafficLightsActor], name = "trafficLightsActor1")
  val trafficLights2 = system.actorOf(Props[TrafficLightsActor], name = "trafficLightsActor2")
  val trafficLights3 = system.actorOf(Props[TrafficLightsActor], name = "trafficLightsActor3")
  val trafficLights4 = system.actorOf(Props[TrafficLightsActor], name = "trafficLightsActor4")

  val pedestrianCrossing1 = system.actorOf(Props[TrafficLightsActor], name = "pedestrianCrossing1")
  val pedestrianCrossing2 = system.actorOf(Props[TrafficLightsActor], name = "pedestrianCrossing2")

  override def receive: Receive = {
    case Init(0,1,2,3,4,5,6,7,8,9) => {
      trafficLights1 ! Init(0,1)
      trafficLights2 ! Init(2,3)
      trafficLights3 ! Init(4,5)
      trafficLights4 ! Init(6,7)
      pedestrianCrossing1 ! Init(8,9)
      pedestrianCrossing2 ! Init(8,9)

      trafficLights1 ! SwitchOn
      trafficLights2 ! SwitchOff
      trafficLights3 ! SwitchOn
      trafficLights4 ! SwitchOff
    }
    case Pedestrian(pedestrianPresent) => {
      if(pedestrianPresent) {
        trafficLights2 ! SwitchOff
        trafficLights4 ! SwitchOff
        trafficLights1 ! SwitchOn
        trafficLights3 ! SwitchOn

        pedestrianCrossing1 ! SwitchOn
        pedestrianCrossing2 ! SwitchOn

        Thread.sleep(pedestrianCrossingTime)

        sender ! Pedestrian(false)
      } else {
        trafficLights2 ! SwitchOn
        trafficLights4 ! SwitchOn
        trafficLights1 ! SwitchOff
        trafficLights3 ! SwitchOff

        pedestrianCrossing1 ! SwitchOff
        pedestrianCrossing2 ! SwitchOff
      }
    }
  }

}
