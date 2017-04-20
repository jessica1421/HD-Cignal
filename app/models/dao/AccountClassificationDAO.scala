package models.dao

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import models.domain.AccountClassification

@Singleton
final private[models] class AccountClassificationDAO @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  protected class AccountClassificationTable(tag: Tag)
    extends Table[AccountClassification](tag, "ACCOUNT_CLASSIFICATIONS") {
    def name = column[String]("NAME")
    def isMain = column[Boolean]("IS_MAIN")
    def id = column[Int]("CODE", O.PrimaryKey)

    def * = (name, isMain, id.?) <> (AccountClassification.tupled, AccountClassification.unapply)
  }

  private[models] object query extends TableQuery(new AccountClassificationTable(_)) {
    @inline def apply(id: Int) = this.withFilter(_.id === id)
  }
}
