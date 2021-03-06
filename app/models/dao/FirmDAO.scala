package models.dao

import java.time.Instant
import javax.inject.{ Inject, Singleton }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import models.domain.Firm

@Singleton
final private[models] class FirmDAO @Inject()(
    protected val accountingMethodDAO: AccountingMethodDAO,
    protected val businessTypeDAO: BusinessTypeDAO,
    protected val municipalityDAO: MunicipalityDAO,
    protected val postingMethodDAO: PostingMethodDAO,
    protected val vatDAO: VatDAO,
    protected val firmTypeDAO: FirmTypeDAO,
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  protected class FirmTable(tag: Tag)
    extends Table[Firm](tag, "FIRMS") {
    def name = column[String]("NAME")
    def address = column[String]("ADDRESS")
    def postingPeriod = column[Short]("POSTING_PERIOD")
    def idBusinessType = column[Int]("ID_BUSINESS_TYPE")
    def idVat = column[Int]("ID_VAT")
    def areaCode = column[Int]("AREA_CODE")
    def codeAccountingMethod = column[String]("CODE_ACCOUNTING_METHOD")
    def codePostingMethod = column[String]("CODE_POSTING_METHOD")
    def idParent = column[Option[Int]]("ID_PARENT")
    def idFirmType = column[Int]("ID_FIRMTYPE")
    def idEnable = column[Boolean]("IS_ENABLE")
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

    def * = (
      name,
      address,
      postingPeriod,
      idBusinessType,
      idVat,
      areaCode,
      codeAccountingMethod,
      codePostingMethod,
      idParent,
      idFirmType,
      idEnable,
      id.?) <> (Firm.tupled, Firm.unapply)

    def accountingMethod = foreignKey(
      s"FK_ACCOUNTING_METHOD_${tableName}",
      codeAccountingMethod,
      accountingMethodDAO.query)(
      _.code,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Restrict)

    def bussinessType = foreignKey(
      s"FK_BUSINESSTYPES_${tableName}",
      idBusinessType,
      businessTypeDAO.query)(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Restrict)

    def vat = foreignKey(
      s"FK_VAT_${tableName}",
      idVat,
      vatDAO.query)(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Restrict)

    def postingMethod = foreignKey(
      s"FK_POSTING_METHOD_${tableName}",
      codePostingMethod,
      postingMethodDAO.query)(
      _.code,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Restrict)

    def firmType = foreignKey(
      s"FK_FIRM_TYPE_${tableName}",
      idFirmType,
      firmTypeDAO.query)(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Restrict)
  }

  private[models] object query extends TableQuery(new FirmTable(_)) {
    @inline def apply(id: Int) = this.withFilter(_.id === id)
  }
}

/**
  Notes:
    Trade name for each company // possible holding
*/
