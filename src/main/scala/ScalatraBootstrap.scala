import javax.servlet.ServletContext

import _root_.akka.actor.ActorSystem
import be.info.unamur.MainServlet
import org.scalatra._

class ScalatraBootstrap extends LifeCycle {

  // Initialize the Actor system here to do it just once and pass it to the servlet that need it
  val system = ActorSystem("SmartCity")


  override def init(context: ServletContext) {
    // Mount servlets
    context.mount(new MainServlet(system), "/*")
  }

  override def destroy(context: ServletContext) {
    system.terminate()
  }
}

