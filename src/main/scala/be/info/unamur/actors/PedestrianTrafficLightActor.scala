package be.info.unamur.actors

import be.info.unamur.messages._
import be.info.unamur.utils.FailureSpreadingActor
import be.info.unamur.utils.Times._
import com.phidgets.InterfaceKitPhidget

/**
  * This actor handles the behaviour of the pedestrian traffic lights. When the Yellow LED is opened, they can pass. Otherwise, they can not.
  *
  * @author Justin Sirjacques
  * @author Noé Picard
  */
class PedestrianTrafficLightActor(ik: InterfaceKitPhidget, yellowPin: Int) extends FailureSpreadingActor {

  val lightYellowPin: Int = yellowPin

  override def receive: Receive = {

    /**
      * Initializes a basic situation. Opens the LED.
      */
    case Initialize() =>
      ik setOutputState(lightYellowPin, true)
      sender ! Initialized()

    /**
      * Switch the LED on.
      */
    case SetOn() =>
      ik setOutputState(lightYellowPin, true)

    /**
      * Switch the LED off.
      */
    case SetOff() =>
      ik setOutputState(lightYellowPin, false)

    /**
      * Makes blinking 3 times the LED and stops it.
    */
    case Stop() =>
      3 times {
        ik setOutputState(lightYellowPin, true)
        Thread sleep 500
        ik setOutputState(lightYellowPin, false)
        Thread sleep 500
      }
      sender ! Stopped()
  }

}
