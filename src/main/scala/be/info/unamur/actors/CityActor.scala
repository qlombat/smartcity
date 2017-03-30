package be.info.unamur.actors

import akka.actor.{Actor, ActorSystem, Props}
import com.phidgets.{InterfaceKitPhidget}

/**
  * @author jeremyduchesne
  */
class CityActor extends Actor {

  sealed trait Message
  case class Init(port:Int*) extends Message
  case class SwitchOn() extends Message
  case class SwitchOff() extends Message
  case class Blink() extends Message
  case class Close() extends Message

  val ik = new InterfaceKitPhidget()

  val system = ActorSystem("PoeteSystem")
  val crossroadsActor = system.actorOf(Props[CrossroadsActor], name = "crossroadsActor")

  override def receive: Receive = {
    case Init => {
      ik openAny()
      ik waitForAttachment()

      crossroadsActor ! Init(0,1,2,3,4,5,6,7)

    }
    case Close => {
      ik close()
    }
  }
}
