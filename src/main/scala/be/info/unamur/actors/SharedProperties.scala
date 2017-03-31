package be.info.unamur.actors

/**
  * @author Noé Picard
  *
  * Shared properties (like case classes for pattern matching on messages) for all actors.
  */
trait SharedProperties
case class Init(port: Int*) extends SharedProperties
case class SwitchOn() extends SharedProperties
case class SwitchOff() extends SharedProperties
case class Blink() extends SharedProperties
case class Close() extends SharedProperties
case class Pedestrian(b: Boolean) extends SharedProperties
case object OpenBarrier extends SharedProperties
