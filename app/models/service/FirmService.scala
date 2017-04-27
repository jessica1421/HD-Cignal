package models.service

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.{Firm, ContactInfo}
import models.dao.{FirmDAO, BusinessTypeDAO, VatDAO, ContactInfoDAO}
import models.repo.{FirmRepo, BusinessTypeRepo, VatRepo, ContactInfoRepo}
import errors._

@Singleton
class FirmService @Inject()(
    protected val firmDAO: FirmDAO,
    protected val businessTypeDAO: BusinessTypeDAO,
    protected val vatDAO: VatDAO,
    protected val contactInfoDAO: ContactInfoDAO,

    protected val firmRepo: FirmRepo,
    protected val businessTypeRepo: BusinessTypeRepo,
    protected val vatRepo: VatRepo,
    protected val contactInfoRepo: ContactInfoRepo,
    protected val dbConfigProvider: DatabaseConfigProvider,
    protected implicit val ec: ExecutionContext)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._
  private val logger = play.api.Logger(this.getClass())

  def add(firm: Firm, contactInfo: Seq[String]): OptionT[Future, Error] = OptionT {
    firmRepo.exists(firm.id) flatMap {exists =>
      if (exists) {
        logger.debug(s"Conflicts ${firm.id} in adding")
        Future.successful(Some(ObjectConflicts))
      } else {
        db.run {
          (for {
            id <- firmDAO.query returning firmDAO.query.map(_.id) += firm
            contacts <- contactInfoDAO.query ++= contactInfo.map(value => ContactInfo(value, id))
          } yield (contacts).map( _ >  0).getOrElse(false)).transactionally
        }.map(success => if (success) None else Some(UnknownError))
      }}
  }

  //update
  //disable
}
