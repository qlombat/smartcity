package be.info.unamur.actors

import akka.actor.Actor

/**
  * @author jeremyduchesne
  */
class CityActor extends Actor {

  sealed trait Message

  case class Init(port:Int) extends Message
  case class SwitchOn() extends Message
  case class SwitchOff() extends Message
  case class Blink() extends Message

  override def receive: Receive = {
    case Init(_) =>
  }
}
