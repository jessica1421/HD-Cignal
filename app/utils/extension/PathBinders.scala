package utils.extension

import play.api.mvc.PathBindable

object PathBinders {
  implicit def bindableOption[T: PathBindable] = new PathBindable[Option[T]] {
    def bind(key: String, value: String): Either[String, Option[T]] =
      implicitly[PathBindable[T]]
        .bind(key, value)
        .fold(left => Left(left), right => Right(Some(right)))
    def unbind(key: String, value: Option[T]): String =
      value.map(_.toString).getOrElse("")
  }
}
