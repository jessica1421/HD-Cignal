package models.repo

import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import models.domain.AccountingMethod

@Singleton
class AccountingMethodRepo @Inject()(
    dao: models.dao.AccountingMethodDAO,
    implicit val ec: ExecutionContext,
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  def get: Future[Seq[AccountingMethod]] = db.run(dao.query.result)
}
