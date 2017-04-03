package models.service
import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.BusinessType
import errors._

@Singleton
class BusinessTypeService @Inject()(
    businessTypeRepo: models.repo.BusinessTypeRepo,
    protected val dbConfigProvider: DatabaseConfigProvider,
    protected implicit val ec: ExecutionContext)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._
  private val logger = play.api.Logger(this.getClass())

  def add(businessType: BusinessType): OptionT[Future, Error] = OptionT {
    businessTypeRepo.exists(businessType.id) flatMap {exists =>
      if (exists) {
        logger.debug(s"Conflicts ${businessType.id} in adding")
        Future.successful(Some(ObjectConflicts))
      } else {
        businessTypeRepo.add(businessType) map { count =>
          if (count == 1) {
            logger.debug(s"BusinessType ${businessType.id} successfully added")
            None
          } else {
            logger.error(s"Unknown error occurred in adding businessType ${businessType.id}")
            Some(UnknownError)
          }
        }
      }}
  }

  def delete(id: Int): OptionT[Future, Error] = OptionT {
    businessTypeRepo.exists(id) flatMap {exists =>
      if (exists) {
        businessTypeRepo.delete(id) map { count =>
          if (count == 1) {
            logger.debug(s"BusinessType $id Deleted ")
            None
          } else {
            logger.error(s"Unknown error occurred in deleting businessType $id")
            Some(UnknownError)
          }
        }
      } else {
        logger.debug(s"BusinessType $id NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }

  def update(id: Int, newName: String): OptionT[Future, Error] = OptionT {
    businessTypeRepo.exists(id) flatMap {exists =>
      if (exists) {
        businessTypeRepo.update(id, newName) map { success =>
          if (success) {
            logger.debug(s"BusinessType $id updated")
            None
          } else {
            logger.error(s"Unknown error occurred in updating businessType $id")
            Some(UnknownError)
          }
        }
      } else {
        logger.debug(s"BusinessType $id NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }
}
