package be.info.unamur.actors

import java.sql.Timestamp
import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorRef, ActorSystem, Cancellable, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import be.info.unamur.messages._
import com.phidgets.InterfaceKitPhidget
import org.joda.time.{DateTime, Seconds}
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.language.postfixOps

/** This actor handles the behaviour of the crossroads. It controls all the sub-actors needed by the crossroads.
  *
  * @author jeremyduchesne
  * @author Quentin Lombat
  * @author Justin Sirjacques
  */
class CrossroadsActor(ik: InterfaceKitPhidget) extends Actor {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  // The TrafficLightsActor that handles the two traffic lights of the main road of the model.
  val trafficLightsMainActor: ActorRef = context.actorOf(Props(new TrafficLightsActor(ik, 0, 1)).withDispatcher("application-dispatcher"), name = "trafficLightsMainActor")
  // The TrafficLightsActor that handles the two traffic lights of the auxiliary road of the model.
  val trafficLightsAuxiliaryActor: ActorRef = context.actorOf(Props(new TrafficLightsActor(ik, 2, 3)).withDispatcher("application-dispatcher"), name = "trafficLightsAuxiliaryActor")

  // The PedestrianTrafficLightActor that handles the two pedestrian passages located on the auxiliary road of the model.
  val pedestrianCrossingActor: ActorRef = context.actorOf(Props(new PedestrianTrafficLightActor(ik, 4)).withDispatcher("application-dispatcher"), name = "pedestrianCrossingActor")

  // The two MainRoadCarDetectorActors that handle the detection sensors on each side of the main road of the model.
  val mainCarDetectorActorWest: ActorRef = context.actorOf(Props(new MainRoadCarDetectorActor(ik, 0)).withDispatcher("application-dispatcher"), name = "mainCarDetectorActorWest")
  val mainCarDetectorActorEast: ActorRef = context.actorOf(Props(new MainRoadCarDetectorActor(ik, 1)).withDispatcher("application-dispatcher"), name = "mainCarDetectorActorEast")

  // The two AuxiliaryCarDetectorActors that handle the detection sensors on each side of the auxiliary road of the model.
  val auxiliaryCarDetectorActorSouth: ActorRef = context.actorOf(Props(new AuxiliaryCarDetectorActor(ik, 2)).withDispatcher("application-dispatcher"), name = "auxiliaryCarDetectorActorSouth")
  val auxiliaryCarDetectorActorNorth: ActorRef = context.actorOf(Props(new AuxiliaryCarDetectorActor(ik, 3)).withDispatcher("application-dispatcher"), name = "auxiliaryCarDetectorActorNorth")

  // Timeout for the asked messages to some actors.
  implicit val timeout = Timeout(5 seconds)

  // A DateTime variable that checks if the last time the two traffic lights have switched was not enough long ago.
  var timeOfLastAuxiliaryGreenLight: DateTime = _


  // The last time the OpenAuxiliary Message has been received.
  var lastOpenAuxiliaryMessage: DateTime = _

  var auxiliaryScheduler: Cancellable = _

  implicit val executionContext: ExecutionContextExecutor = context.dispatcher

  override def receive: Receive = {

    /*
     * Initializes all the sub-actors.
     */
    case Initialize() =>
      val initTrafficLightsMainActor = trafficLightsMainActor ? Initialize()
      val initTrafficLightsAuxiliaryActor = trafficLightsAuxiliaryActor ? Initialize()
      val initPedestrianCrossingActor = pedestrianCrossingActor ? Initialize()
      val initMainRoadCarDetectorActorWest = mainCarDetectorActorWest ? Initialize()
      val initMainRoadCarDetectorActorEast = mainCarDetectorActorEast ? Initialize()
      val initAuxiliaryCarDetectorActorSouth = auxiliaryCarDetectorActorSouth ? Initialize()
      val initAuxiliaryCarDetectorActorNorth = auxiliaryCarDetectorActorNorth ? Initialize()
      timeOfLastAuxiliaryGreenLight = new DateTime()
      lastOpenAuxiliaryMessage = new DateTime()
      auxiliaryScheduler = context.system.scheduler.scheduleOnce(
        Duration.apply(CrossroadsActor.differenceBetweenGreenAuxiliary, "seconds"),
        self,
        OpenAuxiliary())

      val results = for {
        resultInitTrafficLightsMainActor <- initTrafficLightsMainActor
        resultInitTrafficLightsAuxiliaryActor <- initTrafficLightsAuxiliaryActor
        resultInitPedestrianCrossingActor <- initPedestrianCrossingActor
        resultInitMainRoadCarDetectorActorWest <- initMainRoadCarDetectorActorWest
        resultInitMainRoadCarDetectorActorEast <- initMainRoadCarDetectorActorEast
        resultInitAuxiliaryCarDetectorActorSouth <- initAuxiliaryCarDetectorActorSouth
        resultInitAuxiliaryCarDetectorActorNorth <- initAuxiliaryCarDetectorActorNorth
      } yield (resultInitTrafficLightsMainActor,
        resultInitTrafficLightsAuxiliaryActor,
        resultInitPedestrianCrossingActor,
        resultInitMainRoadCarDetectorActorWest,
        resultInitMainRoadCarDetectorActorEast,
        resultInitAuxiliaryCarDetectorActorSouth,
        resultInitAuxiliaryCarDetectorActorNorth
      )

      results pipeTo sender


    /*
     * Sets a basic situation on the model. Opens the main road and closes the auxiliary one.
     */
    case Start() =>
      trafficLightsMainActor ! SetGreen()
      trafficLightsAuxiliaryActor ! SetRed()
      pedestrianCrossingActor ! SetOn()
      mainCarDetectorActorWest ! Start()
      mainCarDetectorActorEast ! Start()
      auxiliaryCarDetectorActorSouth ! Start()
      auxiliaryCarDetectorActorNorth ! Start()

    /*
   * When the detection sensors located on the auxiliary road are triggered, closes the main road and opens the auxiliary one.
   */
    case OpenAuxiliary() =>
      logger.debug("OpenAuxiliary of " + context.self.path.name)
      auxiliaryScheduler.cancel()
      /* Once a carDetected in Auxiliary, turn off listener to not receive too much request "OpenAuxiliary". These listener
         will be turned on again when the red light will be on. */
      auxiliaryCarDetectorActorSouth ! StopListen()
      auxiliaryCarDetectorActorNorth ! StopListen()

      //If there is no car on the main road, no need to wait the entire usual waiting time.
      val requestMainCarDetector1 = mainCarDetectorActorWest ? MainCarDetected()
      val requestMainCarDetector2 = mainCarDetectorActorEast ? MainCarDetected()

      val results: Future[(Any, Any)] = for {
        resultMainCarDetection1 <- requestMainCarDetector1
        resultMainCarDetection2 <- requestMainCarDetector2
      } yield (resultMainCarDetection1, resultMainCarDetection2)

      Await.result(results, (CrossroadsActor.waitingTimeForFuture * 1000) millis) match {
        // It there is not traffic jam, Auxiliary just waits the regulatory time
        case (false, false) =>
          if (timeOfLastAuxiliaryGreenLight.getSecondOfDay + CrossroadsActor.differenceBetweenGreenAuxiliaryTrafficLights < new DateTime().getSecondOfDay) {
            openAuxiliary()
          } else {
            Thread sleep switchTheLights(timeOfLastAuxiliaryGreenLight, CrossroadsActor.differenceBetweenGreenAuxiliaryTrafficLights)
            openAuxiliary()
          }
        // It there is traffic jam, Auxiliary waits more time to let the MainRoad progress.
        case (true, _) | (_, true) =>
          if (timeOfLastAuxiliaryGreenLight.getSecondOfDay + CrossroadsActor.differenceBetweenGreenAuxiliaryTrafficLights < new DateTime().getSecondOfDay) {
            logger.debug("Traffic jam detected. Wait to open auxiliary of " + context.self.path.name)
            Thread sleep (CrossroadsActor.waitingTimeWhenTrafficJam * 1000)
            openAuxiliary()
          } else {
            logger.debug("Wait few seconds to open auxiliary of " + context.self.path.name)
            Thread sleep switchTheLights(timeOfLastAuxiliaryGreenLight, CrossroadsActor.differenceBetweenGreenAuxiliaryTrafficLights)
            openAuxiliary()
          }
      }

    /*
     * Stops all the sub-actors.
     */
    case Stop() =>
      val stopTrafficLightsMainActor = trafficLightsMainActor ? Stop()
      val stopTrafficLightsAuxiliaryActor = trafficLightsAuxiliaryActor ? Stop()
      val stopPedestrianCrossingActor = pedestrianCrossingActor ? Stop()
      val stopMainRoadCarDetectorActorWest = mainCarDetectorActorWest ? Stop()
      val stopMainRoadCarDetectorActorEast = mainCarDetectorActorEast ? Stop()
      val stopAuxiliaryCarDetectorActorSouth = auxiliaryCarDetectorActorSouth ? Stop()
      val stopAuxiliaryCarDetectorActorNorth = auxiliaryCarDetectorActorNorth ? Stop()

      val results = for {
        resultStopTrafficLightsMainActor <- stopTrafficLightsMainActor
        resultStopTrafficLightsAuxiliaryActor <- stopTrafficLightsAuxiliaryActor
        resultStopPedestrianCrossingActor <- stopPedestrianCrossingActor
        resultStopMainRoadCarDetectorActorWest <- stopMainRoadCarDetectorActorWest
        resultStopMainRoadCarDetectorActorEast <- stopMainRoadCarDetectorActorEast
        resultStopAuxiliaryCarDetectorActorSouth <- stopAuxiliaryCarDetectorActorSouth
        resultStopAuxiliaryCarDetectorActorNorth <- stopAuxiliaryCarDetectorActorNorth
      } yield (resultStopTrafficLightsMainActor,
        resultStopTrafficLightsAuxiliaryActor,
        resultStopPedestrianCrossingActor,
        resultStopMainRoadCarDetectorActorWest,
        resultStopMainRoadCarDetectorActorEast,
        resultStopAuxiliaryCarDetectorActorSouth,
        resultStopAuxiliaryCarDetectorActorNorth
      )

      results pipeTo sender
  }

  /*
   * Handles the switch between the main road and the auxiliary one.
   */
  def openAuxiliary(): Unit = {

    //stop message of these actors
    auxiliaryCarDetectorActorSouth ! StopListen()
    auxiliaryCarDetectorActorNorth ! StopListen()

    logger.debug("Turn pedestrian leds off - " + context.self.path.name)
    pedestrianCrossingActor ! SetOff()
    Thread sleep CrossroadsActor.pedestrianCrossingTime * 1000
    logger.debug("Put main road leds to red - " + context.self.path.name)
    trafficLightsMainActor ! SetRed()
    Thread sleep CrossroadsActor.carCrossingTime * 1000
    logger.debug("Put auxiliary road leds to green - " + context.self.path.name)
    trafficLightsAuxiliaryActor ! SetGreen()
    Thread sleep CrossroadsActor.auxiliaryGreenLightTime * 1000
    logger.debug("Put auxiliary road leds to red - " + context.self.path.name)
    trafficLightsAuxiliaryActor ! SetRed()
    Thread sleep CrossroadsActor.carCrossingTime * 1000
    logger.debug("Put main road leds to green - " + context.self.path.name)
    trafficLightsMainActor ! SetGreen()
    logger.debug("Turn pedestrian leds on - " + context.self.path.name)
    pedestrianCrossingActor ! SetOn()

    timeOfLastAuxiliaryGreenLight = DateTime.now()

    //start message of these actors
    auxiliaryCarDetectorActorSouth ! StartListen()
    auxiliaryCarDetectorActorNorth ! StartListen()

    auxiliaryScheduler = context.system.scheduler.scheduleOnce(
      Duration.apply(CrossroadsActor.differenceBetweenGreenAuxiliary, "seconds"),
      self,
      OpenAuxiliary())

  }

  /** Return the time the system has to wait before switching the lights on.
    *
    * @param timeOfLast        The last time the lights were switched on.
    * @param differenceBetween The minimum difference between two switches.
    * @return The remaining time before the lights switch on, in milliseconds.
    */
  def switchTheLights(timeOfLast: DateTime, differenceBetween: Long): Long = {
    val currentDifference: Integer = Seconds.secondsBetween(timeOfLastAuxiliaryGreenLight, new DateTime).getSeconds
    if (currentDifference > differenceBetween) {
      0
    } else {
      (differenceBetween - currentDifference) * 1000
    }
  }
}

/** Companion object for the CrossroadsActor
  *
  * @author Justin SIRJACQUES
  */
object CrossroadsActor {
  /* Constants */

  // The minimum time since the last time the auxiliary trafficlights has been switched on. (seconds)
  val differenceBetweenGreenAuxiliaryTrafficLights = 20

  // The minimum time since the last time the pedestrians could cross the road. (seconds)
  val differenceBetweenGreenPedestrianCrossRoads = 15

  // The minimum time for the system to accept another similar message. (seconds)
  val minimumTimeSinceLastRequest = 10

  // The time auxiliaryStreet has to wait more if there is traffic jam in main street. (seconds)
  val waitingTimeWhenTrafficJam = 5

  // The waiting time for Future results. (seconds)
  val waitingTimeForFuture = 5

  // The time that pedestrians need to cross the road. (seconds)
  val pedestrianCrossingTime = 3

  // The time that cars need to cross the crossroad. (seconds)
  val carCrossingTime = 2

  // The time that green light stay green for auxiliary. (seconds)
  val auxiliaryGreenLightTime = 10

  val differenceBetweenGreenAuxiliary = 60
}