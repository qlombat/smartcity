package be.info.unamur

import akka.actor.{ActorSystem, Props}
import be.info.unamur.actors.CityActor
import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport

class MainServlet extends ScalatraServlet with ScalateSupport {
  before() {
    contentType = "text/html"
  }

  get("/") {
    layoutTemplate("/WEB-INF/templates/views/index.ssp")
  }

  get("/init_actors") {
    // Initialize the Actor System
    val system = ActorSystem("SmartCity")

    val cityActor = system.actorOf(Props[CityActor], name = "cityActor")

    cityActor ! "init"
  }
}