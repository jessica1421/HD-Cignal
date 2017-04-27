package models.repo

import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.Firm

@Singleton
class FirmRepo @Inject()(
    dao: models.dao.FirmDAO,
    implicit val ec: ExecutionContext,
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  def exists(id: Int): Future[Boolean] = db.run(dao.query(id).exists.result)

  def get: Future[Seq[Firm]] = db.run(dao.query.result)

  def getByType(id: Int): Future[Seq[Firm]] =
    db.run(dao.query.filter(_.id === id).result)

  def find(id: Int): OptionT[Future, Firm] =
    OptionT(db.run(dao.query(id).result.headOption))

  def add(firm: Firm): Future[Int] =
    db.run((dao.query returning dao.query.map(_.id)) += firm)

  def update(firm: Firm): Future[Boolean] =
    db.run(dao.query(firm.id).update(firm).map( _ > 0))

  def delete(id: Int): Future[Int] =
    db.run(dao.query(id).delete)
}
