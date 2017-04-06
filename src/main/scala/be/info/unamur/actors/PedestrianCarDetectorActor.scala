package be.info.unamur.actors

import akka.actor.Actor
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event.{SensorChangeEvent, SensorChangeListener}


/**
  * @author Quentin Lombat
  */
class PedestrianCarDetectorActor(ik: InterfaceKitPhidget, index :Int) extends Actor with Messages{

    var sensorChangeListener: SensorChangeListener = _

    override def receive: Receive = {
        case Init() =>
            this.sensorChangeListener = new SensorChangeListener {
                override def sensorChanged(sensorChangeEvent: SensorChangeEvent): Unit = {
                    if(index.equals(sensorChangeEvent.getIndex) && ik.getSensorValue(sensorChangeEvent.getIndex) > 500){
                            sender ! SecondaryCarComing()
                    }
                }
            }
            ik.setSensorChangeTrigger(index, 10)
        case Start() => ik.addSensorChangeListener(this.sensorChangeListener)
        case Stop() => ik.removeSensorChangeListener(this.sensorChangeListener)
    }

}
