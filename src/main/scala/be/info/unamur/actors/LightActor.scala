package be.info.unamur.actors

import akka.actor.Actor

/**
  * @author jeremyduchesne
  */
class LightActor extends TrafficLightsActor{

  var port:Int = _

  override def receive: Receive = {
    case Init(port:Int) => this.port = port
    case SwitchOn => {
      ik.setOutputState(port, true)
    }
    case SwitchOff => {
      ik.setOutputState(port, false)
    }
    case Blink => {
      ik.setOutputState(port, true)
      Thread.sleep(500)
      ik.setOutputState(port, false)
    }
  }

}
