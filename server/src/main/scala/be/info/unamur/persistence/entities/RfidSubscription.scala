package be.info.unamur.persistence.entities

import java.sql.Timestamp

import scalikejdbc._


/** RfidSubscription entity that represents the 'rfidsubscription' table in the database.
  *
  * @author Justin Sirjacques
  */
case class RfidSubscription(id: Long,
                            firstName: String,
                            lastName: String,
                            tag: String,
                            createdAt: Timestamp)

/**
  * RfidTag DAO to query the 'rfidsubscription' table in the database.
  *
  * @author Justin Sirjacques
  */
object RfidSubscription extends SQLSyntaxSupport[RfidSubscription] {
  override val tableName = "rfid_subscription"
  override val columns = Seq("id", "first_name", "last_name", "tag", "created_at")
  override val autoSession = AutoSession
  val rfidsubscription: QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[RfidSubscription], RfidSubscription] = RfidSubscription.syntax("r")


  def apply(r: ResultName[RfidSubscription])(rs: WrappedResultSet): RfidSubscription = new RfidSubscription(
    id = rs.long(r.id),
    firstName = rs.string(r.firstName),
    lastName = rs.string(r.lastName),
    tag = rs.string(r.tag),
    createdAt = rs.timestamp(r.createdAt)
  )

  def find(id: Long)(implicit session: DBSession = autoSession): Option[RfidSubscription] = {
    withSQL {
      select.from(RfidSubscription as rfidsubscription).where.eq(rfidsubscription.id, id)
    }.map(RfidSubscription(rfidsubscription.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[RfidSubscription] = {
    withSQL(select.from(RfidSubscription as rfidsubscription)).map(RfidSubscription(rfidsubscription.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls"count(1)").from(RfidSubscription as rfidsubscription)).map(rs => rs.long(1)).single.apply().get
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[RfidSubscription] = {
    withSQL {
      select.from(RfidSubscription as rfidsubscription).where.append(sqls"$where")
    }.map(RfidSubscription(rfidsubscription.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls"count(1)").from(RfidSubscription as rfidsubscription).where.append(sqls"$where")
    }.map(_.long(1)).single.apply().get
  }

  def create(firstName: String,
             lastName: String,
             tag: String,
             createdAt: Timestamp)(implicit session: DBSession = autoSession): RfidSubscription = {
    val generatedKey = withSQL {
      insert.into(RfidSubscription).columns(
        column.firstName,
        column.lastName,
        column.tag,
        column.createdAt
      ).values(
        firstName,
        lastName,
        tag,
        createdAt)
    }.updateAndReturnGeneratedKey.apply()

    RfidSubscription(
      id = generatedKey,
      firstName = firstName,
      lastName = lastName,
      tag = tag,
      createdAt = createdAt)
  }

  def save(r: RfidSubscription)(implicit session: DBSession = autoSession): RfidSubscription = {
    withSQL {
      update(RfidSubscription as rfidsubscription).set(
        rfidsubscription.id -> r.id,
        rfidsubscription.firstName -> r.firstName,
        rfidsubscription.lastName -> r.lastName,
        rfidsubscription.tag -> r.tag,
        rfidsubscription.createdAt -> r.createdAt
      )
    }.update().apply()
    r
  }

  def destroy(r: RfidSubscription)(implicit session: DBSession = autoSession): Unit = {
    withSQL {
      delete.from(RfidSubscription).where.eq(column.id, r.id)
    }.update.apply()
  }
}
