package be.info.unamur.actors

import be.info.unamur.messages._
import be.info.unamur.utils.FailureSpreadingActor
import be.info.unamur.utils.Times._
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event.{SensorChangeEvent, SensorChangeListener}

//TODO : Test this actor
/** Controls the three LEDs that represents the public lighting, connected to the interface kit.
  *
  * @author Noé Picard
  */
class PublicLightingActor(ik: InterfaceKitPhidget, index: Int, level1Pin: Int, level2Pin: Int, level3Pin: Int) extends FailureSpreadingActor {

  var lightSensorChangeListener: SensorChangeListener = _


  override def receive: Receive = {
    case Initialize() =>
      this.lightSensorChangeListener = new SensorChangeListener {
        override def sensorChanged(sensorChangeEvent: SensorChangeEvent): Unit = {
          if (index.equals(sensorChangeEvent.getIndex))
            ik.getSensorValue(sensorChangeEvent.getIndex) match {
              case sv if sv < 250 =>
                allPinDown()
              case sv if sv < 500 =>
                ik setOutputState(level1Pin, true)
                ik setOutputState(level2Pin, false)
                ik setOutputState(level3Pin, false)
              case sv if sv < 750 =>
                ik setOutputState(level1Pin, true)
                ik setOutputState(level2Pin, true)
                ik setOutputState(level3Pin, false)
              case sv if sv < 1000 =>
                allPinUp()
            }
        }
      }
      //TODO : Changer the trigger value if necessary
      ik setSensorChangeTrigger(index, PublicLightingActor.triggerValue)

      sender ! Initialized()

    case Start() =>
      3 times {
        allPinUp()
        Thread sleep PublicLightingActor.blinkingSpeed / 2
        allPinDown()
        Thread sleep PublicLightingActor.blinkingSpeed / 2
      }

      ik addSensorChangeListener this.lightSensorChangeListener


    case Stop() =>
      ik removeSensorChangeListener this.lightSensorChangeListener
      sender ! Stopped()
  }

  def allPinUp(): Unit = {
    ik setOutputState(level1Pin, true)
    ik setOutputState(level2Pin, true)
    ik setOutputState(level3Pin, true)
  }

  def allPinDown(): Unit = {
    ik setOutputState(level1Pin, false)
    ik setOutputState(level2Pin, false)
    ik setOutputState(level3Pin, false)
  }

}


/**
  * Companion object for the public lighting actor
  */
object PublicLightingActor {
  /* Constants */
  val triggerValue : Int = 100
  val blinkingSpeed: Int = 1000 //in milliseconds
}
