package be.info.unamur.actors

import akka.actor.{Actor, ActorRef, Props}
import com.phidgets.InterfaceKitPhidget

/**
  * @author jeremyduchesne
  */
class CrossroadsActor(ik: InterfaceKitPhidget) extends Actor with SharedProperties{

  val pedestrianCrossingTime = 10000

  val trafficLights1: ActorRef = context.actorOf(Props(new TrafficLightsActor(ik)), name = "trafficLightsActor1")
  val trafficLights2: ActorRef = context.actorOf(Props(new TrafficLightsActor(ik)), name = "trafficLightsActor2")

  val pedestrianCrossing1 = context.actorOf(Props(new PedestrianTrafficLightActor(ik)), name = "pedestrianCrossing1")

  override def receive: Receive = {
    case Init() =>
      trafficLights1 ! Init(0, 1)
      trafficLights2 ! Init(2, 3)
      pedestrianCrossing1 ! Init(4)

    case Start() =>
      trafficLights1 ! SetGreen()
      trafficLights2 ! SetRed()
      pedestrianCrossing1 ! SetOn()

    case SecondaryCarComing()=>
      pedestrianCrossing1 ! SetOff()
      Thread.sleep(4000)
      trafficLights1 ! SetRed()
      trafficLights2 ! SetGreen()
      Thread.sleep(10000)
      trafficLights1 ! SetGreen()
      trafficLights2 ! SetRed()
      Thread.sleep(2000)
      pedestrianCrossing1 ! SetOn()
  }

}
