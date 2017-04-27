package be.info.unamur.actors

import java.sql.Timestamp

import be.info.unamur.messages._
import be.info.unamur.persistence.entities.Sensor
import be.info.unamur.utils.FailureSpreadingActor
import be.info.unamur.utils.Times._
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event.{SensorChangeEvent, SensorChangeListener}

/** Controls the three LEDs that represent the public lighting, connected to the interface kit.
  *
  * @author Noé Picard
  */
class PublicLightingActor(ik: InterfaceKitPhidget, index: Int, level1Pin: Int, level2Pin: Int, level3Pin: Int) extends FailureSpreadingActor {

  var lightSensorChangeListener  : SensorChangeListener = _
  var lightSensorChangeListenerDB: SensorChangeListener = _

  override def receive: Receive = {

    /*
     * Initializes the listener that will handle the changes of light level, and change the intensity of the public lightning according to it.
     */
    case Initialize() =>
      this.lightSensorChangeListener = new SensorChangeListener {
        override def sensorChanged(sensorChangeEvent: SensorChangeEvent): Unit = {
          if (index.equals(sensorChangeEvent.getIndex))
            setPin(ik.getSensorValue(sensorChangeEvent.getIndex))
        }
      }

      this.lightSensorChangeListenerDB = new SensorChangeListener {
        override def sensorChanged(sensorChangeEvent: SensorChangeEvent): Unit = {
          if (index.equals(sensorChangeEvent.getIndex))
            Sensor.create("light", ik.getSensorValue(index), ik.getSensorValue(index), new Timestamp(System.currentTimeMillis()))
        }
      }


      allPinUp()
      ik setSensorChangeTrigger(index, PublicLightingActor.triggerValue)

      sender ! Initialized()


    /*
     * Makes blinking the 3 LEDs 3 times at the launch of the system and adds the listener to the interface kit.
     */
    case Start() =>
      setPin(ik.getSensorValue(index))
      ik addSensorChangeListener this.lightSensorChangeListener
      ik addSensorChangeListener this.lightSensorChangeListenerDB


    /*
     * Stops the LEDs and remove the listener.
     */
    case Stop() =>
      3 times {
        allPinUp()
        Thread sleep PublicLightingActor.blinkingSpeed / 2
        allPinDown()
        Thread sleep PublicLightingActor.blinkingSpeed / 2
      }
      ik removeSensorChangeListener this.lightSensorChangeListener
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

  def setPin(value: Int): Unit = {
    value match {
      case sv if sv < PublicLightingActor.Level0Value =>
        allPinUp()
      case sv if sv < PublicLightingActor.Level1Value =>
        ik setOutputState(level1Pin, true)
        ik setOutputState(level2Pin, true)
        ik setOutputState(level3Pin, false)
      case sv if sv < PublicLightingActor.Level2Value =>
        ik setOutputState(level1Pin, true)
        ik setOutputState(level2Pin, false)
        ik setOutputState(level3Pin, false)
      case sv if sv < PublicLightingActor.Level3Value =>
        allPinDown()
    }
  }
}

/** Companion object for the public lighting actor
  *
  * @author Noé Picard
  */
object PublicLightingActor {
  /* Constants */
  val triggerValue : Int = 20
  val blinkingSpeed: Int = 1000 //in milliseconds
  val Level0Value: Int = 30
  val Level1Value: Int = 190
  val Level2Value: Int = 370
  val Level3Value: Int = 1000
}
