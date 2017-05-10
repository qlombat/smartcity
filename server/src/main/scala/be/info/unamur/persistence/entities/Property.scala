package be.info.unamur.persistence.entities

import scalikejdbc._

/** Property entity that represents the 'properties' table in the database.
  *
  * @author jeremyduchesne
  */
case class Property(key:String,value:Double)

object Property extends SQLSyntaxSupport[Property] {
  override val tableName   = "properties"
  override val columns     = Seq("key","value")
  override val autoSession = AutoSession
  val property: QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[Property], Property] = Property.syntax("p")

  def apply(p: ResultName[Property])(ps: WrappedResultSet): Property = new Property(
    key = ps.string(p.key),
    value = ps.double(p.value)
  )

  def find(key: String)(implicit session: DBSession = autoSession): Option[Property] = {
    withSQL {
      select.from(Property as property).where.eq(property.key, key)
    }.map(Property(property.resultName)).first.apply()
  }

  def create(key:String,value:Double)(implicit session: DBSession = autoSession):Property = {
    Property(
      key = key,
      value = value)
  }

  def save(p: Property)(implicit session: DBSession = autoSession): Property = {
    withSQL {
      update(Property as property).set(
        property.key -> p.key,
        property.value -> p.value
      )
    }.update().apply()
    p
  }

  def destroy(p: Property)(implicit session: DBSession = autoSession): Unit = {
    withSQL {
      delete.from(Property).where.eq(column.key, p.value)
    }.update.apply()
  }
}