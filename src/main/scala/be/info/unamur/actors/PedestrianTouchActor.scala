package be.info.unamur.actors

import akka.actor.Actor
import be.info.unamur.messages._
import be.info.unamur.utils.FailureSpreadingActor
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event.{SensorChangeEvent, SensorChangeListener}


/**
  * @author Quentin Lombat
  */
class PedestrianTouchActor(ik: InterfaceKitPhidget, index: Int) extends FailureSpreadingActor {

  var sensorChangeListener: SensorChangeListener = _

  override def receive: Receive = {
    case Initialize() =>
      // Necessary sender reference for the listener below
      val senderRef = sender

      this.sensorChangeListener = new SensorChangeListener {
        override def sensorChanged(sensorChangeEvent: SensorChangeEvent): Unit = {
          if (index.equals(sensorChangeEvent.getIndex) && ik.getSensorValue(sensorChangeEvent.getIndex) > 500)
            senderRef ! Pedestrian()
        }
      }
      ik setSensorChangeTrigger(index, 500)

      sender ! Initialized()

    case Start() =>
      ik addSensorChangeListener this.sensorChangeListener

    case Stop() =>
      ik removeSensorChangeListener this.sensorChangeListener
      sender ! Stopped()
  }

}
