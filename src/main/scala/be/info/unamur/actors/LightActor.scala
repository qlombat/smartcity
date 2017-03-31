package be.info.unamur.actors

import akka.actor.Actor
import com.phidgets.InterfaceKitPhidget

/**
  * @author jeremyduchesne
  */
class LightActor(ik: InterfaceKitPhidget) extends Actor with SharedProperties{

  var port: Int = _

  override def receive: Receive = {
    case Init(port: Int) =>
      this.port = port
      ik.setOutputState(port, true)

    case SwitchOn() =>
      ik.setOutputState(port, true)

    case SwitchOff() =>
      ik.setOutputState(port, false)

    case Blink =>
      ik.setOutputState(port, true)
      Thread.sleep(500)
      ik.setOutputState(port, false)

  }

}
