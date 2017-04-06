package be.info.unamur.actors

import akka.actor.Actor
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event.{SensorChangeEvent, SensorChangeListener}


/**
  * @author Quentin Lombat
  */
class PedestrianTouchActor(ik: InterfaceKitPhidget, index: Int) extends Actor {

  var sensorChangeListener: SensorChangeListener = _

  override def receive: Receive = {
    case Init() =>
      //Necessary sender reference for the listener under
      val senderRef = sender

      this.sensorChangeListener = new SensorChangeListener {
        override def sensorChanged(sensorChangeEvent: SensorChangeEvent): Unit = {
          if (index.equals(sensorChangeEvent.getIndex) && ik.getSensorValue(sensorChangeEvent.getIndex) > 500)
            senderRef ! Pedestrian()
        }
      }
      ik.setSensorChangeTrigger(index, 500)

    case Start() =>
      ik.addSensorChangeListener(this.sensorChangeListener)

    case Stop() =>
      ik.removeSensorChangeListener(this.sensorChangeListener)
      sender ! StopFinished()
  }

}
