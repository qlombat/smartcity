package be.info.unamur.actors

import be.info.unamur.messages._
import be.info.unamur.utils.FailureSpreadingActor
import be.info.unamur.utils.Times._
import com.phidgets.InterfaceKitPhidget

/**
  * @author Justin Sirjacques
  * @author Noé Picard
  */
class PedestrianTrafficLightActor(ik: InterfaceKitPhidget, yellowPin: Int) extends FailureSpreadingActor {

  val lightYellowPin: Int = yellowPin


  override def receive: Receive = {
    case Initialize() =>
      ik setOutputState(lightYellowPin, true)
      sender ! Initialized()

    case SetOn() =>
      ik setOutputState(lightYellowPin, true)

    case SetOff() =>
      ik setOutputState(lightYellowPin, false)

    /* Makes the lights blinking 3 times and stops */
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
