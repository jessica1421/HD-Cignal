package errors

class ThrowableError(val error: Error) extends Throwable(error.toString) {
  def code: Int = error.code
  def reason: Option[String] = error.reason
}
