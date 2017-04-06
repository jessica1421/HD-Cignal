package models.repo

import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.BusinessType

@Singleton
class BusinessTypeRepo @Inject()(
    dao: models.dao.BusinessTypeDAO,
    implicit val ec: ExecutionContext,
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  def exists(id: Int): Future[Boolean] = db.run(dao.query(id).exists.result)

  def get: Future[Seq[BusinessType]] = db.run(dao.query.result)

  def find(id: Int): OptionT[Future, BusinessType] =
    OptionT(db.run(dao.query(id).result.headOption))

  def add(businesstype: BusinessType): Future[Int] =
    db.run((dao.query returning dao.query.map(_.id)) += businesstype)

  def update(businesstype: BusinessType): Future[Boolean] =
    db.run(dao.query(businesstype.id).map(_.name).update(businesstype.name).map( _ > 0))

  def delete(id: Int): Future[Int] =
    db.run(dao.query(id).delete)
}
