package models.dao

import java.util.UUID
import javax.inject._
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import models.domain.Province

@Singleton
final private[models] class ProvinceDAO @Inject()(
    protected val regionDAO: RegionDAO,
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  protected class ProvinceTable(tag: Tag) extends Table[Province](tag, "PROVINCES") {
    def name = column[String]("NAME")
    def idRegion = column[Int]("ID_REGION")
    def id = column[Int]("ID", O.PrimaryKey)

    def * = (name, idRegion, id.?) <> (Province.tupled, Province.unapply)

    def region = foreignKey(
      s"FK_REGIONS_${tableName}",
      idRegion,
      regionDAO.query)(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Restrict)
  }

  private[models] object query extends TableQuery(new ProvinceTable(_)) {
    @inline def apply(id: Int) = this.withFilter(_.id === id)
  }
}
