package models.dao

import java.util.UUID
import javax.inject._
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import models.domain.Country

@Singleton
final private[models] class CountryDAO @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  protected class CountryTable(tag: Tag) extends Table[Country](tag, "COUNTRIES") {
    def name = column[String]("NAME")
    def id = column[Int]("ID", O.PrimaryKey)

    def * = (name, id.?) <> (Country.tupled, Country.unapply)
  }

  private[models] object query extends TableQuery(new CountryTable(_)) {
    @inline def apply(id: Int) = this.withFilter(_.id === id)
  }
}
