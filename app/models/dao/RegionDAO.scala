package models.dao

import java.util.UUID
import javax.inject._
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import models.domain.Region

@Singleton
final private[models] class RegionDAO @Inject()(
    protected val countryDAO: CountryDAO,
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  protected class RegionTable(tag: Tag) extends Table[Region](tag, "REGIONS") {
    def name = column[String]("NAME")
    def idCountry = column[Int]("ID_COUNTRY")
    def id = column[Int]("ID", O.PrimaryKey)

    def * = (name, idCountry, id.?) <> (Region.tupled, Region.unapply)

    def country = foreignKey(
      s"FK_COUNTRIES_${tableName}",
      idCountry,
      countryDAO.query)(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Restrict)
  }

  private[models] object query extends TableQuery(new RegionTable(_)) {
    @inline def apply(id: Int) = this.withFilter(_.id === id)
  }
}

