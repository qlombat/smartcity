package be.info.unamur.actors

import akka.actor.{Actor, ActorSystem, Props}
import com.phidgets.InterfaceKitPhidget

/**
  * @author jeremyduchesne
  */
class CityActor(system: ActorSystem) extends Actor {

  sealed trait Message
  case class Init(port:Int*) extends Message
  case class SwitchOn() extends Message
  case class SwitchOff() extends Message
  case class Blink() extends Message
  case class Close() extends Message
  case class Pedestrian(b:Boolean) extends Message

  val ik = new InterfaceKitPhidget()

  val crossroadsActor = system.actorOf(Props(new CrossroadsActor(system)), name = "crossroadsActor")

  override def receive: Receive = {
    case "Init" => {
      ik openAny()
      ik waitForAttachment()

      crossroadsActor ! Init

    }
    case "Close" => {
      ik close()
    }
  }
}
