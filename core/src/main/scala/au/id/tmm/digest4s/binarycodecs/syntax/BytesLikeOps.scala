package au.id.tmm.digest4s.binarycodecs.syntax

import au.id.tmm.digest4s.binarycodecs._

final class BytesLikeOps[B : BytesLike] private (b: B) {

  def asBase32String: String = Base32.asBase32String(b)

  def asBase64String: String = Base64.asBase64String(b)

  def asBinaryString: String = Binary.asBinaryString(b)

  def asHexString: String = Hex.asHexString(b)

}

object BytesLikeOps {

  trait Syntax {
    implicit def toTmmUtilsCodecBytesLikeOps[A : BytesLike](a: A): BytesLikeOps[A] = new BytesLikeOps[A](a)
  }

}
