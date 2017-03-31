package be.info.unamur.actors

import akka.actor.{Actor, ActorRef, Props}
import com.phidgets.InterfaceKitPhidget

/**
  * @author jeremyduchesne
  */
class CrossroadsActor(ik: InterfaceKitPhidget) extends Actor with Messages{

  val pedestrianCrossingTime = 10000

  val trafficLightsMain: ActorRef = context.actorOf(Props(new TrafficLightsActor(0,1,ik)), name = "trafficLightsActor1")
  val trafficLightsAuxiliary: ActorRef = context.actorOf(Props(new TrafficLightsActor(2,3,ik)), name = "trafficLightsActor2")

  val pedestrianCrossing1 = context.actorOf(Props(new PedestrianTrafficLightActor(4,ik)), name = "pedestrianCrossing1")

  override def receive: Receive = {
    case Init() =>
      trafficLightsMain ! Init()
      trafficLightsAuxiliary ! Init()
      pedestrianCrossing1 ! Init()

    case Start() =>
      trafficLightsMain ! SetGreen()
      trafficLightsAuxiliary ! SetRed()
      pedestrianCrossing1 ! SetOn()

    case SecondaryCarComing()=>
      pedestrianCrossing1 ! SetOff()
      Thread.sleep(4000)
      trafficLightsMain ! SetRed()
      trafficLightsAuxiliary ! SetGreen()
      Thread.sleep(10000)
      trafficLightsMain ! SetGreen()
      trafficLightsAuxiliary ! SetRed()
      Thread.sleep(2000)
      pedestrianCrossing1 ! SetOn()
  }

}
