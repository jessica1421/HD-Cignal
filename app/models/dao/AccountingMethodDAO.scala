package models.dao

import javax.inject._
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import models.domain.AccountingMethod

@Singleton
final private[models] class AccountingMethodDAO @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  protected class AccountingMethodTable(tag: Tag) extends Table[AccountingMethod](tag, "VATS") {
    def code = column[String]("CODE", O.PrimaryKey)
    def name = column[String]("NAME")

    def * = (code, name) <> (AccountingMethod.tupled, AccountingMethod.unapply)
  }

  private[models] object query extends TableQuery(new AccountingMethodTable(_)) {
    @inline def apply(code: String) = this.withFilter(_.code === code)
  }
}
