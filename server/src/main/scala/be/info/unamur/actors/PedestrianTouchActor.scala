package be.info.unamur.actors

import be.info.unamur.messages._
import be.info.unamur.utils.FailureSpreadingActor
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event.{SensorChangeEvent, SensorChangeListener}


/** This actor handles the behaviour of the touch sensors. If a pedestrian pushes it, the CrossroadsActor will handle the LEDs.
  *
  * @author Quentin Lombat
  */
class PedestrianTouchActor(ik: InterfaceKitPhidget, index: Int) extends FailureSpreadingActor {

  var sensorChangeListener: SensorChangeListener = _

  override def receive: Receive = {

    /*
     * Initializes the listener.
     */
    case Initialize() =>
      this.sensorChangeListener = new SensorChangeListener {
        override def sensorChanged(sensorChangeEvent: SensorChangeEvent): Unit = {
          if (index.equals(sensorChangeEvent.getIndex) && ik.getSensorValue(sensorChangeEvent.getIndex) > 500)
            context.parent ! Pedestrian()
        }
      }
      ik setSensorChangeTrigger(index, 500)

      sender ! Initialized()

    /*
     * Adds the listener to the interface kit.
     */
    case Start() =>
      ik addSensorChangeListener this.sensorChangeListener

    /*
     * Removes the listener from the interface kit.
     */
    case Stop() =>
      ik removeSensorChangeListener this.sensorChangeListener
      sender ! Stopped()
  }

}
