package models.dao

import java.time.Instant
import javax.inject.{ Inject, Singleton }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import models.domain.Account

@Singleton
final private[models] class AccountDAO @Inject()(
    protected val accountClassificationDAO: AccountClassificationDAO,
    protected val firmDAO: FirmDAO,
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  protected class AccountTable(tag: Tag) extends Table[Account](tag, "ACCOUNTS") {
    def postingCode = column[String]("POSTING_CODE")
    def recordCode = column[String]("RECORD_CODE")
    def chartCode = column[String]("CHART_CODE")
    def name = column[String]("NAME")
    def idMain = column[Int]("ID_MAIN")
    def idSubsidiary = column[Int]("ID_SUBSIDIARY")
    def isNominal = column[Boolean]("IS_NOMINAL")
    def isDebit = column[Boolean]("IS_DEBIT")
    def notes = column[String]("NOTES")
    def remarks = column[String]("REMARKS")
    def idParent = column[Option[Int]]("ID_PARENT")
    def idFirm = column[Option[Int]]("ID_FIRM")
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

    def * = (
      postingCode,
      recordCode,
      chartCode,
      name,
      idMain,
      idSubsidiary,
      isNominal,
      isDebit,
      notes,
      remarks,
      idParent,
      idFirm,
      id.?) <> (Account.tupled, Account.unapply)

    def mainAccount = foreignKey(
      s"FK_ACCOUNT_MAIN_${tableName}",
      idMain,
      accountClassificationDAO.query)(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Restrict)

    def subsidiaryAccount = foreignKey(
      s"FK_ACCOUNT_MAIN_${tableName}",
      idSubsidiary,
      accountClassificationDAO.query)(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Restrict)

    def firm = foreignKey(
      s"FK_ACCOUNT_FIRM_${tableName}",
      idFirm,
      firmDAO.query)(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Restrict)
  }

  private[models] object query extends TableQuery(new AccountTable(_)) {
    @inline def apply(id: Int) = this.withFilter(_.id === id)
  }
}
