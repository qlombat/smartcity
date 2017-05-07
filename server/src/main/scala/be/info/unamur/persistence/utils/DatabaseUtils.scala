package be.info.unamur.persistence.utils

import org.slf4j.{Logger, LoggerFactory}
import scalikejdbc.config._


/** Utils methods for the database.
  *
  * @author Noé Picard
  */
trait DatabaseUtils {
  val logger: Logger = LoggerFactory.getLogger(getClass)


  /**
    * Configures the database with the configuration in the 'application.conf' file.
    */
  def configureDatabase(): Unit = {
    DBs.setupAll()
    logger.info("Database configured")
  }

  /**
    * Closes the database.
    */
  def closeDatabase(): Unit = {
    DBs.closeAll()
    logger.info("Database closed")
  }
}
