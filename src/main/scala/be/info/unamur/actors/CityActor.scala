package be.info.unamur.actors

import akka.actor.Actor
import com.phidgets.{InterfaceKitPhidget, RFIDPhidget}

/**
  * @author jeremyduchesne
  */
class CityActor extends Actor {

  val ik = new InterfaceKitPhidget()

  sealed trait Message

  case class Init(port:Int) extends Message
  case class SwitchOn() extends Message
  case class SwitchOff() extends Message
  case class Blink() extends Message
  case class Close() extends Message

  override def receive: Receive = {
    case Init => {
      ik openAny()

      ik waitForAttachment()
    }
    case Close => {
      ik clone()
    }
  }
}
