package be.info.unamur.persistence.entities

import scalikejdbc._

/** Property entity that represents the 'properties' table in the database.
  *
  * @author jeremyduchesne
  */
case class Property(id:String,value:Double)

object Property extends SQLSyntaxSupport[Property] {
  override val tableName   = "properties"
  override val columns     = Seq("id","value")
  override val autoSession = AutoSession
  val property: QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[Property], Property] = Property.syntax("p")

  def apply(p: ResultName[Property])(ps: WrappedResultSet): Property = new Property(
    id = ps.string(p.id),
    value = ps.double(p.value)
  )

  def find(id: String)(implicit session: DBSession = autoSession): Option[Property] = {
    withSQL {
      select.from(Property as property).where.eq(property.id, id)
    }.map(Property(property.resultName)).first.apply()
  }

  def create(id:String,value:Double)(implicit session: DBSession = autoSession):Property = {
    Property(
      id = id,
      value = value)
  }

  def save(p: Property)(implicit session: DBSession = autoSession): Property = {
    withSQL {
      update(Property as property).set(
        property.id -> p.id,
        property.value -> p.value
      )
    }.update().apply()
    p
  }

  def destroy(p: Property)(implicit session: DBSession = autoSession): Unit = {
    withSQL {
      delete.from(Property).where.eq(column.id, p.value)
    }.update.apply()
  }
}