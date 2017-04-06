package be.info.unamur.actors

/** Messages case classes for pattern matching.
  *
  * @author Noé Picard
  */
sealed trait Message
case class Init() extends Message
case class Start() extends Message
case class SetGreen() extends Message
case class SetRed() extends Message
case class SetOn() extends Message
case class SetOff() extends Message
case class SwitchOn() extends Message
case class SwitchOff() extends Message
case class OpenAuxiliary() extends Message
case class Blink() extends Message
case class Close() extends Message
case class Pedestrian() extends Message
case class OpenBarrier() extends Message
case class Stop() extends Message
case class StopFinished() extends Message
