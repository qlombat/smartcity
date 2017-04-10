package be.info.unamur.actors

import akka.actor.{ActorRef, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import be.info.unamur.messages._
import be.info.unamur.utils.FailureSpreadingActor
import com.phidgets.InterfaceKitPhidget
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * @author jeremyduchesne
  * @author Quentin Lombat
  */
class CrossroadsActor(ik: InterfaceKitPhidget) extends FailureSpreadingActor {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  val trafficLightsMainActor     : ActorRef = context.actorOf(Props(new TrafficLightsActor(ik, 0, 1)), name = "trafficLightsMainActor")
  val trafficLightsAuxiliaryActor: ActorRef = context.actorOf(Props(new TrafficLightsActor(ik, 2, 3)), name = "trafficLightsAuxiliaryActor")

  val pedestrianCrossingActor: ActorRef = context.actorOf(Props(new PedestrianTrafficLightActor(ik, 4)), name = "pedestrianCrossingActor")

  val secondaryCarDetectorActor1: ActorRef = context.actorOf(Props(new PedestrianCarDetectorActor(ik, 0)), name = "secondaryCarDetectorActor1")
  val secondaryCarDetectorActor2: ActorRef = context.actorOf(Props(new PedestrianCarDetectorActor(ik, 1)), name = "secondaryCarDetectorActor2")

  val pedestrianTouchDetectorActor1: ActorRef = context.actorOf(Props(new PedestrianTouchActor(ik, 2)), name = "pedestrianTouchDetectorActor1")
  val pedestrianTouchDetectorActor2: ActorRef = context.actorOf(Props(new PedestrianTouchActor(ik, 3)), name = "pedestrianTouchDetectorActor2")

  implicit val timeout = Timeout(5 seconds)


  override def receive: Receive = {
    case Initialize() =>
      val initTrafficLightsMainActor = trafficLightsMainActor ? Initialize()
      val initTrafficLightsAuxiliaryActor = trafficLightsAuxiliaryActor ? Initialize()
      val initPedestrianCrossingActor = pedestrianCrossingActor ? Initialize()
      val initSecondaryCarDetectorActor1 = secondaryCarDetectorActor1 ? Initialize()
      val initSecondaryCarDetectorActor2 = secondaryCarDetectorActor2 ? Initialize()
      val initPedestrianTouchDetectorActor1 = pedestrianTouchDetectorActor1 ? Initialize()
      val initPedestrianTouchDetectorActor2 = pedestrianTouchDetectorActor2 ? Initialize()

      val results = for {
        resultInitTrafficLightsMainActor <- initTrafficLightsMainActor
        resultInitTrafficLightsAuxiliaryActor <- initTrafficLightsAuxiliaryActor
        resultInitPedestrianCrossingActor <- initPedestrianCrossingActor
        resultInitSecondaryCarDetectorActor1 <- initSecondaryCarDetectorActor1
        resultInitSecondaryCarDetectorActor2 <- initSecondaryCarDetectorActor2
        resultInitPedestrianTouchDetectorActor1 <- initPedestrianTouchDetectorActor1
        resultInitPedestrianTouchDetectorActor2 <- initPedestrianTouchDetectorActor2
      } yield (resultInitTrafficLightsMainActor,
        resultInitTrafficLightsAuxiliaryActor,
        resultInitPedestrianCrossingActor,
        resultInitSecondaryCarDetectorActor1,
        resultInitSecondaryCarDetectorActor2,
        resultInitPedestrianTouchDetectorActor1,
        resultInitPedestrianTouchDetectorActor2)

      results pipeTo sender


    case Start() =>
      trafficLightsMainActor ! SetGreen()
      trafficLightsAuxiliaryActor ! SetRed()
      pedestrianCrossingActor ! SetOn()
      secondaryCarDetectorActor1 ! Start()
      secondaryCarDetectorActor2 ! Start()
      pedestrianTouchDetectorActor1 ! Start()
      pedestrianTouchDetectorActor2 ! Start()


    case OpenAuxiliary() =>
      pedestrianCrossingActor ! SetOff()
      Thread sleep 4000
      trafficLightsMainActor ! SetRed()
      trafficLightsAuxiliaryActor ! SetGreen()
      Thread sleep 10000
      trafficLightsMainActor ! SetGreen()
      trafficLightsAuxiliaryActor ! SetRed()
      Thread sleep 2000
      pedestrianCrossingActor ! SetOn()


    case Pedestrian() =>
      Thread sleep 4000
      trafficLightsAuxiliaryActor ! SetRed()
      trafficLightsMainActor ! SetRed()
      Thread sleep 4000
      pedestrianCrossingActor ! SetOn()
      Thread sleep 4000
      trafficLightsMainActor ! SetGreen()
      pedestrianCrossingActor ! SetOn()


    case Stop() =>
      val stopTrafficLightsMainActor = trafficLightsMainActor ? Stop()
      val stopTrafficLightsAuxiliaryActor = trafficLightsAuxiliaryActor ? Stop()
      val stopPedestrianCrossingActor = pedestrianCrossingActor ? Stop()
      val stopSecondaryCarDetectorActor1 = secondaryCarDetectorActor1 ? Stop()
      val stopSecondaryCarDetectorActor2 = secondaryCarDetectorActor2 ? Stop()
      val stopPedestrianTouchDetectorActor1 = pedestrianTouchDetectorActor1 ? Stop()
      val stopPedestrianTouchDetectorActor2 = pedestrianTouchDetectorActor2 ? Stop()

      val results = for {
        resultStopTrafficLightsMainActor <- stopTrafficLightsMainActor
        resultStopTrafficLightsAuxiliaryActor <- stopTrafficLightsAuxiliaryActor
        resultStopPedestrianCrossingActor <- stopPedestrianCrossingActor
        resultStopSecondaryCarDetectorActor1 <- stopSecondaryCarDetectorActor1
        resultStopSecondaryCarDetectorActor2 <- stopSecondaryCarDetectorActor2
        resultStopPedestrianTouchDetectorActor1 <- stopPedestrianTouchDetectorActor1
        resultStopPedestrianTouchDetectorActor2 <- stopPedestrianTouchDetectorActor2
      } yield (resultStopTrafficLightsMainActor,
        resultStopTrafficLightsAuxiliaryActor,
        resultStopPedestrianCrossingActor,
        resultStopSecondaryCarDetectorActor1,
        resultStopSecondaryCarDetectorActor2,
        resultStopPedestrianTouchDetectorActor1,
        resultStopPedestrianTouchDetectorActor2)

      results pipeTo sender
  }

}
