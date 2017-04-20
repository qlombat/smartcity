package be.info.unamur.utils

/** Provides a simple way to execute some code a certain number of times
  *
  * @author Noé Picard
  */
class Times(n: Int) {
  def times(block: => Unit): Unit = (1 to n) foreach { _ => block }
}

object Times {
  implicit def times(n: Int): Times = new Times(n)
}