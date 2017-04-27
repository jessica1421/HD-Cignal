package models.dao

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import models.domain.Proprietor

@Singleton
final private[models] class ProprietorDAO @Inject()(
    protected val firmDAO: FirmDAO,
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  protected class ProprietorTable(tag: Tag) extends Table[Proprietor](tag, "PROPRIETORS") {
    def name = column[String]("NAME")
    def share = column[Double]("SHARE")
    def idFirm = column[Int]("ID_FIRM")
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

    def * = (name, share, idFirm, id.?)<>(Proprietor.tupled, Proprietor.unapply)

    def firm = foreignKey(
      s"FK_COUNTRIES_${tableName}",
      idFirm,
      firmDAO.query)(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Restrict)
  }

  private[models] object query extends TableQuery(new ProprietorTable(_)) {
    @inline def apply(id: Int) = this.withFilter(_.id === id)
  }
}
