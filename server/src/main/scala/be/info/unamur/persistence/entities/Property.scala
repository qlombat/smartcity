package be.info.unamur.persistence.entities

import scalikejdbc._


/** Property entity that represents the 'properties' table in the database.
  *
  * @author jeremyduchesne
  * @author Noe Picard
  */
case class Property(id: String,
                    value: String)

object Property extends SQLSyntaxSupport[Property] {
  override val tableName   = "properties"
  override val columns     = Seq("id", "value")
  override val autoSession = AutoSession
  val property: QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[Property], Property] = Property.syntax("p")

  def apply(p: ResultName[Property])(ps: WrappedResultSet): Property = new Property(
    id = ps.string(p.id),
    value = ps.string(p.value)
  )

  def find(id: String)(implicit session: DBSession = autoSession): Option[Property] = {
    withSQL {
      select.from(Property as property).where.eq(property.id, id)
    }.map(Property(property.resultName)).first.apply()
  }

  def create(id: String, value: String)(implicit session: DBSession = autoSession): Property = {
    withSQL {
      insert.into(Property).columns(
        column.id,
        column.value
      ).values(
        id,
        value)
    }.update().apply()

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
      delete.from(Property).where.eq(column.id, p.id)
    }.update.apply()
  }
}