package models.dao

import java.time.Instant
import javax.inject.{ Inject, Singleton }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import models.domain.FirmType

@Singleton
final private[models] class FirmTypeDAO @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  protected class FirmTypesTable(tag: Tag)
    extends Table[FirmType](tag, "FIRM_TYPES") {
    def `type` = column[String]("NAME")
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

    def * = (
      `type`,
      id.?) <> (FirmType.tupled, FirmType.unapply)
  }

  private[models] object query extends TableQuery(new FirmTypesTable(_)) {
    @inline def apply(id: Int) = this.withFilter(_.id === id)
  }
}
