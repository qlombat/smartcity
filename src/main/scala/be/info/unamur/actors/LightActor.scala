package be.info.unamur.actors

import akka.actor.ActorSystem

/**
  * @author jeremyduchesne
  */
class LightActor(system: ActorSystem) extends TrafficLightsActor(system){

  var port:Int = _

  override def receive: Receive = {
    case Init(port:Int) => {
      this.port = port
      ik.setOutputState(port, true)
    }
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
