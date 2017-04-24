package be.info.unamur.actors

import java.sql.Timestamp

import akka.actor.{ActorRef, Props}
import be.info.unamur.messages._
import be.info.unamur.persistence.entities.Sensor
import be.info.unamur.utils.FailureSpreadingActor
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event.{SensorChangeEvent, SensorChangeListener}


/** This actor handles the behaviour of the detection sensor. If it detects a car, the CrossroadsActor will handle the LEDs.
  *
  * @author Quentin Lombat
  * @author Justin SIRJACQUES
  */
class AuxiliaryCarDetectorActor(ik: InterfaceKitPhidget, index: Int) extends FailureSpreadingActor {

  var sensorChangeListener: SensorChangeListener = _
  var sensorChangeListenerDB: SensorChangeListener = _

  override def receive: Receive = {

    /*
     * Initializes the listener.
     */
    case Initialize() =>
      // Necessary sender reference for the listener below

      this.sensorChangeListener = new SensorChangeListener {
        override def sensorChanged(sensorChangeEvent: SensorChangeEvent): Unit = {
          if (index.equals(sensorChangeEvent.getIndex) && ik.getSensorValue(sensorChangeEvent.getIndex) < AuxiliaryCarDetectorActor.valueCarDetection)
            context.parent ! OpenAuxiliary()
        }
      }

      this.sensorChangeListenerDB = new SensorChangeListener {
        override def sensorChanged(sensorChangeEvent: SensorChangeEvent): Unit = {
          if (index.equals(sensorChangeEvent.getIndex)) {
            ik.getSensorValue(sensorChangeEvent.getIndex) < AuxiliaryCarDetectorActor.valueCarDetection match {
              case true => Sensor.create(context.self.path.name, 1, ik.getSensorValue(index), new Timestamp(System.currentTimeMillis()))
              case false => Sensor.create(context.self.path.name, 0, ik.getSensorValue(index), new Timestamp(System.currentTimeMillis()))
            }
          }
        }
      }
      ik setSensorChangeTrigger(index, AuxiliaryCarDetectorActor.trigger)

      sender ! Initialized()

    /*
     * Adds the listener to the interface kit.
     */
    case Start() =>
      ik addSensorChangeListener this.sensorChangeListener
      if (ik.getSensorValue(index) < AuxiliaryCarDetectorActor.valueCarDetection) {
        sender ! OpenAuxiliary()
      }
      ik addSensorChangeListener this.sensorChangeListenerDB

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
object AuxiliaryCarDetectorActor {
  /* Constants */
  val valueCarDetection: Int = 500
  val trigger: Int = 500
  val dbTrigger: Int = 500
}
