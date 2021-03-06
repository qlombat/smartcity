package be.info.unamur.messages

/** Messages case classes for pattern matching.
  *
  * @author Noé Picard
  */
sealed trait CityMessage
case class Initialize() extends CityMessage
case class Initialized() extends CityMessage
case class Start() extends CityMessage
case class SetGreen() extends CityMessage
case class SetRed() extends CityMessage
case class SetOn() extends CityMessage
case class SetOff() extends CityMessage
case class SwitchOn() extends CityMessage
case class SwitchOff() extends CityMessage
case class OpenAuxiliary() extends CityMessage
case class OpenMainRoad() extends CityMessage
case class Blink() extends CityMessage
case class Close() extends CityMessage
case class OpenBarrier() extends CityMessage
case class CloseBarrier() extends CityMessage
case class Stop() extends CityMessage
case class StopListen() extends CityMessage
case class StartListen() extends CityMessage
case class Stopped() extends CityMessage
case class MainCarDetected() extends CityMessage
case class Opened() extends CityMessage
case class Closed() extends CityMessage