package models.repo

import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.Region

@Singleton
class RegionRepo @Inject()(
    dao: models.dao.RegionDAO,
    implicit val ec: ExecutionContext,
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  def exists(id: Int): Future[Boolean] = db.run(dao.query(id).exists.result)

  def get: Future[Seq[Region]] = db.run(dao.query.result)

  def find(id: Int): OptionT[Future, Region] =
    OptionT(db.run(dao.query(id).result.headOption))

  def add(region: Region): Future[Int] =
    db.run((dao.query returning dao.query.map(_.id)) += region)

  def updateName(id: Int, newName: String): Future[Boolean] =
    db.run(dao.query(id).map(_.name).update(newName).map( _ > 0))

  def updateCountry(id: Int, idCountry: Int): Future[Boolean] =
    db.run(dao.query(id).map(_.idCountry).update(idCountry).map( _ > 0))

  def update(id: Int, newIdCountry: Option[Int], newName: Option[String])
    : Future[Unit] = db.run {
    DBIO.seq(Seq(
      newIdCountry.map(dao.query(id).map(_.idCountry).update(_)),
      newName.map(dao.query(id).map(_.name).update(_)))
    .collect({ case Some(action) => action}):_*)
  }

  def delete(id: Int): Future[Int] =
    db.run(dao.query(id).delete)
}


