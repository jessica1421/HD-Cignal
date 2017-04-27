package models.dao

import java.time.Instant
import javax.inject.{ Inject, Singleton }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import models.domain.AccountingPeriod

@Singleton
final private[models] class AccountingPeriodDAO @Inject()(
    protected val firmDAO: FirmDAO,
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  protected class AccountingPeriodTable(tag: Tag)
    extends Table[AccountingPeriod](tag, "ACCOUNTING_PERIODS") {
    def idFirm = column[Int]("ID_FIRM",O.PrimaryKey)
    def startDate = column[Instant]("START_DATE")
    def endDate = column[Instant]("END_DATE")
    def id = column[Int]("ID_FIRM", O.PrimaryKey, O.AutoInc)

    def * = (idFirm, startDate, endDate, id.?) <> (AccountingPeriod.tupled, AccountingPeriod.unapply)

    def firm = foreignKey(
      s"FK_FIRMS_${tableName}",
      idFirm,
      firmDAO.query)(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Restrict)

    def idxStartDate =
      index(s"IDX_START_DATE_$tableName", startDate)

    def idxEndDate =
      index(s"IDX_END_DATE_$tableName", endDate)
  }

  private[models] object query extends TableQuery(new AccountingPeriodTable(_)) {
    @inline def apply(idFirm: Int) = this.withFilter(_.idFirm === idFirm)
  }
}
