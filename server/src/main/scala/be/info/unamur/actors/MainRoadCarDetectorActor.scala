package be.info.unamur.actors

import be.info.unamur.messages._
import be.info.unamur.utils.FailureSpreadingActor
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event.{SensorChangeEvent, SensorChangeListener}

/** This actor handles the behaviour of the main road detection sensor. If it detects a car, the CrossroadsActor will handle the LEDs.
  *
  * @author jeremyduchesne
  */
class MainRoadCarDetectorActor(ik: InterfaceKitPhidget, index: Int) extends FailureSpreadingActor {

  var sensorChangeListener: SensorChangeListener = _

  override def receive: Receive = {

    /*
     * Initializes the listener.
     */
    case Initialize() =>
      // Necessary sender reference for the listener below
      val senderRef = sender

      this.sensorChangeListener = new SensorChangeListener {
        override def sensorChanged(sensorChangeEvent: SensorChangeEvent): Unit = {
          if (index.equals(sensorChangeEvent.getIndex) && ik.getSensorValue(sensorChangeEvent.getIndex) > MainRoadCarDetectorActor.valueCarDetection)
            // Not used currently
            senderRef ! OpenMainRoad()
        }
      }
      ik setSensorChangeTrigger(index, MainRoadCarDetectorActor.trigger)

      sender ! Initialized()

    /*
     * Adds the listener to the interface kit.
     */
    case Start() =>
      ik addSensorChangeListener this.sensorChangeListener

    /*
     * Checks if there is a car on the street.
     */
    case MainCarDetected() =>
      if (ik.getSensorValue(index) < 400) {
        sender ! true
      } else sender ! false

    /*
     * Removes the listener from the interface kit.
     */
    case Stop() =>
      ik removeSensorChangeListener this.sensorChangeListener
      sender ! Stopped()
  }
}

/** Companion object for the AuxiliaryCarDetectorActor
  *
  * @author Justin SIRJACQUES
  */
object MainRoadCarDetectorActor {
  /* Constants */
  val valueCarDetection: Int = 750
  val trigger: Int = 100
}