package be.info.unamur.actors

import java.sql.Timestamp

import akka.actor.Actor
import be.info.unamur.messages._
import be.info.unamur.persistence.entities.Sensor
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event.{SensorChangeEvent, SensorChangeListener}


/** This actor handles the behaviour of the detection sensor. If it detects a car, the CrossroadsActor will handle the LEDs.
  *
  * @author Quentin Lombat
  * @author Justin SIRJACQUES
  */
class AuxiliaryCarDetectorActor(ik: InterfaceKitPhidget, index: Int) extends Actor {

  var sensorChangeListener  : SensorChangeListener = _
  var sensorChangeListenerDB: SensorChangeListener = _
  var lastDBUpdate          : Long                 = 0

  override def receive: Receive = {

    /*
     * Initializes the listener.
     */
    case Initialize() =>
      // Necessary sender reference for the listener below

      sensorChangeListener = new SensorChangeListener {
        override def sensorChanged(sensorChangeEvent: SensorChangeEvent): Unit = {
          if (index.equals(sensorChangeEvent.getIndex) && ik.getSensorValue(sensorChangeEvent.getIndex) < AuxiliaryCarDetectorActor.valueCarDetection) {
            Sensor.create(context.self.path.name, 1, sensorChangeEvent.getValue, new Timestamp(System.currentTimeMillis()))
            context.parent ! OpenAuxiliary()
          }
        }
      }

      sensorChangeListenerDB = new SensorChangeListener {
        override def sensorChanged(sensorChangeEvent: SensorChangeEvent): Unit = {
          if (index.equals(sensorChangeEvent.getIndex) && (ik.getSensorValue(sensorChangeEvent.getIndex) < AuxiliaryCarDetectorActor.valueCarDetection) && (System.currentTimeMillis() - lastDBUpdate > AuxiliaryCarDetectorActor.timeBetweenCarDectection * 1000)) {
            Sensor.create(context.self.path.name, 1, sensorChangeEvent.getValue, new Timestamp(System.currentTimeMillis()))
            lastDBUpdate = System.currentTimeMillis()
          }
        }
      }

      ik setSensorChangeTrigger(index, AuxiliaryCarDetectorActor.trigger)

      sender ! Initialized()

    /*
     * Adds the listener to the interface kit.
     * Removes the listener for database updates from the interface kit.
     */
    case Start() =>
      ik addSensorChangeListener sensorChangeListener
      ik removeSensorChangeListener sensorChangeListenerDB
      if (ik.getSensorValue(index) < AuxiliaryCarDetectorActor.valueCarDetection) {
        sender ! OpenAuxiliary()
      }

    /*
     * Removes the listener from the interface kit.
     */
    case Stop() =>
      ik removeSensorChangeListener sensorChangeListener
      sender ! Stopped()

    /*
     * Adds the listener for database updates to the interface kit.
     */
    case GreenLigth() =>
      ik addSensorChangeListener sensorChangeListenerDB
      sender ! Stopped()
  }
}

/** Companion object for the AuxiliaryCarDetectorActor
  *
  * @author Justin SIRJACQUES
  */
object AuxiliaryCarDetectorActor {
  /* Constants */

  //Upper this value, there isn't any car. Under, a car is present
  val valueCarDetection: Int = 500

  //Trigger for the listeners
  val trigger: Int = 500

  //Minimum time between two car detections. (seconds)
  val timeBetweenCarDectection: Int = 3
}
