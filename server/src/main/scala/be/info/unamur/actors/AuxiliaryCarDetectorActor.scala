package be.info.unamur.actors

import java.sql.Timestamp

import akka.actor.Actor
import be.info.unamur.messages._
import be.info.unamur.persistence.entities.Sensor
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event.{SensorChangeEvent, SensorChangeListener}
import org.slf4j.{Logger, LoggerFactory}


/** This actor handles the behaviour of the detection sensor. If it detects a car, the CrossroadsActor will handle the LEDs.
  *
  * @author Quentin Lombat
  * @author Justin SIRJACQUES
  */
class AuxiliaryCarDetectorActor(ik: InterfaceKitPhidget, index: Int) extends Actor {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  var sensorChangeListener: SensorChangeListener = _

  var listen = true
  var listenerAdded = false

  override def receive: Receive = {

    /*
     * Initializes the listener.
     */
    case Initialize() =>
      // Necessary sender reference for the listener below
      sensorChangeListener = new SensorChangeListener {
        override def sensorChanged(sensorChangeEvent: SensorChangeEvent): Unit = {
          if (index.equals(sensorChangeEvent.getIndex)) {
            val value = ik.getSensorValue(sensorChangeEvent.getIndex)
            if (value < AuxiliaryCarDetectorActor.valueCarDetection) {
              logger.debug("Car detected on " + context.self.path.name)
              Sensor.create(context.self.path.name, 1, sensorChangeEvent.getValue, new Timestamp(System.currentTimeMillis()))
              if (listen) {
                logger.debug(context.self.path.name + " would open auxiliary traffic light")
                context.parent ! OpenAuxiliary()
              }
            } else {
              logger.debug("Car is away on " + context.self.path.name + " " + value )
              Sensor.create(context.self.path.name, 0, sensorChangeEvent.getValue, new Timestamp(System.currentTimeMillis()))
            }
          }
        }
      }
      sender ! Initialized()

    /*
     * Adds the listener to the interface kit.
     * Removes the listener for database updates from the interface kit.
     */
    case Start() =>
      ik setSensorChangeTrigger(index, AuxiliaryCarDetectorActor.trigger)
      if(!listenerAdded){
        listenerAdded = true
        ik addSensorChangeListener sensorChangeListener
      }
      self ! StartListen
    /*
     * Removes all listener from the interface kit.
     */
    case Stop() =>
      listenerAdded = false
      ik removeSensorChangeListener sensorChangeListener
      sender ! Stopped()

    /*
     * Start to listen this actor
     */
    case StartListen() =>
      ik setSensorChangeTrigger(index, AuxiliaryCarDetectorActor.trigger)
      logger.debug("Start to listen " + context.self.path.name)
      listen = true

    /*
     * Stop to listen this actor
     */
    case StopListen() =>
      ik setSensorChangeTrigger(index, AuxiliaryCarDetectorActor.trigger)
      logger.debug("Stop to listen " + context.self.path.name)
      listen = false
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
}
