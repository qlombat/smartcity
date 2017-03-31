package be.info.unamur.actors

import akka.actor.{Actor, ActorRef, Props}
import com.phidgets.RFIDPhidget
import com.phidgets.event.{TagGainEvent, TagGainListener}

/**
  * @author Noé Picard
  */
class ParkingActor extends Actor with SharedProperties {
  val rfid = new RFIDPhidget()

  val barrierActor: ActorRef = context.actorOf(Props(new BarrierActor(rfid)), name = "barrierActor")

  rfid addTagGainListener new TagGainListener {
    override def tagGained(tagGainEvent: TagGainEvent): Unit = {
      barrierActor ! OpenBarrier
    }
  }

  override def receive: Receive = {
    case Init() =>
      rfid openAny()
      rfid waitForAttachment()
      rfid setAntennaOn true

      barrierActor ! Init()
  }
}
