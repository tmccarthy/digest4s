package au.id.tmm.digest4s.digest

import java.io.{File, IOException, InputStream}
import java.nio.ByteBuffer
import java.nio.file.Path

import org.apache.commons.codec.digest.DigestUtils

import scala.collection.immutable.ArraySeq

trait SafeDigestible[-A] {
  protected[digest] def digest(digestUtils: DigestUtils, a: A): Array[Byte]
}

object SafeDigestible {
  @inline def apply[A : SafeDigestible]: SafeDigestible[A] = implicitly[SafeDigestible[A]]

  implicit val forArray: SafeDigestible[Array[Byte]] = (utils, array) => utils.digest(array)
  implicit val forUTF8String: SafeDigestible[String] = (utils, string) => utils.digest(string)
  implicit val forByteArray: SafeDigestible[ArraySeq[Byte]] = (utils, array) =>
    utils.digest(array.unsafeArray.asInstanceOf[Array[Byte]])
  implicit val forIterable: SafeDigestible[Iterable[Byte]] = (utils, iterable) => utils.digest(iterable.toArray)

  final class SafeDigestibleOps[-A : SafeDigestible] private[SafeDigestible] (a: A) {

    def md2: MD2Digest           = MD2Digest.digest(a)
    def md5: MD5Digest           = MD5Digest.digest(a)
    def sha1: SHA1Digest         = SHA1Digest.digest(a)
    def sha224: SHA224Digest     = SHA224Digest.digest(a)
    def sha256: SHA256Digest     = SHA256Digest.digest(a)
    def sha384: SHA384Digest     = SHA384Digest.digest(a)
    def sha512: SHA512Digest     = SHA512Digest.digest(a)
    def sha3_224: SHA3_224Digest = SHA3_224Digest.digest(a)
    def sha3_256: SHA3_256Digest = SHA3_256Digest.digest(a)
    def sha3_384: SHA3_384Digest = SHA3_384Digest.digest(a)
    def sha3_512: SHA3_512Digest = SHA3_512Digest.digest(a)

  }

  trait Syntax {
    implicit def toTmmUtilsCodecSafeDigestibleOps[A : SafeDigestible](a: A): SafeDigestibleOps[A] =
      new SafeDigestibleOps[A](a)
  }
}

trait UnsafeDigestible[-A] {
  protected def unsafeDigest(digestUtils: DigestUtils, a: A): Array[Byte]

  private[digest] def digest(digestUtils: DigestUtils, a: A): Either[IOException, Array[Byte]] =
    try Right(unsafeDigest(digestUtils, a))
    catch {
      case e: IOException => Left(e)
    }
}

object UnsafeDigestible {
  @inline def apply[A : UnsafeDigestible]: UnsafeDigestible[A] = implicitly[UnsafeDigestible[A]]

  implicit val forFile: UnsafeDigestible[File]               = (utils, file) => utils.digest(file)
  implicit val forPath: UnsafeDigestible[Path]               = (utils, path) => utils.digest(path.toFile)
  implicit val forInputStream: UnsafeDigestible[InputStream] = (utils, is) => utils.digest(is)
  implicit val forByteBuffer: UnsafeDigestible[ByteBuffer]   = (utils, buffer) => utils.digest(buffer)

  final class UnsafeDigestibleOps[-A : UnsafeDigestible] private[UnsafeDigestible] (a: A) {

    def md2OrError: Either[IOException, MD2Digest]           = MD2Digest.digestOrError(a)
    def md5OrError: Either[IOException, MD5Digest]           = MD5Digest.digestOrError(a)
    def sha1OrError: Either[IOException, SHA1Digest]         = SHA1Digest.digestOrError(a)
    def sha224OrError: Either[IOException, SHA224Digest]     = SHA224Digest.digestOrError(a)
    def sha256OrError: Either[IOException, SHA256Digest]     = SHA256Digest.digestOrError(a)
    def sha384OrError: Either[IOException, SHA384Digest]     = SHA384Digest.digestOrError(a)
    def sha512OrError: Either[IOException, SHA512Digest]     = SHA512Digest.digestOrError(a)
    def sha3_224OrError: Either[IOException, SHA3_224Digest] = SHA3_224Digest.digestOrError(a)
    def sha3_256OrError: Either[IOException, SHA3_256Digest] = SHA3_256Digest.digestOrError(a)
    def sha3_384OrError: Either[IOException, SHA3_384Digest] = SHA3_384Digest.digestOrError(a)
    def sha3_512OrError: Either[IOException, SHA3_512Digest] = SHA3_512Digest.digestOrError(a)

  }

  trait Syntax {
    implicit def toTmmUtilsCodecUnsafeDigestibleOps[A : UnsafeDigestible](a: A): UnsafeDigestibleOps[A] =
      new UnsafeDigestibleOps[A](a)
  }

}
