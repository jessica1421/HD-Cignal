package models.service
import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT
import models.domain.Vat
import errors._

@Singleton
class VatService @Inject()(
    vatRepo: models.repo.VatRepo,
    protected val dbConfigProvider: DatabaseConfigProvider,
    protected implicit val ec: ExecutionContext)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._
  private val logger = play.api.Logger(this.getClass())

  def add(vat: Vat): OptionT[Future, Error] = OptionT {
    vatRepo.exists(vat.id) flatMap {exists =>
      if (exists) {
        logger.debug(s"Conflicts ${vat.id} in adding")
        Future.successful(Some(ObjectConflicts))
      } else {
        vatRepo.add(vat) map { count =>
          if (count == 1) {
            logger.debug(s"Vat ${vat.id} successfully added")
            None
          } else {
            logger.error(s"Unknown error occurred in adding vat ${vat.id}")
            Some(UnknownError)
          }
        }
      }}
  }

  def delete(id: Int): OptionT[Future, Error] = OptionT {
    vatRepo.exists(id) flatMap {exists =>
      if (exists) {
        vatRepo.delete(id) map {count =>
          if (count == 1) {
            logger.debug(s"Vat $id Deleted ")
            None
          } else {
            logger.error(s"Unknown error occurred in deleting vat $id")
            Some(UnknownError)
          }
        }
      } else {
        logger.debug(s"Vat $id NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }

  def update(id: Int, newName: Option[String], newValue: Option[Double])
    : OptionT[Future, Error] = OptionT {
    vatRepo.exists(id) flatMap {exists =>
      if (exists) {
        vatRepo.update(id, newName, newValue).map(r => None)
      } else {
        logger.debug(s"Vat $id NotFound")
        Future.successful(Some(ObjectNotExists))
    }}
  }
}
