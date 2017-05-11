package be.info.unamur.persistence.entities

import java.sql.Time

import scalikejdbc._


/**
  * @author Noé Picard
  */
case class BusSchedule(id: Long,
                       day: String,
                       openingTime: Time,
                       closingTime: Time)

object BusSchedule extends SQLSyntaxSupport[BusSchedule] {
  override val tableName   = "bus_schedule"
  override val columns     = Seq("id", "day", "opening_time", "closing_time")
  override val autoSession = AutoSession
  val busSchedule: QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[BusSchedule], BusSchedule] = BusSchedule.syntax("bs")

  def apply(bs: ResultName[BusSchedule])(bss: WrappedResultSet): BusSchedule = new BusSchedule(
    id = bss.long(bs.id),
    day = bss.string(bs.day),
    openingTime = bss.time(bs.openingTime),
    closingTime = bss.time(bs.closingTime)
  )

  def find(id: String)(implicit session: DBSession = autoSession): Option[BusSchedule] = {
    withSQL {
      select.from(BusSchedule as busSchedule).where.eq(busSchedule.id, id)
    }.map(BusSchedule(busSchedule.resultName)).first.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[BusSchedule] = {
    withSQL {
      select.from(BusSchedule as busSchedule).where.append(sqls"$where")
    }.map(BusSchedule(busSchedule.resultName)).list().apply()
  }

  def create(id: Long, day: String, openingTime: Time, closingTime: Time)(implicit session: DBSession = autoSession): BusSchedule = {
    withSQL {
      insert.into(BusSchedule).columns(
        column.id,
        column.day,
        column.openingTime,
        column.closingTime
      ).values(
        id,
        day,
        openingTime,
        closingTime)
    }.update().apply()

    BusSchedule(
      id = id,
      day = day,
      openingTime = openingTime,
      closingTime = closingTime)
  }

  def save(bs: BusSchedule)(implicit session: DBSession = autoSession): BusSchedule = {
    withSQL {
      update(BusSchedule as busSchedule).set(
        busSchedule.id -> bs.id,
        busSchedule.day -> bs.day,
        busSchedule.openingTime -> bs.openingTime,
        busSchedule.closingTime -> bs.closingTime
      )
    }.update().apply()
    bs
  }

  def destroy(bs: BusSchedule)(implicit session: DBSession = autoSession): Unit = {
    withSQL {
      delete.from(BusSchedule).where.eq(column.id, bs.id)
    }.update.apply()
  }
}
