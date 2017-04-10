package be.info.unamur.utils

import akka.actor.{Actor, Status}

/** Provides an automatic response back to the sender in case of an unexpected exception
  *
  * @author Noé Picard
  */
trait FailureSpreadingActor extends Actor {
  override def preRestart(reason: Throwable, message: Option[Any]) {
    super.preRestart(reason, message)
    sender ! Status.Failure(reason)
  }
}
