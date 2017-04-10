package be.info.unamur.actors

import be.info.unamur.messages._
import be.info.unamur.utils.FailureSpreadingActor
import be.info.unamur.utils.Times._
import com.phidgets.InterfaceKitPhidget

/**
  * @author jeremyduchesne
  * @author Noé Picard
  */
class TrafficLightsActor(ik: InterfaceKitPhidget, redPin: Int, greenPin: Int) extends FailureSpreadingActor {

  val lightRedPin  : Int = redPin
  val lightGreenPin: Int = greenPin


  override def receive: Receive = {
    case Initialize() =>
      ik setOutputState(lightRedPin, true)
      ik setOutputState(lightGreenPin, true)
      sender ! Initialized()

    case SetGreen() =>
      ik setOutputState(lightRedPin, false)
      ik setOutputState(lightGreenPin, true)

    case SetRed() =>
      ik setOutputState(lightRedPin, true)
      ik setOutputState(lightGreenPin, false)

    /* Makes the lights blinking 3 times and stops */
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
