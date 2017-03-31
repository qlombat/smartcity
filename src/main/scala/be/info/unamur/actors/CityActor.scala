package be.info.unamur.actors

import akka.actor.{Actor, ActorRef, Props}
import com.phidgets.InterfaceKitPhidget

/**
  * @author jeremyduchesne
  * @author Noé Picard
  */
class CityActor extends Actor with SharedProperties {

  val crossroadsActor: ActorRef = context.actorOf(Props(new CrossroadsActor(ik)), name = "crossroadsActor")

  val ik = new InterfaceKitPhidget()

  override def receive: Receive = {
    case "init" =>
      ik openAny()
      ik waitForAttachment()

      crossroadsActor ! Init()

    case "close" =>
      ik close()

  }
}
