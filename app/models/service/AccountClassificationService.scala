package models.service

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.AccountClassification
import errors._

@Singleton
class AccountClassificationService @Inject()(
    protected val accountClassificationRepo: models.repo.AccountClassificationRepo,
    protected val dbConfigProvider: DatabaseConfigProvider,
    protected implicit val ec: ExecutionContext)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._
  private val logger = play.api.Logger(this.getClass())

  def add(accountClassification: AccountClassification): OptionT[Future, Error] = OptionT {
    accountClassificationRepo.exists(accountClassification.id) flatMap {exists =>
      if (exists) {
        logger.debug(s"Conflicts ${accountClassification.id} in adding")
        Future.successful(Some(ObjectConflicts))
      } else {
        accountClassificationRepo.add(accountClassification) map { count =>
          if (count == 1) {
            logger.debug(s"AccountClassification ${accountClassification.id} successfully added")
            None
          } else {
            logger.error(s"Unknown error occurred in adding accountClassification ${accountClassification.id}")
            Some(UnknownError)
          }
        }
      }}
  }

  def delete(id: Int): OptionT[Future, Error] = OptionT {
    accountClassificationRepo.exists(id) flatMap {exists =>
      if (exists) {
        accountClassificationRepo.delete(id) map { count =>
          if (count == 1) {
            logger.debug(s"AccountClassification $id Deleted ")
            None
          } else {
            logger.error(s"Unknown error occurred in deleting accountClassification $id")
            Some(UnknownError)
          }
        }
      } else {
        logger.debug(s"AccountClassification $id NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }

  def update(accountClassification: AccountClassification): OptionT[Future, Error] = OptionT {
    accountClassificationRepo.exists(accountClassification.id) flatMap {exists =>
      if (exists) {
        accountClassificationRepo.update(accountClassification) map { success =>
          if (success) {
            logger.debug(s"AccountClassification $accountClassification.id.get updated")
            None
          } else {
            logger.error(s"Unknown error occurred in updating accountClassification $accountClassification.id.get")
            Some(UnknownError)
          }
        }
      } else {
        logger.debug(s"AccountClassification $accountClassification.id.get NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }
}
