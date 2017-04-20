package be.info.unamur.actors

import be.info.unamur.messages._
import be.info.unamur.utils.FailureSpreadingActor
import be.info.unamur.utils.Times._
import com.phidgets.InterfaceKitPhidget

/** This actor handles the behaviour of the traffic lights.
  *
  * @author jeremyduchesne
  * @author Noé Picard
  */
class TrafficLightsActor(ik: InterfaceKitPhidget, redPin: Int, greenPin: Int) extends FailureSpreadingActor {

  // The green LED.
  val lightRedPin  : Int = redPin
  // The red LED.
  val lightGreenPin: Int = greenPin

  override def receive: Receive = {

    /*
     * Opens the two LEDs
     */
    case Initialize() =>
      ik setOutputState(lightRedPin, true)
      ik setOutputState(lightGreenPin, true)
      sender ! Initialized()

    /*
     * Opens the green LED and closes the red one.
     */
    case SetGreen() =>
      ik setOutputState(lightRedPin, false)
      ik setOutputState(lightGreenPin, true)

    /*
     * Opens the red LED and closes the green one.
     */
    case SetRed() =>
      ik setOutputState(lightRedPin, true)
      ik setOutputState(lightGreenPin, false)

    /*
     * Makes blinking 3 times the two LEDs and stops it.
     */
    case Stop() =>
      3 times {
        ik setOutputState(lightRedPin, true)
        ik setOutputState(lightGreenPin, true)
        Thread sleep 500
        ik setOutputState(lightRedPin, false)
        ik setOutputState(lightGreenPin, false)
        Thread sleep 500
      }
      sender ! Stopped()
  }


}
