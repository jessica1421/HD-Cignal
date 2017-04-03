package models.service
import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.{ OptionT, EitherT}
import cats.implicits._
import models.domain.{ Country, Region }
import errors._

@Singleton
class RegionService @Inject()(
    protected val countryDAO: models.dao.CountryDAO,
    protected val regionDAO: models.dao.RegionDAO,
    protected val countryRepo: models.repo.CountryRepo,
    protected val regionRepo: models.repo.RegionRepo,
    protected val dbConfigProvider: DatabaseConfigProvider,
    protected implicit val ec: ExecutionContext)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._
  private val logger = play.api.Logger(this.getClass())

  def getByCountry(idCountry: Int): Future[Seq[(Country, Region)]] =
    db.run((countryDAO.query(idCountry) join regionDAO.query on(_.id === _.idCountry)).result)

  def add(region: Region): EitherT[Future, Error, Int] = {
    for {
      _ <- EitherT[Future, Error, Int] {
          countryRepo.exists(region.idCountry) map {exists =>
            if (exists) {
              Right(0)
            } else {
              logger.debug(s"Country ${region.idCountry} NotFound")
              Left(ObjectNotExists)
            }
          }
        }
      _ <- EitherT[Future, Error, Int] {
          regionRepo.exists(region.id) map {exists =>
            if (exists) {
              logger.debug(s"Region ${region.id} already exists")
              Left(ObjectConflicts)
            } else {
              Right(0)
            }
          }
        }
      id <- EitherT[Future, Error, Int] {
          regionRepo.add(region) map {id =>
            if (id > 0) {
              Right(id)
            } else {
              Left(UnknownError)
            }
          }
        }

    } yield (id)
  }

  def delete(id: Int): OptionT[Future, Error] = OptionT {
    regionRepo.exists(id) flatMap {exists =>
      if (exists) {
        regionRepo.delete(id) map { count =>
          if (count == 1) {
            logger.debug(s"Region $id deleted ")
            None
          } else {
            logger.error(s"Unknown error occurred in deleting region $id")
            Some(UnknownError)
          }
        }
      } else {
        logger.debug(s"Region $id NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }

  def update(id: Int, newIdCountry: Option[Int], newName: Option[String])
    : OptionT[Future, Error] = OptionT {
    regionRepo.exists(id) flatMap {exists =>
      if (exists) {
        regionRepo.update(id, newIdCountry, newName).map(r => None)
      } else {
        logger.debug(s"Region $id NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }
}
