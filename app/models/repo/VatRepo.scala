package models.repo

import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.Vat

@Singleton
class VatRepo @Inject()(
    dao: models.dao.VatDAO,
    implicit val ec: ExecutionContext,
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  def exists(id: Int): Future[Boolean] = db.run(dao.query(id).exists.result)

  def get: Future[Seq[Vat]] = db.run(dao.query.result)

  def find(id: Int): OptionT[Future, Vat] =
    OptionT(db.run(dao.query(id).result.headOption))

  def add(vat: Vat): Future[Int] =
    db.run((dao.query returning dao.query.map(_.id)) += vat)

  def updateName(id: Int, newName: String): Future[Boolean] =
    db.run(dao.query(id).map(_.name).update(newName).map( _ > 0))

  def updateValue(id: Int, value: Double): Future[Boolean] =
    db.run(dao.query(id).map(_.value).update(value).map( _ > 0))

  // def update(vat: Vat)
  //   : Future[Unit] = db.run {
  //   DBIO.seq(Seq(
  //     vat.name.map(dao.query(vat.id).map(_.name).update(_)),
  //     vat.value.map(dao.query(vat.id).map(_.value).update(_)))
  //   .collect({ case Some(action) => action}):_*)
  // }

  def update(vat: Vat): Future[Boolean] =
    db.run(dao.query(vat.id).update(vat).map( _ > 0))

  def delete(id: Int): Future[Int] =
    db.run(dao.query(id).delete)
}
