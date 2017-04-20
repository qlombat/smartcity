package be.info.unamur.actors

import akka.actor.{ActorRef, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import be.info.unamur.messages._
import be.info.unamur.utils.FailureSpreadingActor
import com.phidgets.InterfaceKitPhidget
import org.joda.time.DateTime
import org.slf4j.{Logger, LoggerFactory}
import org.joda.time.Seconds

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

/** This actor handles the behaviour of the crossroads. It controls all the sub-actors needed by the crossroads.
  *
  * @author jeremyduchesne
  * @author Quentin Lombat
  */
class CrossroadsActor(ik: InterfaceKitPhidget) extends FailureSpreadingActor {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  // The TrafficLightsActor that handles the two traffic lights of the main road of the model.
  val trafficLightsMainActor     : ActorRef = context.actorOf(Props(new TrafficLightsActor(ik, 0, 1)), name = "trafficLightsMainActor")
  // The TrafficLightsActor that handles the two traffic lights of the auxiliary road of the model.
  val trafficLightsAuxiliaryActor: ActorRef = context.actorOf(Props(new TrafficLightsActor(ik, 2, 3)), name = "trafficLightsAuxiliaryActor")

  // The PedestrianTrafficLightActor that handles the two pedestrian passages located on the auxiliary road of the model.
  val pedestrianCrossingActor: ActorRef = context.actorOf(Props(new PedestrianTrafficLightActor(ik, 4)), name = "pedestrianCrossingActor")

  // The two MainRoadCarDetectorActors that handle the detection sensors on each side of the main road of the model.
  val mainCarDetectorActor1: ActorRef = context.actorOf(Props(new MainRoadCarDetectorActor(ik,5)), name = "mainCarDetectorActor1")
  val mainCarDetectorActor2: ActorRef = context.actorOf(Props(new MainRoadCarDetectorActor(ik,6)), name = "mainCarDetectorActor2")

  // The two AuxiliaryCarDetectorActors that handle the detection sensors on each side of the auxiliary road of the model.
  val secondaryCarDetectorActor1: ActorRef = context.actorOf(Props(new AuxiliaryCarDetectorActor(ik, 0)), name = "secondaryCarDetectorActor1")
  val secondaryCarDetectorActor2: ActorRef = context.actorOf(Props(new AuxiliaryCarDetectorActor(ik, 1)), name = "secondaryCarDetectorActor2")

  // The two PedestrianTouchActors that handle the touch sensors on each side of the auxiliary road of the model.
  val pedestrianTouchDetectorActor1: ActorRef = context.actorOf(Props(new PedestrianTouchActor(ik, 2)), name = "pedestrianTouchDetectorActor1")
  val pedestrianTouchDetectorActor2: ActorRef = context.actorOf(Props(new PedestrianTouchActor(ik, 3)), name = "pedestrianTouchDetectorActor2")

  // Timeout for the asked messages to some actors.
  implicit val timeout = Timeout(5 seconds)

  // A DateTime variable that checks if the last time the two traffic lights have switched was not enough long ago.
  var timeOfLastAuxiliaryGreenLight:DateTime = _
  // A DateTime variable that checks if the last time the two pedestrians crossroads have been green was not enough long ago.
  var timeOfLastPedestrianGreenLight:DateTime = _

  // The minimum time since the last time the auxiliary trafficlights has been switched on.
  val differenceBetweenGreenAuxiliaryTrafficLights = 180

  // The minimum time since the last time the pedestrians could cross the road.
  val differenceBetweenGreenPedestrianCrossRoads = 40

  // The minimum time for the system to accept another similar message.
  val minimumTimeSinceLastRequest = 20

  // The last time the OpenAuxiliary Message has been received.
  var lastOpenAuxiliaryMessage:DateTime = _

  //the last time the Pedestrian Message has been received.
  var lastPedestrianMessage:DateTime = _


  override def receive: Receive = {

    /*
     * Initializes all the sub-actors.
     */
    case Initialize() =>
      val initTrafficLightsMainActor = trafficLightsMainActor ? Initialize()
      val initTrafficLightsAuxiliaryActor = trafficLightsAuxiliaryActor ? Initialize()
      val initPedestrianCrossingActor = pedestrianCrossingActor ? Initialize()
      val initMainRoadCarDetectorActor1 = mainCarDetectorActor1 ? Initialize()
      val initMainRoadCarDetectorActor2 = mainCarDetectorActor2 ? Initialize()
      val initSecondaryCarDetectorActor1 = secondaryCarDetectorActor1 ? Initialize()
      val initSecondaryCarDetectorActor2 = secondaryCarDetectorActor2 ? Initialize()
      val initPedestrianTouchDetectorActor1 = pedestrianTouchDetectorActor1 ? Initialize()
      val initPedestrianTouchDetectorActor2 = pedestrianTouchDetectorActor2 ? Initialize()
      timeOfLastAuxiliaryGreenLight = new DateTime()
      timeOfLastPedestrianGreenLight = new DateTime()
      lastOpenAuxiliaryMessage = new DateTime()
      lastPedestrianMessage = new DateTime()

      val results = for {
        resultInitTrafficLightsMainActor <- initTrafficLightsMainActor
        resultInitTrafficLightsAuxiliaryActor <- initTrafficLightsAuxiliaryActor
        resultInitPedestrianCrossingActor <- initPedestrianCrossingActor
        resultInitMainRoadCarDetectorActor1 <- initMainRoadCarDetectorActor1
        resultInitMainRoadCarDetectorActor2 <- initMainRoadCarDetectorActor2
        resultInitSecondaryCarDetectorActor1 <- initSecondaryCarDetectorActor1
        resultInitSecondaryCarDetectorActor2 <- initSecondaryCarDetectorActor2
        resultInitPedestrianTouchDetectorActor1 <- initPedestrianTouchDetectorActor1
        resultInitPedestrianTouchDetectorActor2 <- initPedestrianTouchDetectorActor2
      } yield (resultInitTrafficLightsMainActor,
        resultInitTrafficLightsAuxiliaryActor,
        resultInitPedestrianCrossingActor,
        resultInitMainRoadCarDetectorActor1,
        resultInitMainRoadCarDetectorActor2,
        resultInitSecondaryCarDetectorActor1,
        resultInitSecondaryCarDetectorActor2,
        resultInitPedestrianTouchDetectorActor1,
        resultInitPedestrianTouchDetectorActor2)

      results pipeTo sender


    /*
     * Sets a basic situation on the model. Opens the main road and closes the auxiliary one.
     */
    case Start() =>
      trafficLightsMainActor ! SetGreen()
      trafficLightsAuxiliaryActor ! SetRed()
      pedestrianCrossingActor ! SetOn()
      mainCarDetectorActor1 ! Start()
      mainCarDetectorActor2 ! Start()
      secondaryCarDetectorActor1 ! Start()
      secondaryCarDetectorActor2 ! Start()
      pedestrianTouchDetectorActor1 ! Start()
      pedestrianTouchDetectorActor2 ! Start()


    /*
     * When the detection sensors located on the auxiliary road are triggered, closes the main road and opens the auxiliary one.
     */
    case OpenAuxiliary() =>
      // Checks if the request has been sent few minutes ago. If yes, does not add it to the message queue.
      if (lastOpenAuxiliaryMessage.getSecondOfDay + minimumTimeSinceLastRequest < new DateTime().getSecondOfDay) {
        lastOpenAuxiliaryMessage = DateTime.now()

        //If there is no car on the main road, no need to wait the entire usual waiting time.
        val requestMainCarDetector1 = mainCarDetectorActor1 ? MainCarDetected()
        val requestMainCarDetector2 = mainCarDetectorActor2 ? MainCarDetected()

        val results:Future[(Any,Any)] = for {
          resultMainCarDetection1 <- requestMainCarDetector1
          resultMainCarDetection2 <- requestMainCarDetector2
        } yield (resultMainCarDetection1,resultMainCarDetection2)

        Await.result(results,5000 millis) match {
          case (false,false) => openAuxiliary()
          case _ =>
            Thread sleep switchTheLights(timeOfLastAuxiliaryGreenLight, differenceBetweenGreenAuxiliaryTrafficLights)
            openAuxiliary()
        }
      }

    /*
     * When the touch sensors are triggered, closes the auxiliary road if it is opened, opens the main road, and let the pedestrians pass.
     */
    case Pedestrian() =>
      // Checks if the request has been sent few minutes ago. If yes, does not add it to the message queue.
      if (lastPedestrianMessage.getSecondOfDay + minimumTimeSinceLastRequest < new DateTime().getSecondOfDay) {
        lastPedestrianMessage = DateTime.now()
        Thread sleep switchTheLights(timeOfLastPedestrianGreenLight, differenceBetweenGreenPedestrianCrossRoads)
        timeOfLastPedestrianGreenLight = DateTime.now()
        trafficLightsAuxiliaryActor ! SetRed()
        Thread sleep 4000
        pedestrianCrossingActor ! SetOn()
        trafficLightsMainActor ! SetGreen()
      }

    /*
     * Stops all the sub-actors.
     */
    case Stop() =>
      val stopTrafficLightsMainActor = trafficLightsMainActor ? Stop()
      val stopTrafficLightsAuxiliaryActor = trafficLightsAuxiliaryActor ? Stop()
      val stopPedestrianCrossingActor = pedestrianCrossingActor ? Stop()
      val stopMainRoadCarDetectorActor1 = mainCarDetectorActor1 ? Stop()
      val stopMainRoadCarDetectorActor2 = mainCarDetectorActor2 ? Stop()
      val stopSecondaryCarDetectorActor1 = secondaryCarDetectorActor1 ? Stop()
      val stopSecondaryCarDetectorActor2 = secondaryCarDetectorActor2 ? Stop()
      val stopPedestrianTouchDetectorActor1 = pedestrianTouchDetectorActor1 ? Stop()
      val stopPedestrianTouchDetectorActor2 = pedestrianTouchDetectorActor2 ? Stop()

      val results = for {
        resultStopTrafficLightsMainActor <- stopTrafficLightsMainActor
        resultStopTrafficLightsAuxiliaryActor <- stopTrafficLightsAuxiliaryActor
        resultStopPedestrianCrossingActor <- stopPedestrianCrossingActor
        resultStopMainRoadCarDetectorActor1 <- stopMainRoadCarDetectorActor1
        resultStopMainRoadCarDetectorActor2 <- stopMainRoadCarDetectorActor2
        resultStopSecondaryCarDetectorActor1 <- stopSecondaryCarDetectorActor1
        resultStopSecondaryCarDetectorActor2 <- stopSecondaryCarDetectorActor2
        resultStopPedestrianTouchDetectorActor1 <- stopPedestrianTouchDetectorActor1
        resultStopPedestrianTouchDetectorActor2 <- stopPedestrianTouchDetectorActor2
      } yield (resultStopTrafficLightsMainActor,
        resultStopTrafficLightsAuxiliaryActor,
        resultStopPedestrianCrossingActor,
        resultStopMainRoadCarDetectorActor1,
        resultStopMainRoadCarDetectorActor2,
        resultStopSecondaryCarDetectorActor1,
        resultStopSecondaryCarDetectorActor2,
        resultStopPedestrianTouchDetectorActor1,
        resultStopPedestrianTouchDetectorActor2)

      results pipeTo sender
  }

  /*
   * Handles the switch between the main road and the auxiliary one.
   */
  def openAuxiliary(): Unit = {
    pedestrianCrossingActor ! SetOff()
    Thread sleep 4000
    trafficLightsMainActor ! SetRed()
    trafficLightsAuxiliaryActor ! SetGreen()
    Thread sleep 20000
    trafficLightsMainActor ! SetGreen()
    trafficLightsAuxiliaryActor ! SetRed()
    timeOfLastAuxiliaryGreenLight = DateTime.now()
    Thread sleep 2000
    pedestrianCrossingActor ! SetOn()
  }

  /** Return the time the system has to wait before switching the lights on.
    *
    * @param timeOfLast The last time the lights were switched on.
    * @param differenceBetween The minimum difference between two switches.
    * @return The remaining time before the lights switch on, in milliseconds.
    */
  def switchTheLights(timeOfLast:DateTime, differenceBetween:Long): Long = {
    val currentDifference:Integer = Seconds.secondsBetween(timeOfLastAuxiliaryGreenLight,new DateTime).getSeconds
    if (currentDifference > differenceBetween) {
      0
    } else {
      (differenceBetween - currentDifference)*1000
    }
  }

}