package models.repo

import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.Municipality

@Singleton
class MunicipalityRepo @Inject()(
    dao: models.dao.MunicipalityDAO,
    implicit val ec: ExecutionContext,
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  def exists(id: Int): Future[Boolean] = db.run(dao.query(id).exists.result)

  def get: Future[Seq[Municipality]] = db.run(dao.query.result)

  def find(id: Int): OptionT[Future, Municipality] =
    OptionT(db.run(dao.query(id).result.headOption))

  def add(municipality: Municipality): Future[Int] =
    db.run((dao.query returning dao.query.map(_.id)) += municipality)

  def updateName(id: Int, newName: String): Future[Boolean] =
    db.run(dao.query(id).map(_.name).update(newName).map( _ > 0))

  def updateAreaCode(id: Int, areacode: Int): Future[Boolean] =
    db.run(dao.query(id).map(_.areacode).update(areacode).map( _ > 0))

  def updateProvince(id: Int, idProvince: Int): Future[Boolean] =
    db.run(dao.query(id).map(_.idProvince).update(idProvince).map( _ > 0))

  def update(id: Int, newName: Option[String], newAreaCode: Option[Int], newIdProvince: Option[Int])
    : Future[Unit] = db.run {
    DBIO.seq(Seq(
      newName.map(dao.query(id).map(_.name).update(_)),
      newAreaCode.map(dao.query(id).map(_.areacode).update(_)),
      newIdProvince.map(dao.query(id).map(_.idProvince).update(_)))
    .collect({ case Some(action) => action}):_*)
  }

  def delete(id: Int): Future[Int] =
    db.run(dao.query(id).delete)

}
