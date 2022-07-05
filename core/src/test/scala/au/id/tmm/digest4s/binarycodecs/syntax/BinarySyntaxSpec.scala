package au.id.tmm.digest4s.binarycodecs.syntax

import au.id.tmm.digest4s.binarycodecs.BytesLike
import org.apache.commons.codec.DecoderException

import scala.collection.immutable.ArraySeq

class BinarySyntaxSpec extends AbstractBinaryCodecSyntaxSpec {

  override protected def encodingName: String = "binary"

  override protected def validEncodedString = "11011110101000011110000110101101"
  override protected def bytes              = new ArraySeq.ofByte(Array(0xad.toByte, 0xe1.toByte, 0xa1.toByte, 0xde.toByte))

  override protected def invalidEncodedString = "ZZZZ"

  override protected def useStringContext(string: String): ArraySeq.ofByte = binary"$string"
  override protected def useParseExtensionMethod(string: String): Either[DecoderException, ArraySeq.ofByte] =
    string.parseBinary
  override protected def useParseOrThrowExtensionMethod(string: String): ArraySeq.ofByte = string.parseBinaryOrThrow
  override protected def useEncodeExtensionMethod[B : BytesLike](bytes: B): String       = bytes.asBinaryString

}
