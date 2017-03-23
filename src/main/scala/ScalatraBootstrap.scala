import javax.servlet.ServletContext

import _root_.akka.actor.{ActorSystem, Props}
import be.info.unamur.actors.ServoMotorActor
import be.info.unamur.{ActorsServlet, MainServlet}
import org.scalatra._

class ScalatraBootstrap extends LifeCycle {

  // Initialize the Actor System
  val system = ActorSystem()
  val servoMotorActor = system.actorOf(Props[ServoMotorActor])

  override def init(context: ServletContext) {
    // Mount servlets
    context.mount(new MainServlet, "/*")
    context.mount(new ActorsServlet(system, servoMotorActor), "/actors/*")
  }
}

