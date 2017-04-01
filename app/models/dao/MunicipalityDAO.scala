package models.dao

import java.util.UUID
import javax.inject._
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import models.domain.Municipality

@Singleton
final private[models] class MunicipalityDAO @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  protected class MunicipalityTable(tag: Tag) extends Table[Municipality](tag, "MUNICIPALITIES") {
    def name = column[String]("NAME")
    def areacode = column[Int]("AREACODE")
    def idProvince = column[Int]("ID_PROVINCE")
    def id = column[Int]("ID", O.PrimaryKey)

    def * = (name, areacode, idProvince, id.?) <> (Municipality.tupled, Municipality.unapply)
  }

  private[models] object query extends TableQuery(new MunicipalityTable(_)) {
    @inline def apply(id: Int) = this.withFilter(_.id === id)
  }
}
