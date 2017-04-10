package be.info.unamur.utils

/**
  * @author Noé Picard
  */
class Times(n: Int) {
  def times(block: => Unit): Unit = (1 to n) foreach { i => block }
}

object Times {
  implicit def times(n: Int): Times = new Times(n)
}