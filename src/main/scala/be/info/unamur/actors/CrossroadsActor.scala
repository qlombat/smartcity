package be.info.unamur.actors

import akka.actor.{Actor, ActorRef, Props}
import com.phidgets.InterfaceKitPhidget

/**
  * @author jeremyduchesne
  */
class CrossroadsActor(ik: InterfaceKitPhidget) extends Actor with SharedProperties {

  val pedestrianCrossingTime = 10000

  val trafficLights1: ActorRef = context.actorOf(Props(new TrafficLightsActor(ik)), name = "trafficLightsActor1")
  val trafficLights2: ActorRef = context.actorOf(Props(new TrafficLightsActor(ik)), name = "trafficLightsActor2")

  // val pedestrianCrossing1 = system.actorOf(Props[TrafficLightsActor], name = "pedestrianCrossing1")

  override def receive: Receive = {
    case Init() =>
      trafficLights1 ! Init(0, 1)
      trafficLights2 ! Init(2, 3)

      //TODO Create pedestrianCrossingActor, they only use a single LED
      //pedestrianCrossing1 ! Init(4)

      //trafficLights1 ! SwitchOn
      //trafficLights2 ! SwitchOff
      //pedestrianCrossing1 ! SwitchOn

    case Pedestrian(pedestrianPresent) =>
      if (pedestrianPresent) {
        trafficLights2 ! SwitchOff
        trafficLights1 ! SwitchOn

        // pedestrianCrossing1 ! SwitchOn

        Thread.sleep(pedestrianCrossingTime)

        sender ! Pedestrian(false)
      } else {
        trafficLights2 ! SwitchOn
        trafficLights1 ! SwitchOff

        // pedestrianCrossing1 ! SwitchOff
      }
  }

}
