package models.service
import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.Municipality
import errors._

@Singleton
class MunicipalityService @Inject()(
    municipalityDAO: models.dao.MunicipalityDAO,
    municipalityRepo: models.repo.MunicipalityRepo,
    protected val dbConfigProvider: DatabaseConfigProvider,
    protected implicit val ec: ExecutionContext)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._
  private val logger = play.api.Logger(this.getClass())

  def add(municipality: Municipality): OptionT[Future, Error] = OptionT {
    municipalityRepo.exists(municipality.id) flatMap {exists =>
      if (exists) {
        logger.debug(s"Conflicts ${municipality.id} in adding")
        Future.successful(Some(ObjectConflicts))
      } else {
        municipalityRepo.add(municipality) map { count =>
          if (count == 1) {
            logger.debug(s"Municipality ${municipality.id} successfully added")
            None
          } else {
            logger.error(s"Unknown error occurred in adding municipality ${municipality.id}")
            Some(UnknownError)
          }
        }
      }}
  }

  def delete(id: Int): OptionT[Future, Error] = OptionT {
    municipalityRepo.exists(id) flatMap {exists =>
      if (exists) {
        municipalityRepo.delete(id) map { count =>
          if (count == 1) {
            logger.debug(s"Municipality $id Deleted ")
            None
          } else {
            logger.error(s"Unknown error occurred in deleting municipality $id")
            Some(UnknownError)
          }
        }
      } else {
        logger.debug(s"Municipality $id NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }

  def update(
      id: Int,
      newName: Option[String],
      newAreaCode: Option[Int],
      newIdProvince: Option[Int])
    : OptionT[Future, Error] = OptionT {
    municipalityRepo.exists(id) flatMap {exists =>
      if (exists) {
        municipalityRepo.update(id, newName, newAreaCode, newIdProvince).map(r => None)
      } else {
        logger.debug(s"Municipality $id NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }
}
