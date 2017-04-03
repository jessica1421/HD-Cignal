package models.service
import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.ContactInfo
import errors._

@Singleton
class ContactInfoService @Inject()(
    protected val contactInfoRepo: models.repo.ContactInfoRepo,
    protected val dbConfigProvider: DatabaseConfigProvider,
    protected implicit val ec: ExecutionContext)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._
  private val logger = play.api.Logger(this.getClass())

  def add(contactInfo: ContactInfo): OptionT[Future, Error] = OptionT {
    contactInfoRepo.exists(contactInfo.id) flatMap {exists =>
      if (exists) {
        logger.debug(s"Conflicts ${contactInfo.id} in adding")
        Future.successful(Some(ObjectConflicts))
      } else {
        contactInfoRepo.add(contactInfo) map { count =>
          if (count == 1) {
            logger.debug(s"ContactInfo ${contactInfo.id} successfully added")
            None
          } else {
            logger.error(s"Unknown error occurred in adding contactInfo ${contactInfo.id}")
            Some(UnknownError)
          }
        }
      }}
  }

  def delete(id: Int): OptionT[Future, Error] = OptionT {
    contactInfoRepo.exists(id) flatMap {exists =>
      if (exists) {
        contactInfoRepo.delete(id) map { count =>
          if (count == 1) {
            logger.debug(s"ContactInfo $id Deleted ")
            None
          } else {
            logger.error(s"Unknown error occurred in deleting contactInfo $id")
            Some(UnknownError)
          }
        }
      } else {
        logger.debug(s"ContactInfo $id NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }

  def update(id: Int, newName: String): OptionT[Future, Error] = OptionT {
    contactInfoRepo.exists(id) flatMap {exists =>
      if (exists) {
        contactInfoRepo.update(id, newName) map { success =>
          if (success) {
            logger.debug(s"ContactInfo $id updated")
            None
          } else {
            logger.error(s"Unknown error occurred in updating contactInfo $id")
            Some(UnknownError)
          }
        }
      } else {
        logger.debug(s"ContactInfo $id NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }
}
