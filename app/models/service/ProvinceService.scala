package models.service
import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.{ OptionT, EitherT}
import cats.implicits._
import models.domain.Province
import errors._

@Singleton
class ProvinceService @Inject()(
    provinceRepo: models.repo.ProvinceRepo,
    regionRepo: models.repo.ProvinceRepo,
    protected val dbConfigProvider: DatabaseConfigProvider,
    protected implicit val ec: ExecutionContext)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._
  private val logger = play.api.Logger(this.getClass())

  def add(province: Province): EitherT[Future, Error, Int] = {
    for {
      _ <- EitherT[Future, Error, Int] {
          regionRepo.exists(province.idRegion) map {exists =>
            if (exists) Right(0) else  Left(ObjectNotExists)
          }
        }

      id <- EitherT[Future, Error, Int] {
          provinceRepo.add(province) map {id =>
            if (id > 0) Right(id) else Left(UnknownError)
          }
        }
    } yield (id)
  }

  def delete(id: Int): OptionT[Future, Error] = OptionT {
    provinceRepo.exists(id) flatMap {exists =>
      if (exists) {
        provinceRepo.delete(id) map { count =>
          if (count == 1) {
            logger.debug(s"Province $id deleted ")
            None
          } else {
            logger.error(s"Unknown error occurred in deleting province $id")
            Some(UnknownError)
          }
        }
      } else {
        logger.debug(s"Province $id NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }

  def update(id: Int, newIdRegion: Option[Int], newName: Option[String])
    : OptionT[Future, Error] = OptionT {
    provinceRepo.exists(id) flatMap {exists =>
      if (exists) {
        provinceRepo.update(id, newIdRegion, newName).map(r => None)
      } else {
        logger.debug(s"Province $id NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }
}
