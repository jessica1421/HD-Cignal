package models.repo

import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import models.domain.AccountingPeriod

@Singleton
class AccountingPeriodRepo @Inject()(
    dao: models.dao.AccountingPeriodDAO,
    implicit val ec: ExecutionContext,
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  def exist(idFirm: Int): Future[Boolean] = db.run(dao.query(idFirm).exists.result)

  def get: Future[Seq[AccountingPeriod]] = db.run(dao.query.result)

  def addOrUpdate(accountingPeriod: AccountingPeriod): Future[Int] =
    db.run(dao.query.insertOrUpdate(accountingPeriod))

  def delete(id: Int): Future[Int] =
    db.run(dao.query(id).delete)

}
