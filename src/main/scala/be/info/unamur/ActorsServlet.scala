package be.info.unamur

import javax.servlet.ServletConfig

import akka.actor.{ActorRef, ActorSystem}
import com.phidgets.RFIDPhidget
import com.phidgets.event.{TagGainEvent, TagGainListener}
import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport

/**
  * Created by Noé Picard on 3/23/17.
  *
  * Main servlet to pilot actors.
  */
class ActorsServlet(system: ActorSystem, servoMotorActor: ActorRef) extends ScalatraServlet with
  ScalateSupport {

  val rfid = new RFIDPhidget()

  rfid addTagGainListener new TagGainListener {
    override def tagGained(tagGainEvent: TagGainEvent): Unit = {
      servoMotorActor ! "OPEN_THE_BARRIER"
    }
  }

  override def init(config: ServletConfig): Unit = {
    super.init(config)

    rfid openAny()
    rfid waitForAttachment()
    rfid setAntennaOn true
  }
}
