package be.info.unamur.persistence.entities

import java.sql.Timestamp

import scalikejdbc._


/** Zone entity that represents the 'zones' table in the database.
  *
  * @author Noé Picard
  */
case class Zone(id: Long,
                name: String,
                opened: Boolean,
                createdAt: Timestamp)

/**
  * Zone DAO to query the 'zones' table in the database.
  *
  * @author Noé Picard
  */
object Zone extends SQLSyntaxSupport[Zone] {
  override val tableName   = "zones"
  override val columns     = Seq("id", "name", "opened", "created_at")
  override val autoSession = AutoSession
  val zone: QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[Zone], Zone] = Zone.syntax("z")


  def apply(z: ResultName[Zone])(rs: WrappedResultSet): Zone = new Zone(
    id = rs.long(z.id),
    name = rs.string(z.name),
    opened = rs.boolean(z.opened),
    createdAt = rs.timestamp(z.createdAt)
  )

  def find(id: Long)(implicit session: DBSession = autoSession): Option[Zone] = {
    withSQL {
      select.from(Zone as zone).where.eq(zone.id, id)
    }.map(Zone(zone.resultName)).first.apply()
  }

  def findByName(name: String)(implicit session: DBSession = autoSession): Option[Zone] = {
    withSQL {
      select.from(Zone as zone).where.eq(zone.name, name)
    }.map(Zone(zone.resultName)).first.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Zone] = {
    withSQL(select.from(Zone as zone)).map(Zone(zone.resultName)).list.apply()
  }

  def findAllLast()(implicit session: DBSession = autoSession): List[Zone] = {
    sql"""
    SELECT ${zone.result.*}
    FROM ${Zone.as(zone)}
    WHERE ${zone.createdAt} = (
        SELECT MAX(zone_tmp.created_at)
        FROM zones zone_tmp
        WHERE zone_tmp.name = ${zone.name}
    )""".map(Zone(zone.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls"count(1)").from(Zone as zone)).map(rs => rs.long(1)).single.apply().get
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Zone] = {
    withSQL {
      select.from(Zone as zone).where.append(sqls"$where")
    }.map(Zone(zone.resultName)).list.apply()
  }

  def findAllDesc()(implicit session: DBSession = autoSession): List[Zone] = {
    withSQL {
      select.from(Zone as zone).orderBy(zone.createdAt).desc
    }.map(Zone(zone.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls"count(1)").from(Zone as zone).where.append(sqls"$where")
    }.map(_.long(1)).single.apply().get
  }

  def create(name: String,
             opened: Boolean,
             createdAt: Timestamp)(implicit session: DBSession = autoSession): Zone = {
    val generatedKey = withSQL {
      insert.into(Zone).columns(
        column.name,
        column.opened,
        column.createdAt
      ).values(
        name,
        opened,
        createdAt)
    }.updateAndReturnGeneratedKey.apply()

    Zone(
      id = generatedKey,
      name = name,
      opened = opened,
      createdAt = createdAt)
  }

  def save(z: Zone)(implicit session: DBSession = autoSession): Zone = {
    withSQL {
      update(Zone as zone).set(
        zone.id -> z.id,
        zone.name -> z.name,
        zone.opened -> z.opened,
        zone.createdAt -> z.createdAt
      )
    }.update().apply()
    z
  }

  def destroy(z: Zone)(implicit session: DBSession = autoSession): Unit = {
    withSQL {
      delete.from(Zone).where.eq(column.id, z.id)
    }.update.apply()
  }
}
