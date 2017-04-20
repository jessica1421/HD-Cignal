package models.service

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.Proprietor
import errors._

@Singleton
class ProprietorService @Inject()(
    protected val proprietorRepo: models.repo.ProprietorRepo,
    protected val dbConfigProvider: DatabaseConfigProvider,
    protected implicit val ec: ExecutionContext)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._
  private val logger = play.api.Logger(this.getClass())

  def add(proprietor: Proprietor): OptionT[Future, Error] = OptionT {
    proprietorRepo.exists(proprietor.id) flatMap {exists =>
      if (exists) {
        logger.debug(s"Conflicts ${proprietor.id} in adding")
        Future.successful(Some(ObjectConflicts))
      } else {
        proprietorRepo.add(proprietor) map { count =>
          if (count == 1) {
            logger.debug(s"Proprietor ${proprietor.id} successfully added")
            None
          } else {
            logger.error(s"Unknown error occurred in adding proprietor ${proprietor.id}")
            Some(UnknownError)
          }
        }
      }}
  }

  def delete(idProprietor: Int): OptionT[Future, Error] = OptionT {
    proprietorRepo.exists(idProprietor) flatMap {exists =>
      if (exists) {
        proprietorRepo.delete(idProprietor) map { count =>
          if (count == 1) {
            logger.debug(s"Proprietor $idProprietor Deleted ")
            None
          } else {
            logger.error(s"Unknown error occurred in deleting proprietor $idProprietor")
            Some(UnknownError)
          }
        }
      } else {
        logger.debug(s"Proprietor $idProprietor NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }

  def update(proprietor: Proprietor): OptionT[Future, Error] = OptionT {
    proprietorRepo.exists(proprietor.id) flatMap {exists =>
      if (exists) {
        proprietorRepo.update(proprietor) map { success =>
          if (success) {
            logger.debug(s"Proprietor ${proprietor.id} updated")
            None
          } else {
            logger.error(s"Unknown error occurred in updating proprietor ${proprietor.id}")
            Some(UnknownError)
          }
        }
      } else {
        logger.debug(s"Proprietor ${proprietor.id} NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }
}
