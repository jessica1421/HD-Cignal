package models.repo

import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.Account

@Singleton
class AccountRepo @Inject()(
    dao: models.dao.AccountDAO,
    implicit val ec: ExecutionContext,
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  def exists(id: Int): Future[Boolean] = db.run(dao.query(id).exists.result)

  def get: Future[Seq[Account]] = db.run(dao.query.result)

  def find(id: Int): OptionT[Future, Account] =
    OptionT(db.run(dao.query(id).result.headOption))

  def add(account: Account): Future[Int] =
    db.run((dao.query returning dao.query.map(_.id)) += account)

  def update(account: Account): Future[Boolean] =
    db.run({
      dao
        .query(account.id)
        .map(_.name)
        .update(account.name)
        .map( _ > 0)})

  def delete(id: Int): Future[Int] =
    db.run(dao.query(id).delete)
}
