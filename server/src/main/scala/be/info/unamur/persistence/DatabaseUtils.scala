package be.info.unamur.persistence

import grizzled.slf4j.Logger
import scalikejdbc.config._


/**
  * @author Noé Picard
  */
trait DatabaseUtils {
  val logger: Logger = Logger[DatabaseUtils]


  def configureDatabase(): Unit = {
    DBs.setupAll()
    logger.info("Database configured")
  }

  def closeDatabase(): Unit = {
    DBs.closeAll()
    logger.info("Database closed")
  }
}
