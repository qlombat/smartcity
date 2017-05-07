package be.info.unamur.persistence.entities

import java.sql.Timestamp

import scalikejdbc._


/** RfidTag entity that represents the 'rfidtag' table in the database.
  *
  * @author Justin Sirjacques
  */
case class RfidTag(id: Long,
                   name: String,
                   tag: String,
                   createdAt: Timestamp)

/**
  * RfidTag DAO to query the 'rfidtag' table in the database.
  *
  * @author Justin Sirjacques
  */
object RfidTag extends SQLSyntaxSupport[RfidTag] {
  override val tableName   = "rfidtag"
  override val columns     = Seq("id", "name", "tag", "created_at")
  override val autoSession = AutoSession
  val rfidtag: QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[RfidTag], RfidTag] = RfidTag.syntax("r")


  def apply(r: ResultName[RfidTag])(rs: WrappedResultSet): RfidTag = new RfidTag(
    id = rs.long(r.id),
    name = rs.string(r.name),
    tag = rs.string(r.tag),
    createdAt = rs.timestamp(r.createdAt)
  )

  def find(id: Long)(implicit session: DBSession = autoSession): Option[RfidTag] = {
    withSQL {
      select.from(RfidTag as rfidtag).where.eq(rfidtag.id, id)
    }.map(RfidTag(rfidtag.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[RfidTag] = {
    withSQL(select.from(RfidTag as rfidtag)).map(RfidTag(rfidtag.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls"count(1)").from(RfidTag as rfidtag)).map(rs => rs.long(1)).single.apply().get
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[RfidTag] = {
    withSQL {
      select.from(RfidTag as rfidtag).where.append(sqls"$where")
    }.map(RfidTag(rfidtag.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls"count(1)").from(RfidTag as rfidtag).where.append(sqls"$where")
    }.map(_.long(1)).single.apply().get
  }

  def create(name: String,
             tag: String,
             createdAt: Timestamp)(implicit session: DBSession = autoSession): RfidTag = {
    val generatedKey = withSQL {
      insert.into(RfidTag).columns(
        column.name,
        column.tag,
        column.createdAt
      ).values(
        name,
        tag,
        createdAt)
    }.updateAndReturnGeneratedKey.apply()

    RfidTag(
      id = generatedKey,
      name = name,
      tag = tag,
      createdAt = createdAt)
  }

  def save(r: RfidTag)(implicit session: DBSession = autoSession): RfidTag = {
    withSQL {
      update(RfidTag as rfidtag).set(
        rfidtag.id -> r.id,
        rfidtag.name -> r.name,
        rfidtag.tag -> r.tag,
        rfidtag.createdAt -> r.createdAt
      )
    }.update().apply()
    r
  }

  def destroy(r: RfidTag)(implicit session: DBSession = autoSession): Unit = {
    withSQL {
      delete.from(RfidTag).where.eq(column.id, r.id)
    }.update.apply()
  }
}
