package models.repo

import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.Country

@Singleton
class CountryRepo @Inject()(
    dao: models.dao.CountryDAO,
    implicit val ec: ExecutionContext,
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  def exists(id: Int): Future[Boolean] = db.run(dao.query(id).exists.result)

  def get: Future[Seq[Country]] = db.run(dao.query.result)

  def find(id: Int): OptionT[Future, Country] =
    OptionT(db.run(dao.query(id).result.headOption))

  def add(country: Country): Future[Int] =
    db.run((dao.query returning dao.query.map(_.id)) += country)

  def update(country: Country): Future[Boolean] =
    db.run(dao.query(country.id).map(_.name).update(country.name).map( _ > 0))

  def delete(id: Int): Future[Int] =
    db.run(dao.query(id).delete)
}
