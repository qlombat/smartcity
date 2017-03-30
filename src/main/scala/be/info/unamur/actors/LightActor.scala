package be.info.unamur.actors

import akka.actor.Actor

/**
  * @author jeremyduchesne
  */
class LightActor extends CityActor{

  var port:Int = _

  override def receive: Receive = {
    case Init(port:Int) => this.port = port
    case SwitchOn => {

    }
    case SwitchOff => {

    }
    case Blink => {

    }
  }

}
