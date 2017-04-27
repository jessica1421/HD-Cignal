package models.service

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.FirmType
import models.dao.FirmTypeDAO
import models.repo.FirmTypeRepo
import errors._

@Singleton
class FirmTypeService @Inject()(
    protected val firmTypeRepo: FirmTypeRepo,
    protected val dbConfigProvider: DatabaseConfigProvider,
    protected implicit val ec: ExecutionContext)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._
  private val logger = play.api.Logger(this.getClass())

  def add(firmType: FirmType): OptionT[Future, Error] = OptionT {
    firmTypeRepo.exists(firmType.id) flatMap {exists =>
      if (exists) {
        logger.debug(s"Conflicts ${firmType.id} in adding")
        Future.successful(Some(ObjectConflicts))
      } else {
        firmTypeRepo.add(firmType) map { count =>
          if (count == 1) {
            logger.debug(s"Vat ${firmType.id} successfully added")
            None
          } else {
            logger.error(s"Unknown error occurred in adding firmType ${firmType.id}")
            Some(UnknownError)
          }
        }
      }}
  }

  def delete(id: Int): OptionT[Future, Error] = OptionT {
    firmTypeRepo.exists(id) flatMap {exists =>
      if (exists) {
        firmTypeRepo.delete(id) map {count =>
          if (count == 1) {
            logger.debug(s"Vat $id Deleted ")
            None
          } else {
            logger.error(s"Unknown error occurred in deleting firmType $id")
            Some(UnknownError)
          }
        }
      } else {
        logger.debug(s"Vat $id NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }

  def update(firmType: FirmType)
    : OptionT[Future, Error] = OptionT {
    firmTypeRepo.exists(firmType.id) flatMap {exists =>
      if (exists) {
        firmTypeRepo.update(firmType).map(r => None)
      } else {
        logger.debug(s"firmType $firmType.id NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }
}
