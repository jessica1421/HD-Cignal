package models.service
import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.Country
import errors._

@Singleton
class CountryService @Inject()(
    protected val countryDAO: models.dao.CountryDAO,
    protected val countryRepo: models.repo.CountryRepo,
    protected val dbConfigProvider: DatabaseConfigProvider,
    protected implicit val ec: ExecutionContext)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._
  private val logger = play.api.Logger(this.getClass())

  def add(country: Country): OptionT[Future, Error] = OptionT {
    countryRepo.exists(country.id) flatMap {exists =>
      if (exists) {
        logger.debug(s"Conflicts ${country.id} in adding")
        Future.successful(Some(ObjectConflicts))
      } else {
        countryRepo.add(country) map { count =>
          if (count == 1) {
            logger.debug(s"Country ${country.id} successfully added")
            None
          } else {
            logger.error(s"Unknown error occurred in adding country ${country.id}")
            Some(UnknownError)
          }
        }
      }}
  }

  def delete(id: Int): OptionT[Future, Error] = OptionT {
    countryRepo.exists(id) flatMap {exists =>
      if (exists) {
        countryRepo.delete(id) map { count =>
          if (count == 1) {
            logger.debug(s"Country $id Deleted ")
            None
          } else {
            logger.error(s"Unknown error occurred in deleting country $id")
            Some(UnknownError)
          }
        }
      } else {
        logger.debug(s"Country $id NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }

  def update(id: Int, newName: String): OptionT[Future, Error] = OptionT {
    countryRepo.exists(id) flatMap {exists =>
      if (exists) {
        countryRepo.update(id, newName) map { success =>
          if (success) {
            logger.debug(s"Country $id updated")
            None
          } else {
            logger.error(s"Unknown error occurred in updating country $id")
            Some(UnknownError)
          }
        }
      } else {
        logger.debug(s"Country $id NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }
}
