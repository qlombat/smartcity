package be.info.unamur.persistence.entities

import java.sql.Timestamp

import scalikejdbc._

/**
  * Created by Justin SIRJACQUES on 24-04-17.
  */
case class RfidTag(id: Long,
                   name: String,
                   tag: String,
                   createdAt: Timestamp)

object RfidTag extends SQLSyntaxSupport[RfidTag] {
  override val tableName = "rfidtags"
  override val columns = Seq("id", "name", "tag", "created_at")
  override val autoSession = AutoSession
  val rfidtag: QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[RfidTag], RfidTag] = RfidTag.syntax("r")


  def apply(s: ResultName[RfidTag])(rs: WrappedResultSet): RfidTag = new RfidTag(
    id = rs.long(s.id),
    name = rs.string(s.name),
    tag = rs.string(s.tag),
    createdAt = rs.timestamp(s.createdAt)
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
      insert.into(Sensor).columns(
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

  def save(s: RfidTag)(implicit session: DBSession = autoSession): RfidTag = {
    withSQL {
      update(RfidTag as rfidtag).set(
        rfidtag.id -> s.id,
        rfidtag.name -> s.name,
        rfidtag.tag -> s.tag,
        rfidtag.createdAt -> s.createdAt
      )
    }.update().apply()
    s
  }

  def destroy(s: RfidTag)(implicit session: DBSession = autoSession): Unit = {
    withSQL {
      delete.from(RfidTag).where.eq(column.id, s.id)
    }.update.apply()
  }
}
