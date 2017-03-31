import javax.servlet.ServletContext

import _root_.akka.actor.ActorSystem
import be.info.unamur.MainServlet
import org.scalatra._

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    // Mount servlets
    context.mount(new MainServlet(), "/*")
  }
}

