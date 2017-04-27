package models.repo

import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.ContactInfo

@Singleton
class ContactInfoRepo @Inject()(
    dao: models.dao.ContactInfoDAO,
    implicit val ec: ExecutionContext,
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  def exists(id: Int): Future[Boolean] = db.run(dao.query(id).exists.result)

  def get: Future[Seq[ContactInfo]] = db.run(dao.query.result)

  def getByFirm(idFirm: Int): Future[Seq[ContactInfo]] =
    db.run(dao.query.filter(_.idFirm === idFirm).result)

  def find(id: Int): OptionT[Future, ContactInfo] =
    OptionT(db.run(dao.query(id).result.headOption))

  def add(contactinfo: ContactInfo): Future[Int] =
    db.run((dao.query returning dao.query.map(_.id)) += contactinfo)

  def update(contactinfo: ContactInfo): Future[Boolean] =
    db.run(dao.query(contactinfo.id).update(contactinfo).map( _ > 0))

  def delete(id: Int): Future[Int] =
    db.run(dao.query(id).delete)
}
