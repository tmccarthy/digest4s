package au.id.tmm.digest4s.digest.syntax

import java.io.{ByteArrayInputStream, File, IOException, InputStream}
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}

import au.id.tmm.digest4s.binarycodecs.BytesLike
import au.id.tmm.digest4s.binarycodecs.syntax._
import au.id.tmm.digest4s.digest._
import au.id.tmm.digest4s.digest.syntax.DigestSyntaxSpec.{
  DigestTestDimension,
  SafeDigestibleTestDimension,
  UnsafeDigestibleTestDimension,
}
import munit.{FunSuite, Location}

import scala.collection.immutable.ArraySeq
import scala.reflect.ClassTag

class DigestSyntaxSpec extends FunSuite {

  DigestTestDimension.all.foreach { digestTestDimension =>
    if (digestTestDimension.javaVersionCompatible) {
      SafeDigestibleTestDimension.all.foreach { digestibleTestDimension =>
        safeDigestTests(digestTestDimension, digestibleTestDimension)
      }
      UnsafeDigestibleTestDimension.all.foreach { digestibleTestDimension =>
        unsafeDigestTests(digestTestDimension, digestibleTestDimension)
      }
    }
  }

  private def safeDigestTests[D, A](
    digestTestDimension: DigestTestDimension[D],
    digestibleTestDimension: SafeDigestibleTestDimension[A],
  )(implicit
    loc: Location,
  ): Unit = {
    implicit val safeDigestableInstance: SafeDigestible[A] = digestibleTestDimension.safeDigestible
    implicit val bytesLikeInstance: BytesLike[D]           = digestTestDimension.bytesLike

    digestTestDimension.tests.foreach { case (bytes, expectedDigest) =>
      val a: A = digestibleTestDimension.fromBytes(bytes)

      test(
        s"${digestTestDimension.classTag.runtimeClass.getSimpleName} for ${digestibleTestDimension.name} (${bytes.asHexString} -> ${expectedDigest.asHexString})",
      ) {
        val obtained = digestTestDimension.safe(a)
        assertEquals(obtained, expectedDigest, obtained.asHexString)
      }
    }
  }

  private def unsafeDigestTests[D, A](
    digestTestDimension: DigestTestDimension[D],
    digestibleTestDimension: UnsafeDigestibleTestDimension[A],
  )(implicit
    loc: Location,
  ): Unit =
    digestTestDimension.tests.foreach { case (bytes, expectedDigest) =>
      implicit val safeDigestableInstance: UnsafeDigestible[A] = digestibleTestDimension.unsafeDigestible
      implicit val bytesLikeInstance: BytesLike[D]             = digestTestDimension.bytesLike

      val a: A = digestibleTestDimension.fromBytes(bytes)

      test(
        s"${digestTestDimension.classTag.runtimeClass.getSimpleName} for ${digestibleTestDimension.classTag.runtimeClass.getSimpleName} (${bytes.asHexString} -> ${expectedDigest.asHexString})",
      ) {
        val obtained = digestTestDimension.unsafe(a)
        assertEquals(obtained, Right(expectedDigest), obtained.map(_.asHexString))
      }
    }

}

object DigestSyntaxSpec {

  private val testBytes = "hello world".getBytes

  sealed abstract class DigestTestDimension[D : BytesLike : ClassTag](
    firstTest: (Array[Byte], D),
    otherTests: (Array[Byte], D)*,
  ) {
    def unsafe[A : UnsafeDigestible](a: A): Either[IOException, D]
    def safe[A : SafeDigestible](a: A): D
    def tests: List[(Array[Byte], D)] = firstTest :: otherTests.toList
    def classTag: ClassTag[D]         = implicitly
    def bytesLike: BytesLike[D]       = implicitly

    def javaVersionCompatible: Boolean = true
  }

  object DigestTestDimension {
    case object ForMD2
        extends DigestTestDimension[MD2Digest](
          testBytes -> MD2Digest(hex"d9cce882ee690a5c1ce70beff3a78c77"),
        ) {
      override def unsafe[A : UnsafeDigestible](a: A): Either[IOException, MD2Digest] = a.md2OrError
      override def safe[A : SafeDigestible](a: A): MD2Digest                          = a.md2
    }
    case object ForMD5
        extends DigestTestDimension[MD5Digest](
          testBytes -> MD5Digest(hex"5eb63bbbe01eeed093cb22bb8f5acdc3"),
        ) {
      override def unsafe[A : UnsafeDigestible](a: A): Either[IOException, MD5Digest] = a.md5OrError
      override def safe[A : SafeDigestible](a: A): MD5Digest                          = a.md5
    }
    case object ForSHA1
        extends DigestTestDimension[SHA1Digest](
          testBytes -> SHA1Digest(hex"2aae6c35c94fcfb415dbe95f408b9ce91ee846ed"),
        ) {
      override def unsafe[A : UnsafeDigestible](a: A): Either[IOException, SHA1Digest] = a.sha1OrError
      override def safe[A : SafeDigestible](a: A): SHA1Digest                          = a.sha1
    }
    case object ForSHA224
        extends DigestTestDimension[SHA224Digest](
          testBytes -> SHA224Digest(hex"2f05477fc24bb4faefd86517156dafdecec45b8ad3cf2522a563582b"),
        ) {
      override def unsafe[A : UnsafeDigestible](a: A): Either[IOException, SHA224Digest] = a.sha224OrError
      override def safe[A : SafeDigestible](a: A): SHA224Digest                          = a.sha224
    }
    case object ForSHA256
        extends DigestTestDimension[SHA256Digest](
          testBytes -> SHA256Digest(hex"b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9"),
        ) {
      override def unsafe[A : UnsafeDigestible](a: A): Either[IOException, SHA256Digest] = a.sha256OrError
      override def safe[A : SafeDigestible](a: A): SHA256Digest                          = a.sha256
    }
    case object ForSHA384
        extends DigestTestDimension[SHA384Digest](
          testBytes -> SHA384Digest(
            hex"83bff28dde1b1bf5810071c6643c08e5b05bdb836effd70b403ea8ea0a634dc4997eb1053aa3593f590f9c63630dd90b",
          ),
        ) {
      override def unsafe[A : UnsafeDigestible](a: A): Either[IOException, SHA384Digest] = a.sha384OrError
      override def safe[A : SafeDigestible](a: A): SHA384Digest                          = a.sha384
    }
    case object ForSHA512
        extends DigestTestDimension[SHA512Digest](
          testBytes -> SHA512Digest(
            hex"309ecc489c12d6eb4cc40f50c902f2b4d0ed77ee511a7c7a9bcd3ca86d4cd86f989dd35bc5ff499670da34255b45b0cfd830e81f605dcf7dc5542e93ae9cd76f",
          ),
        ) {
      override def unsafe[A : UnsafeDigestible](a: A): Either[IOException, SHA512Digest] = a.sha512OrError
      override def safe[A : SafeDigestible](a: A): SHA512Digest                          = a.sha512
    }
    case object ForSHA3_224
        extends DigestTestDimension[SHA3_224Digest](
          testBytes -> SHA3_224Digest(hex"dfb7f18c77e928bb56faeb2da27291bd790bc1045cde45f3210bb6c5"),
        ) {
      override def unsafe[A : UnsafeDigestible](a: A): Either[IOException, SHA3_224Digest] = a.sha3_224OrError
      override def safe[A : SafeDigestible](a: A): SHA3_224Digest                          = a.sha3_224
      override def javaVersionCompatible: Boolean                                          = !isJava8
    }
    case object ForSHA3_256
        extends DigestTestDimension[SHA3_256Digest](
          testBytes -> SHA3_256Digest(hex"644bcc7e564373040999aac89e7622f3ca71fba1d972fd94a31c3bfbf24e3938"),
        ) {
      override def unsafe[A : UnsafeDigestible](a: A): Either[IOException, SHA3_256Digest] = a.sha3_256OrError
      override def safe[A : SafeDigestible](a: A): SHA3_256Digest                          = a.sha3_256
      override def javaVersionCompatible: Boolean                                          = !isJava8
    }
    case object ForSHA3_384
        extends DigestTestDimension[SHA3_384Digest](
          testBytes -> SHA3_384Digest(
            hex"83bff28dde1b1bf5810071c6643c08e5b05bdb836effd70b403ea8ea0a634dc4997eb1053aa3593f590f9c63630dd90b",
          ),
        ) {
      override def unsafe[A : UnsafeDigestible](a: A): Either[IOException, SHA3_384Digest] = a.sha3_384OrError
      override def safe[A : SafeDigestible](a: A): SHA3_384Digest                          = a.sha3_384
      override def javaVersionCompatible: Boolean                                          = !isJava8
    }
    case object ForSHA3_512
        extends DigestTestDimension[SHA3_512Digest](
          testBytes -> SHA3_512Digest(
            hex"840006653e9ac9e95117a15c915caab81662918e925de9e004f774ff82d7079a40d4d27b1b372657c61d46d470304c88c788b3a4527ad074d1dccbee5dbaa99a",
          ),
        ) {
      override def unsafe[A : UnsafeDigestible](a: A): Either[IOException, SHA3_512Digest] = a.sha3_512OrError
      override def safe[A : SafeDigestible](a: A): SHA3_512Digest                          = a.sha3_512
      override def javaVersionCompatible: Boolean                                          = !isJava8
    }

    val all: List[DigestTestDimension[_]] = List(
      ForMD2,
      ForMD5,
      ForSHA1,
      ForSHA224,
      ForSHA256,
      ForSHA384,
      ForSHA512,
      ForSHA3_224,
      ForSHA3_256,
      ForSHA3_384,
      ForSHA3_512,
    )
  }

  final class SafeDigestibleTestDimension[A : SafeDigestible](val name: String, val fromBytes: Array[Byte] => A) {
    val safeDigestible: SafeDigestible[A] = implicitly
  }

  object SafeDigestibleTestDimension {
    def named[A : SafeDigestible](name: String, fromBytes: Array[Byte] => A): SafeDigestibleTestDimension[A] =
      new SafeDigestibleTestDimension(name, fromBytes)

    def apply[A : SafeDigestible : ClassTag](fromBytes: Array[Byte] => A): SafeDigestibleTestDimension[A] =
      new SafeDigestibleTestDimension[A](implicitly[ClassTag[A]].runtimeClass.getSimpleName, fromBytes)

    val ForString: SafeDigestibleTestDimension[String] =
      SafeDigestibleTestDimension[String](b => new String(b, StandardCharsets.UTF_8))
    val ForBytes: SafeDigestibleTestDimension[Array[Byte]] =
      SafeDigestibleTestDimension[Array[Byte]](b => b)
    val ForArraySeq: SafeDigestibleTestDimension[ArraySeq[Byte]] =
      SafeDigestibleTestDimension
        .named[ArraySeq[Byte]]("ArraySeq.unsafeWrapArray[Byte]", b => ArraySeq.unsafeWrapArray(b))
    val ForArraySeqOfByte: SafeDigestibleTestDimension[ArraySeq.ofByte] =
      SafeDigestibleTestDimension[ArraySeq.ofByte](b => new ArraySeq.ofByte(b))
    val ForUntaggedArraySeq: SafeDigestibleTestDimension[ArraySeq[Byte]] =
      SafeDigestibleTestDimension
        .named[ArraySeq[Byte]]("ArraySeq.untagged[Byte]", b => ArraySeq.untagged(b.toIndexedSeq: _*))
    val ForArraySeqOfBoxed: SafeDigestibleTestDimension[ArraySeq[Byte]] =
      SafeDigestibleTestDimension.named[ArraySeq[Byte]](
        "ArraySeqOfBoxed",
        b => {
          val builder = ArraySeq.untagged.newBuilder[Any]
          b.foreach(builder.addOne)
          builder.result().asInstanceOf[ArraySeq[Byte]]
        },
      )
    val ForByteVector: SafeDigestibleTestDimension[Vector[Byte]] =
      SafeDigestibleTestDimension[Vector[Byte]](b => b.toVector)

    val all: List[SafeDigestibleTestDimension[_]] = List(
      ForString,
      ForBytes,
      ForArraySeq,
      ForArraySeqOfByte,
      ForUntaggedArraySeq,
      ForByteVector,
    )
  }

  final class UnsafeDigestibleTestDimension[A : UnsafeDigestible : ClassTag](val fromBytes: Array[Byte] => A) {
    def classTag: ClassTag[A]                 = implicitly
    val unsafeDigestible: UnsafeDigestible[A] = implicitly
  }

  object UnsafeDigestibleTestDimension {
    val ForPath = new UnsafeDigestibleTestDimension[Path](b =>
      Files.write(Files.createTempFile(getClass.getName, "txt"), b),
    )
    val ForFile        = new UnsafeDigestibleTestDimension[File](b => ForPath.fromBytes(b).toFile)
    val ForInputStream = new UnsafeDigestibleTestDimension[InputStream](new ByteArrayInputStream(_))
    val ForByteBuffer  = new UnsafeDigestibleTestDimension[ByteBuffer](ByteBuffer.wrap)

    val all: List[UnsafeDigestibleTestDimension[_]] = List(
      ForPath,
      ForFile,
      ForInputStream,
      ForByteBuffer,
    )
  }

  private def isJava8: Boolean = System.getProperty("java.specification.version") == "1.8"

}
