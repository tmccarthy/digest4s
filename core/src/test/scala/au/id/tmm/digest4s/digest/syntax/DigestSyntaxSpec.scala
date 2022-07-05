package au.id.tmm.digest4s.digest.syntax

import java.io.{ByteArrayInputStream, InputStream}
import java.nio.ByteBuffer
import java.nio.file.Files

import au.id.tmm.digest4s.binarycodecs.syntax._
import au.id.tmm.digest4s.digest._
import munit.FunSuite

import scala.collection.immutable.ArraySeq

class DigestSyntaxSpec extends FunSuite {

  private val string     = "hello world"
  private def array      = "hello world".getBytes
  private val byteArray  = ArraySeq.unsafeWrapArray(array)
  private val byteVector = array.toVector

  private def byteBuffer               = ByteBuffer.wrap(array)
  private def inputStream: InputStream = new ByteArrayInputStream(array)
  private def path                     = Files.write(Files.createTempFile(getClass.getName, "txt"), array)
  private def file                     = path.toFile

  private val expectedMd5Hash = MD5Digest(hex"5eb63bbbe01eeed093cb22bb8f5acdc3")

  test("The syntax to compute a MD5 hash should work for a string") {
    assertEquals(string.md5, expectedMd5Hash)
  }
  test("The syntax to compute a MD5 hash should work for a array") {
    assertEquals(array.md5, expectedMd5Hash)
  }
  test("The syntax to compute a MD5 hash should work for a byteArray") {
    assertEquals(byteArray.md5, expectedMd5Hash)
  }
  test("The syntax to compute a MD5 hash should work for a byteVector") {
    assertEquals(byteVector.md5, expectedMd5Hash)
  }
  test("The syntax to compute a MD5 hash should work for a byteBuffer") {
    assertEquals(byteBuffer.md5OrError, Right(expectedMd5Hash))
  }
  test("The syntax to compute a MD5 hash should work for a inputStream") {
    assertEquals(inputStream.md5OrError, Right(expectedMd5Hash))
  }
  test("The syntax to compute a MD5 hash should work for a path") {
    assertEquals(path.md5OrError, Right(expectedMd5Hash))
  }
  test("The syntax to compute a MD5 hash should work for a file") {
    assertEquals(file.md5OrError, Right(expectedMd5Hash))
  }

  private val expectedSha1Hash = SHA1Digest(hex"2aae6c35c94fcfb415dbe95f408b9ce91ee846ed")

  test("The syntax to compute a SHA1 hash should work for a string") {
    assertEquals(string.sha1, expectedSha1Hash)
  }
  test("The syntax to compute a SHA1 hash should work for a array") {
    assertEquals(array.sha1, expectedSha1Hash)
  }
  test("The syntax to compute a SHA1 hash should work for a byteArray") {
    assertEquals(byteArray.sha1, expectedSha1Hash)
  }
  test("The syntax to compute a SHA1 hash should work for a byteVector") {
    assertEquals(byteVector.sha1, expectedSha1Hash)
  }
  test("The syntax to compute a SHA1 hash should work for a byteBuffer") {
    assertEquals(byteBuffer.sha1OrError, Right(expectedSha1Hash))
  }
  test("The syntax to compute a SHA1 hash should work for a inputStream") {
    assertEquals(inputStream.sha1OrError, Right(expectedSha1Hash))
  }
  test("The syntax to compute a SHA1 hash should work for a path") {
    assertEquals(path.sha1OrError, Right(expectedSha1Hash))
  }
  test("The syntax to compute a SHA1 hash should work for a file") {
    assertEquals(file.sha1OrError, Right(expectedSha1Hash))
  }

  private val expectedSha256Hash = SHA256Digest(hex"b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9")

  test("The syntax to compute a SHA256 hash should work for a string") {
    assertEquals(string.sha256, expectedSha256Hash)
  }
  test("The syntax to compute a SHA256 hash should work for a array") {
    assertEquals(array.sha256, expectedSha256Hash)
  }
  test("The syntax to compute a SHA256 hash should work for a byteArray") {
    assertEquals(byteArray.sha256, expectedSha256Hash)
  }
  test("The syntax to compute a SHA256 hash should work for a byteVector") {
    assertEquals(byteVector.sha256, expectedSha256Hash)
  }
  test("The syntax to compute a SHA256 hash should work for a byteBuffer") {
    assertEquals(byteBuffer.sha256OrError, Right(expectedSha256Hash))
  }
  test("The syntax to compute a SHA256 hash should work for a inputStream") {
    assertEquals(inputStream.sha256OrError, Right(expectedSha256Hash))
  }
  test("The syntax to compute a SHA256 hash should work for a path") {
    assertEquals(path.sha256OrError, Right(expectedSha256Hash))
  }
  test("The syntax to compute a SHA256 hash should work for a file") {
    assertEquals(file.sha256OrError, Right(expectedSha256Hash))
  }

  private val expectedSha512Hash =
    SHA512Digest(
      hex"309ecc489c12d6eb4cc40f50c902f2b4d0ed77ee511a7c7a9bcd3ca86d4cd86f989dd35bc5ff499670da34255b45b0cfd830e81f605dcf7dc5542e93ae9cd76f",
    )

  test("The syntax to compute a SHA512 hash should work for a string") {
    assertEquals(string.sha512, expectedSha512Hash)
  }
  test("The syntax to compute a SHA512 hash should work for a array") {
    assertEquals(array.sha512, expectedSha512Hash)
  }
  test("The syntax to compute a SHA512 hash should work for a byteArray") {
    assertEquals(byteArray.sha512, expectedSha512Hash)
  }
  test("The syntax to compute a SHA512 hash should work for a byteVector") {
    assertEquals(byteVector.sha512, expectedSha512Hash)
  }
  test("The syntax to compute a SHA512 hash should work for a byteBuffer") {
    assertEquals(byteBuffer.sha512OrError, Right(expectedSha512Hash))
  }
  test("The syntax to compute a SHA512 hash should work for a inputStream") {
    assertEquals(inputStream.sha512OrError, Right(expectedSha512Hash))
  }
  test("The syntax to compute a SHA512 hash should work for a path") {
    assertEquals(path.sha512OrError, Right(expectedSha512Hash))
  }
  test("The syntax to compute a SHA512 hash should work for a file") {
    assertEquals(file.sha512OrError, Right(expectedSha512Hash))
  }

  if (System.getProperty("java.specification.version") != "1.8") {

    val expectedSha3_256Hash = SHA3_256Digest(
      hex"644bcc7e564373040999aac89e7622f3ca71fba1d972fd94a31c3bfbf24e3938",
    )

    test("The syntax to compute a SHA3_256 hash should work for a string") {
      assertEquals(string.sha3_256, expectedSha3_256Hash)
    }
    test("The syntax to compute a SHA3_256 hash should work for a array") {
      assertEquals(array.sha3_256, expectedSha3_256Hash)
    }
    test("The syntax to compute a SHA3_256 hash should work for a byteArray") {
      assertEquals(byteArray.sha3_256, expectedSha3_256Hash)
    }
    test("The syntax to compute a SHA3_256 hash should work for a byteVector") {
      assertEquals(byteVector.sha3_256, expectedSha3_256Hash)
    }
    test("The syntax to compute a SHA3_256 hash should work for a byteBuffer") {
      assertEquals(byteBuffer.sha3_256OrError, Right(expectedSha3_256Hash))
    }
    test("The syntax to compute a SHA3_256 hash should work for a inputStream") {
      assertEquals(inputStream.sha3_256OrError, Right(expectedSha3_256Hash))
    }
    test("The syntax to compute a SHA3_256 hash should work for a path") {
      assertEquals(path.sha3_256OrError, Right(expectedSha3_256Hash))
    }
    test("The syntax to compute a SHA3_256 hash should work for a file") {
      assertEquals(file.sha3_256OrError, Right(expectedSha3_256Hash))
    }

    val expectedSha3_512Hash =
      SHA3_512Digest(
        hex"840006653e9ac9e95117a15c915caab81662918e925de9e004f774ff82d7079a40d4d27b1b372657c61d46d470304c88c788b3a4527ad074d1dccbee5dbaa99a",
      )

    test("The syntax to compute a SHA3_512 hash should work for a string") {
      assertEquals(string.sha3_512, expectedSha3_512Hash)
    }
    test("The syntax to compute a SHA3_512 hash should work for a array") {
      assertEquals(array.sha3_512, expectedSha3_512Hash)
    }
    test("The syntax to compute a SHA3_512 hash should work for a byteArray") {
      assertEquals(byteArray.sha3_512, expectedSha3_512Hash)
    }
    test("The syntax to compute a SHA3_512 hash should work for a byteVector") {
      assertEquals(byteVector.sha3_512, expectedSha3_512Hash)
    }
    test("The syntax to compute a SHA3_512 hash should work for a byteBuffer") {
      assertEquals(byteBuffer.sha3_512OrError, Right(expectedSha3_512Hash))
    }
    test("The syntax to compute a SHA3_512 hash should work for a inputStream") {
      assertEquals(inputStream.sha3_512OrError, Right(expectedSha3_512Hash))
    }
    test("The syntax to compute a SHA3_512 hash should work for a path") {
      assertEquals(path.sha3_512OrError, Right(expectedSha3_512Hash))
    }
    test("The syntax to compute a SHA3_512 hash should work for a file") {
      assertEquals(file.sha3_512OrError, Right(expectedSha3_512Hash))
    }

  }
}
