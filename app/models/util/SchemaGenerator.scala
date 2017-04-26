package models.util

import javax.inject._
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import models.dao._

@Singleton
class SchemaGenerator @Inject()(
    protected val countryDA0: CountryDAO,
    protected val regionDA0: RegionDAO,
    protected val provinceDA0: ProvinceDAO,
    protected val municipalityDA0: MunicipalityDAO,
    protected val contactInfoDA0: ContactInfoDAO,
    protected val vatDA0: VatDAO,
    protected val businessTypeDA0: BusinessTypeDAO,
    protected val accountDAO: AccountDAO,

    val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._
  def createDDLScript() = {
    val schemas =
      countryDA0.query.schema ++
      regionDA0.query.schema ++
      provinceDA0.query.schema ++
      municipalityDA0.query.schema ++
      contactInfoDA0.query.schema ++
      vatDA0.query.schema ++
      businessTypeDA0.query.schema ++
      accountDAO.query.schema

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
