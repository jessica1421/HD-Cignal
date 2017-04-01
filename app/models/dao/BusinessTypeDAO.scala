package models.dao

import java.util.UUID
import javax.inject._
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import models.domain.BusinessType

@Singleton
final private[models] class BusinessTypeDAO @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  protected class BusinessTypeTable(tag: Tag) extends Table[BusinessType](tag, "BUSINESSTYPES") {
    def name = column[String]("NAME")
    def id = column[Int]("ID", O.PrimaryKey)

    def * = (name, id.?) <> (BusinessType.tupled, BusinessType.unapply)
  }

  private[models] object query extends TableQuery(new BusinessTypeTable(_)) {
    @inline def apply(id: Int) = this.withFilter(_.id === id)
  }
}
