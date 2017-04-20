package models.repo

import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.AccountClassification

@Singleton
class AccountClassificationRepo @Inject()(
    dao: models.dao.AccountClassificationDAO,
    implicit val ec: ExecutionContext,
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  def exists(id: Int): Future[Boolean] = db.run(dao.query(id).exists.result)

  def get: Future[Seq[AccountClassification]] = db.run(dao.query.result)

  def getByType(isMain: Boolean): Future[Seq[AccountClassification]] =
    db.run(dao.query.filter(_.isMain === isMain).result)

  def find(id: Int): OptionT[Future, AccountClassification] =
    OptionT(db.run(dao.query(id).result.headOption))

  def add(accountClassification: AccountClassification): Future[Int] =
    db.run((dao.query returning dao.query.map(_.id)) += accountClassification)

  def update(accountClassification: AccountClassification): Future[Boolean] =
    db.run({
      dao
        .query(accountClassification.id)
        .map(_.name)
        .update(accountClassification.name)
        .map( _ > 0)})

  def delete(id: Int): Future[Int] =
    db.run(dao.query(id).delete)
}
