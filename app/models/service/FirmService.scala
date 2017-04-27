package models.service

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.{ OptionT, EitherT }
import cats.implicits._
import models.domain.{Firm, ContactInfo}
import models.dao.{FirmDAO, BusinessTypeDAO, VatDAO, ContactInfoDAO}
import models.repo._
import errors._

@Singleton
class FirmService @Inject()(
    protected val firmDAO: FirmDAO,
    protected val businessTypeDAO: BusinessTypeDAO,
    protected val vatDAO: VatDAO,
    protected val contactInfoDAO: ContactInfoDAO,

    protected val firmRepo: FirmRepo,
    protected val accountingMethodRepo: AccountingMethodRepo,
    protected val postingMethodRepo: PostingMethodRepo,
    protected val businessTypeRepo: BusinessTypeRepo,
    protected val vatRepo: VatRepo,
    protected val contactInfoRepo: ContactInfoRepo,
    protected val dbConfigProvider: DatabaseConfigProvider,
    protected implicit val ec: ExecutionContext)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._
  private val logger = play.api.Logger(this.getClass())

  def addFirm(firm: Firm): OptionT[Future, Error] = OptionT {
    firmRepo.exists(firm.id) flatMap { exists =>
      if (exists) {
        logger.debug(s"Conflicts ${firm.id} in adding")
        Future.successful(Some(ObjectConflicts))
      } else {
        db.run(firmDAO.query returning firmDAO.query.map(_.id) += firm)
          .map(id => if (id > 0) None else Some(UnknownError))
      }
    }
  }

  def addFirm(firm: Firm, contactInfo: Seq[String]): OptionT[Future, Error] = OptionT {
    firmRepo.exists(firm.id) flatMap { exists =>
      if (exists) {
        logger.debug(s"Conflicts ${firm.id} in adding")
        Future.successful(Some(ObjectConflicts))
      } else {
        db.run {
          (for {
            id <- firmDAO.query returning firmDAO.query.map(_.id) += firm
            contacts <- contactInfoDAO.query ++= contactInfo.map(value => ContactInfo(value, id))
          } yield (contacts).map( _ >  0).getOrElse(false)).transactionally
        }.map(success => if (success) None else Some(UnknownError))
    }}
  }

  // update codePostingMethod
  def updateCodePostingMethod(idFirm: Int, code: String)
    : EitherT[Future, Error, Unit] = {
    for {
      _ <- EitherT[Future, Error, Unit] {
        firmRepo.exists(idFirm) map { exists =>
          if (exists) {
            Right(())
          } else Left(ObjectNotExists)
        }
      }
      _ <- EitherT[Future, Error, Unit] {
        postingMethodRepo.exists(code) map { exists =>
          if (exists) {
            Right(())
          } else Left(ObjectNotExists)
        }
      }
      _ <- EitherT[Future, Error, Unit] {
        db
        .run(firmDAO.query(idFirm).map(_.codePostingMethod).update(code))
        .map(count => if (count > 0) Right(()) else Left(UnknownError))
      }
    } yield ()
  }

  // update idBusinessType
  def updateBusinessType(idFirm: Int, idBusinessType: Int): EitherT[Future, Error, Unit] = {
    for {
      _ <- EitherT[Future, Error, Unit] {
        firmRepo.exists(idFirm) map { exists =>
          if (exists) {
            Right(())
          } else Left(ObjectNotExists)
        }
      }
      _ <- EitherT[Future, Error, Unit] {
        businessTypeRepo.exists(idBusinessType) map { exists =>
          if (exists) {
            Right(())
          } else Left(ObjectNotExists)
        }
      }
      _ <- EitherT[Future, Error, Unit] {
        db
        .run(firmDAO.query(idFirm).map(_.idBusinessType).update(idBusinessType))
        .map(count => if (count > 0) Right(()) else Left(UnknownError))
      }
    } yield ()
  }

  // update idVat
  def updateVat(idFirm: Int, idVat: Int): EitherT[Future, Error, Unit] = {
    for {
      _ <- EitherT[Future, Error, Unit] {
        firmRepo.exists(idFirm) map { exists =>
          if (exists) {
            Right(())
          } else Left(ObjectNotExists)
        }
      }
      _ <- EitherT[Future, Error, Unit] {
        vatRepo.exists(idVat) map { exists =>
          if (exists) {
            Right(())
          } else Left(ObjectNotExists)
        }
      }
      _ <- EitherT[Future, Error, Unit] {
        db
        .run(firmDAO.query(idFirm).map(_.idVat).update(idVat))
        .map(count => if (count > 0) Right(()) else Left(UnknownError))
      }
    } yield ()
  }
  // update codeAccountingMethod
  def updateCodeAccountingMethod(idFirm: Int, code: String)
    : EitherT[Future, Error, Unit] = {
    for {
      _ <- EitherT[Future, Error, Unit] {
        firmRepo.exists(idFirm) map { exists =>
          if (exists) {
            Right(())
          } else Left(ObjectNotExists)
        }
      }
      _ <- EitherT[Future, Error, Unit] {
        accountingMethodRepo.exists(code) map { exists =>
          if (exists) {
            Right(())
          } else Left(ObjectNotExists)
        }
      }
      _ <- EitherT[Future, Error, Unit] {
        db
        .run(firmDAO.query(idFirm).map(_.codeAccountingMethod).update(code))
        .map(count => if (count > 0) Right(()) else Left(UnknownError))
      }
    } yield ()
  }
// update idFirmType
  def updateFirmBasicInfo(
      idFirm: Int,
      newName: Option[String],
      newAddress: Option[String],
      newAreaCode: Option[Int])
    : OptionT[Future, Error] = OptionT {
    firmRepo.exists(idFirm) flatMap { exists =>
      if (exists) {
        db.run {
          DBIO.seq(Seq(
            newName.map(firmDAO.query(idFirm).map(_.name).update(_)),
            newAddress.map(firmDAO.query(idFirm).map(_.address).update(_)),
            newAreaCode.map(firmDAO.query(idFirm).map(_.areaCode).update(_)))
            .collect({ case Some(action) => action}):_*)
        } map (r => None)
      } else Future.successful(Some(ObjectNotExists))
    }
  }

  def activation(idFirm: Int, idEnable: Boolean): OptionT[Future, Error] = OptionT{
    firmRepo.exists(idFirm) flatMap { exists =>
      if (exists) {
        db
          .run(firmDAO.query(idFirm).map(_.idEnable).update(idEnable))
          .map(count => if (count > 0) None else Some(UnknownError))
      } else Future.successful(Some(ObjectNotExists))
    }
  }

  // update postingPeriod
  // update accountingPeriodCode
}

