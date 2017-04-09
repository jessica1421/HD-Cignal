package models.repo

import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.Proprietor

@Singleton
class ProprietorRepo @Inject()(
    dao: models.dao.ProprietorDAO,
    implicit val ec: ExecutionContext,
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  def exists(id: Int): Future[Boolean] = db.run(dao.query(id).exists.result)

  def get: Future[Seq[Proprietor]] = db.run(dao.query.result)

  def getByFirm(idFirm: Int): Future[Seq[Proprietor]] =
    db.run(dao.query.filter(_.idFirm === idFirm).result)

  def find(id: Int): OptionT[Future, Proprietor] =
    OptionT(db.run(dao.query(id).result.headOption))

  def add(proprietor: Proprietor): Future[Int] =
    db.run((dao.query returning dao.query.map(_.id)) += proprietor)

  def update(proprietor: Proprietor): Future[Boolean] =
    db.run(dao.query(proprietor.id).update(proprietor).map( _ > 0))

  def delete(id: Int): Future[Int] =
    db.run(dao.query(id).delete)
}
