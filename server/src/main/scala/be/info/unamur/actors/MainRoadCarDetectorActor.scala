package be.info.unamur.actors

import java.sql.Timestamp

import akka.actor.{Actor, Cancellable}
import be.info.unamur.messages._
import be.info.unamur.persistence.entities.{Sensor, Zone}
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event.{SensorChangeEvent, SensorChangeListener}
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/** This actor handles the behaviour of the main road detection sensor. If it detects a car, the CrossroadsActor will handle the LEDs.
  *
  * @author jeremyduchesne
  * @author Quentin Lombat
  *
  */
class MainRoadCarDetectorActor(ik: InterfaceKitPhidget, index: Int) extends Actor {
  val logger: Logger = LoggerFactory.getLogger(getClass)
  var sensorChangeListenerTrafficJam: SensorChangeListener = _
  var sensorChangeListenerDB: SensorChangeListener = _
  var initialValue: Int = 0
  var alreadyDetected = false
  var trafficJamDetected = false
  var cancellableScheduler: Cancellable = _

  var lastInitialValueUpdate: Long = 0


  val system = akka.actor.ActorSystem("system")


  override def receive: Receive = {

    case Initialize() =>

      initialValue = ik.getSensorValue(index)

      this.sensorChangeListenerDB = new SensorChangeListener {
        override def sensorChanged(sensorChangeEvent: SensorChangeEvent): Unit = {
          if (index.equals(sensorChangeEvent.getIndex)) {
            val value = ik.getSensorValue(sensorChangeEvent.getIndex)
            val detected = Math.abs(initialValue - value) > MainRoadCarDetectorActor.valueCarDetection
            if (!alreadyDetected && detected) {
              logger.debug("Car detected on " + context.self.path.name)
              alreadyDetected = true
              Sensor.create(context.self.path.name, 1, ik.getSensorValue(index), new Timestamp(System.currentTimeMillis()))

              //Add traffic jam detector
              cancellableScheduler = system.scheduler.scheduleOnce(Duration.apply(MainRoadCarDetectorActor.timeToDetectTrafficJam, "seconds")) {
                logger.debug("Traffic Jam detected on " + context.self.path.name)
                trafficJamDetected = true
                context.self.path.name match {
                  case "mainCarDetectorActor1" => Zone.create("MainRoadWest", "Main road West", opened = false, new Timestamp(System.currentTimeMillis()))
                  case "mainCarDetectorActor2" => Zone.create("MainRoadEast", "Main road East", opened = false, new Timestamp(System.currentTimeMillis()))
                }
              }

            } else if (alreadyDetected && !detected) {
              logger.debug("Car is away on " + context.self.path.name)
              alreadyDetected = false

              //Remove traffic Jam detector
              if (!cancellableScheduler.isCancelled) {
                logger.debug("Cancel traffic jam detector on " + context.self.path.name)
                cancellableScheduler.cancel()
              }

              if (trafficJamDetected) {
                context.self.path.name match {
                  case "mainCarDetectorActor1" => Zone.create("MainRoadWest", "Main road West", opened = true, new Timestamp(System.currentTimeMillis()))
                  case "mainCarDetectorActor2" => Zone.create("MainRoadEast", "Main road East", opened = true, new Timestamp(System.currentTimeMillis()))
                }
                trafficJamDetected = false
              }

            } else if (!alreadyDetected && !detected) {
              if (Math.abs(System.currentTimeMillis() - lastInitialValueUpdate) > 1000) {
                logger.debug("Luminosity change on " + context.self.path.name + " " + value)
                initialValue = value
                lastInitialValueUpdate = System.currentTimeMillis()
              }
            }
          }
        }

        ik setSensorChangeTrigger(index, MainRoadCarDetectorActor.trigger)
        sender ! Initialized()
      }
    /*
     * Adds the listener to the interface kit.
     */
    case Start() => ik addSensorChangeListener sensorChangeListenerDB

    /*
     * Checks if there is a car on the street.
     */
    case MainCarDetected() =>
      if (Math.abs(initialValue - ik.getSensorValue(index)) > MainRoadCarDetectorActor.valueCarDetection) {
        sender ! true
      } else sender ! false

    /*
     * Removes the listener from the interface kit.
     */
    case Stop() =>
      ik removeSensorChangeListener sensorChangeListenerDB
      sender ! Stopped()
  }

}

/** Companion object for the AuxiliaryCarDetectorActor
  *
  * @author Justin SIRJACQUES
  */
object MainRoadCarDetectorActor {
  /* Constants */

  //Under this value, there isn't any car. Upper, a car is present
  val valueCarDetection: Int = 300

  //Trigger for the listener
  val trigger: Int = 50

  //Minimum time between two car detections. (seconds)
  val timeBetweenCarDectection: Int = 3

  //Time to detect a traffic Jam (seconds)
  val timeToDetectTrafficJam = 5
}