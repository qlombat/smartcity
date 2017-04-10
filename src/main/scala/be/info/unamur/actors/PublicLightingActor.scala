package be.info.unamur.actors

import be.info.unamur.messages._
import be.info.unamur.utils.FailureSpreadingActor
import be.info.unamur.utils.Times._
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event.{SensorChangeEvent, SensorChangeListener}

//TODO : Test this actor
/**
  * Controls the three LEDs that represent the public lighting, connected to the interface kit.
  *
  * @author Noé Picard
  */
class PublicLightingActor(ik: InterfaceKitPhidget, index: Int, level1Pin: Int, level2Pin: Int, level3Pin: Int) extends FailureSpreadingActor {

  var lightSensorChangeListener: SensorChangeListener = _

  override def receive: Receive = {

    /**
      * Initializes the listener that will handle the changes of light level, and change the intensity of the public lightning according to it.
      */
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

    /**
      * Makes blinking the 3 LEDs 3 times at the launch of the system and adds the listener to the interface kit.
      */
    case Start() =>
      3 times {
        allPinUp()
        Thread sleep PublicLightingActor.blinkingSpeed / 2
        allPinDown()
        Thread sleep PublicLightingActor.blinkingSpeed / 2
      }

      ik addSensorChangeListener this.lightSensorChangeListener


    /**
      * Stops the LEDs and remove the listener.
      */
    case Stop() =>
      ik removeSensorChangeListener this.lightSensorChangeListener
      allPinDown()
      sender ! Stopped()
  }

  /**
    * Switches all the lights on.
    */
  def allPinUp(): Unit = {
    ik setOutputState(level1Pin, true)
    ik setOutputState(level2Pin, true)
    ik setOutputState(level3Pin, true)
  }

  /**
    * Switches all the lights off.
    */
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
