package be.info.unamur.actors

import akka.actor.{Actor, ActorRef, Props}
import com.phidgets.InterfaceKitPhidget

/**
  * @author jeremyduchesne
  * @author Quentin Lombat
  */
class CrossroadsActor(ik: InterfaceKitPhidget) extends Actor with Messages{

  val pedestrianCrossingTime = 10000

  val trafficLightsMain: ActorRef = context.actorOf(Props(new TrafficLightsActor(0,1,ik)), name = "trafficLightsActor1")
  val trafficLightsAuxiliary: ActorRef = context.actorOf(Props(new TrafficLightsActor(2,3,ik)), name = "trafficLightsActor2")

  val pedestrianCrossing1: ActorRef = context.actorOf(Props(new PedestrianTrafficLightActor(4,ik)), name = "pedestrianCrossing1")

  val secondaryCarDetector1: ActorRef = context.actorOf(Props(new PedestrianCarDetectorActor(ik, 0)), name = "SecondaryCarDetector")
  val secondaryCarDetector2: ActorRef = context.actorOf(Props(new PedestrianCarDetectorActor(ik, 1)), name = "SecondaryCarDetector")

  val pedestrianTouchDetector1: ActorRef = context.actorOf(Props(new PedestrianTouchActor(ik, 2)), name = "pedestrianDetector1")
  val pedestrianTouchDetector2: ActorRef = context.actorOf(Props(new PedestrianTouchActor(ik, 3)), name = "pedestrianCarDetector2")



  override def receive: Receive = {
    case Init() =>
      trafficLightsMain ! Init()
      trafficLightsAuxiliary ! Init()
      pedestrianCrossing1 ! Init()
      secondaryCarDetector1 ! Init()
      secondaryCarDetector2 ! Init()
      pedestrianTouchDetector1 ! Init()
      pedestrianTouchDetector2 ! Init()

    case Start() =>
      trafficLightsMain ! SetGreen()
      trafficLightsAuxiliary ! SetRed()
      pedestrianCrossing1 ! SetOn()
      secondaryCarDetector1 ! Start()
      secondaryCarDetector2 ! Start()
      pedestrianTouchDetector1 ! Start()
      pedestrianTouchDetector2 ! Start()

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

    case Pedestrian()=>
      Thread.sleep(4000)
      trafficLightsAuxiliary ! SetRed()
      trafficLightsMain ! SetRed()
      Thread.sleep(4000)
      pedestrianCrossing1 ! SetOn()
      Thread.sleep(4000)
      trafficLightsMain ! SetGreen()
  }

}
