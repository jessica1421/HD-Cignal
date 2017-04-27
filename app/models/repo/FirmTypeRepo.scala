package models.repo

import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.FirmType

@Singleton
class FirmTypeRepo @Inject()(
    dao: models.dao.FirmTypeDAO,
    implicit val ec: ExecutionContext,
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  def exists(id: Int): Future[Boolean] = db.run(dao.query(id).exists.result)

  def get: Future[Seq[FirmType]] = db.run(dao.query.result)

  def find(id: Int): OptionT[Future, FirmType] =
    OptionT(db.run(dao.query(id).result.headOption))

  def add(firmType: FirmType): Future[Int] =
    db.run((dao.query returning dao.query.map(_.id)) += firmType)

  def update(firmType: FirmType): Future[Boolean] =
    db.run(dao.query(firmType.id).update(firmType).map( _ > 0))

  def delete(id: Int): Future[Int] =
    db.run(dao.query(id).delete)
}
