package models.repo

import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.Province

@Singleton
class ProvinceRepo @Inject()(
    dao: models.dao.ProvinceDAO,
    implicit val ec: ExecutionContext,
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  def exists(id: Int): Future[Boolean] = db.run(dao.query(id).exists.result)

  def get: Future[Seq[Province]] = db.run(dao.query.result)

  def find(id: Int): OptionT[Future, Province] =
    OptionT(db.run(dao.query(id).result.headOption))

  def add(province: Province): Future[Int] =
    db.run((dao.query returning dao.query.map(_.id)) += province)

  def updateName(id: Int, newName: String): Future[Boolean] =
    db.run(dao.query(id).map(_.name).update(newName).map( _ > 0))

  def updateRegion(id: Int, idRegion: Int): Future[Boolean] =
    db.run(dao.query(id).map(_.idRegion).update(idRegion).map( _ > 0))

  def delete(id: Int): Future[Int] =
    db.run(dao.query(id).delete)
}


