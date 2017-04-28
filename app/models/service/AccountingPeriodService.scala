package models.service

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.AccountingPeriod
import errors._

@Singleton
class AccountingPeriodService @Inject()(
    protected val accountingPeriodRepo: models.repo.AccountingPeriodRepo,
    protected val dbConfigProvider: DatabaseConfigProvider,
    protected implicit val ec: ExecutionContext)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._
  private val logger = play.api.Logger(this.getClass())

  def add(ap: AccountingPeriod): OptionT[Future, Error] = OptionT {
    accountingPeriodRepo.exists(ap.id) flatMap { exists =>
      if (exists) {
        logger.debug(s"Conflicts ${ap.id} in adding")
        Future.successful(Some(ObjectConflicts))
      } else {
        accountingPeriodRepo.addOrUpdate(ap) map { count =>
          if (count == 1) {
            logger.debug(s"Account period ${ap.id} successfully added")
            None
          } else {
            logger.error(s"Unknown error occurred in adding account period ${ap.id}")
            Some(UnknownError)
          }
        }
      }}
  }

  def delete(id: Int): OptionT[Future, Error] = OptionT {
    accountingPeriodRepo.exists(id) flatMap { exists =>
      if (exists) {
        accountingPeriodRepo.delete(id) map { count =>
          if (count == 1) {
            logger.debug(s"Account period $id Deleted ")
            None
          } else {
            logger.error(s"Unknown error occurred in deleting businessType $id")
            Some(UnknownError)
          }
        }
      } else {
        logger.debug(s"Account period $id NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }

  def update(ap: AccountingPeriod): OptionT[Future, Error] = OptionT {
    accountingPeriodRepo.exists(ap.id) flatMap {exists =>
      if (exists) {
        accountingPeriodRepo.addOrUpdate(ap) map { success =>
          if (success > 1) {
            logger.debug(s"Account period $ap.id.get updated")
            None
          } else {
            logger.error(s"Unknown error occurred in updating Accounting period $ap.id.get")
            Some(UnknownError)
          }
        }
      } else {
        logger.debug(s"Account period $ap.id.get NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }
}
