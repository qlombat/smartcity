package be.info.unamur.actors

import akka.actor.{Actor, ActorRef, Props}
import com.phidgets.RFIDPhidget
import com.phidgets.event.{TagGainEvent, TagGainListener}

/** Implements the behavior of the parking. Uses the RFID tag reader to pilot the barrier.
  *
  * @author Noé Picard
  */
class ParkingActor extends Actor with Messages {
  val rfid = new RFIDPhidget()

  val barrierActor: ActorRef = context.actorOf(Props(new BarrierActor(rfid)), name = "barrierActor")

  /*
   * Sends the "open barrier" message when a RFID tag is read.
   */
  rfid addTagGainListener new TagGainListener {
    override def tagGained(tagGainEvent: TagGainEvent): Unit = {
      barrierActor ! OpenBarrier()
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
