package models.util

import javax.inject._
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import models.dao._

@Singleton
class SchemaGenerator @Inject()(
    protected val accountDAO: AccountDAO,
    protected val accClassificationDAO: AccountClassificationDAO,
    protected val accMethodDAO: AccountingMethodDAO,
    protected val accPeriodDAO: AccountingPeriodDAO,
    protected val businessTypeDAO: BusinessTypeDAO,
    protected val contactInfoDAO: ContactInfoDAO,
    protected val countryDAO: CountryDAO,
    protected val firmDAO: FirmDAO,
    protected val municipalityDAO: MunicipalityDAO,
    protected val postingMethodDAO: PostingMethodDAO,
    protected val proprietorDAO: ProprietorDAO,
    protected val provinceDAO: ProvinceDAO,
    protected val regionDAO: RegionDAO,
    protected val vatDAO: VatDAO,
    val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._
  def createDDLScript() = {
    val schemas =
      accountDAO.query.schema ++
      accClassificationDAO.query.schema ++
      accMethodDAO.query.schema ++
      accPeriodDAO.query.schema ++
      businessTypeDAO.query.schema ++
      contactInfoDAO.query.schema ++
      countryDAO.query.schema ++
      firmDAO.query.schema ++
      municipalityDAO.query.schema ++
      postingMethodDAO.query.schema ++
      proprietorDAO.query.schema ++
      provinceDAO.query.schema ++
      regionDAO.query.schema ++
      vatDAO.query.schema

    val writer = new java.io.PrintWriter("target/schema.sql")
    writer.write("# --- !Ups\n\n")
    schemas.createStatements.foreach { s => writer.write(s + ";\n\n") }
    writer.write("\n\n# --- !Downs\n\n")
    schemas.dropStatements.foreach { s => writer.write(s + ";\n") }
    println("Schema definitions are written")
    writer.close()
  }
  createDDLScript()
}
