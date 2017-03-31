package be.info.unamur.actors

import akka.actor.{Actor, ActorRef, Props}
import com.phidgets.InterfaceKitPhidget

/**
  * @author jeremyduchesne
  * @author Noé Picard
  */
class CityActor extends Actor with SharedProperties{

  val ik = new InterfaceKitPhidget()

  val crossroadsActor: ActorRef = context.actorOf(Props(new CrossroadsActor(ik)), name = "crossroadsActor")
  val parkingActor: ActorRef = context.actorOf(Props(new ParkingActor()), name = "parkingActor")

  override def receive: Receive = {
    case Init() =>
      ik openAny()
      ik waitForAttachment()

      crossroadsActor ! Init()
      parkingActor ! Init()

      Thread.sleep(2000)

      crossroadsActor ! Start()

      Thread.sleep(2000)

      crossroadsActor ! SecondaryCarComing()
    case Close() =>
      ik close()

  }
}
