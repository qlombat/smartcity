package be.info.unamur.actors

import java.sql.Timestamp

import akka.actor.Actor
import be.info.unamur.messages._
import be.info.unamur.persistence.entities.Sensor
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event.{SensorChangeEvent, SensorChangeListener}


/** This actor handles the behaviour of the main road detection sensor. If it detects a car, the CrossroadsActor will handle the LEDs.
  *
  * @author jeremyduchesne
  */
class MainRoadCarDetectorActor(ik: InterfaceKitPhidget, index: Int) extends Actor {

  var sensorChangeListenerDB: SensorChangeListener = _
  var lastDBUpdate          : Long                 = 0

  override def receive: Receive = {

    case Initialize() =>

      this.sensorChangeListenerDB = new SensorChangeListener {
        override def sensorChanged(sensorChangeEvent: SensorChangeEvent): Unit = {
          if (index.equals(sensorChangeEvent.getIndex)) {
            if ((ik.getSensorValue(sensorChangeEvent.getIndex) > MainRoadCarDetectorActor.valueCarDetection) && (System.currentTimeMillis() - lastDBUpdate > MainRoadCarDetectorActor.timeBetweenCarDectection * 1000)) {
              Sensor.create(context.self.path.name, 1, ik.getSensorValue(index), new Timestamp(System.currentTimeMillis()))
              lastDBUpdate = System.currentTimeMillis()
            }
          }
        }
      }

      ik setSensorChangeTrigger(index, MainRoadCarDetectorActor.trigger)

      sender ! Initialized()

    /*
     * Adds the listener to the interface kit.
     */
    case Start() =>
      ik addSensorChangeListener sensorChangeListenerDB

    /*
     * Checks if there is a car on the street.
     */
    case MainCarDetected() =>
      if (ik.getSensorValue(index) > MainRoadCarDetectorActor.valueCarDetection) {
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
  val valueCarDetection: Int = 600

  //Trigger for the listener
  val trigger: Int = 300

  //Minimum time between two car detections. (seconds)
  val timeBetweenCarDectection: Int = 3
}