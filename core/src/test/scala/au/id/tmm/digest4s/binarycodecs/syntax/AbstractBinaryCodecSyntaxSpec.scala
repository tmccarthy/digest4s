package au.id.tmm.digest4s.binarycodecs.syntax

import au.id.tmm.digest4s.binarycodecs.BytesLike
import munit.FunSuite
import org.apache.commons.codec.DecoderException

import scala.collection.immutable.ArraySeq

abstract class AbstractBinaryCodecSyntaxSpec extends FunSuite {

  protected def encodingName: String

  protected def validEncodedString: String
  protected def bytes: ArraySeq.ofByte

  protected def invalidEncodedString: String

  protected def useStringContext(string: String): ArraySeq.ofByte
  protected def useParseExtensionMethod(string: String): Either[DecoderException, ArraySeq.ofByte]
  protected def useParseOrThrowExtensionMethod(string: String): ArraySeq.ofByte
  protected def useEncodeExtensionMethod[B : BytesLike](bytes: B): String

  test(s"the $encodingName string context should convert some $encodingName to a byte array") {
    assertEquals(useStringContext(validEncodedString), bytes)
  }

  test(s"the $encodingName string context should throw if invalid $encodingName is provided") {
    intercept[DecoderException](useStringContext(invalidEncodedString))
  }

  test(s"the $encodingName string ops should parse a byte array from a $encodingName string") {
    assertEquals(useParseExtensionMethod(validEncodedString), Right(bytes))
  }

  test(s"the $encodingName string ops should fail to parse an invalid $encodingName string") {
    assertEquals(useParseExtensionMethod(invalidEncodedString).left.map(_.getClass), Left(classOf[DecoderException]))
  }

  test(s"the $encodingName string ops should parse a byte array from a $encodingName string using parseOrThrow") {
    assertEquals(useParseExtensionMethod(validEncodedString), Right(bytes))
  }

  test(s"the $encodingName string ops should throw if parsing an invalid $encodingName string with parseOrThrow") {
    intercept[DecoderException](useParseOrThrowExtensionMethod(invalidEncodedString))
  }

  test(s"the $encodingName iterable ops should encode a ByteArray to $encodingName") {
    assertEquals(useEncodeExtensionMethod(bytes), validEncodedString)
  }

  test(s"the $encodingName iterable ops should encode a list of bytes to $encodingName") {
    assertEquals(useEncodeExtensionMethod(bytes.toList), validEncodedString)
  }

  test(s"the $encodingName array ops should encode an array to $encodingName") {
    assertEquals(useEncodeExtensionMethod(bytes.unsafeArray), validEncodedString)
  }

}
