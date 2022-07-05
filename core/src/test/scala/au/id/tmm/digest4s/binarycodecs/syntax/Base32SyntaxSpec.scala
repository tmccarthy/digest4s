package au.id.tmm.digest4s.binarycodecs.syntax

import au.id.tmm.digest4s.binarycodecs.BytesLike
import org.apache.commons.codec.DecoderException

import scala.collection.immutable.ArraySeq

class Base32SyntaxSpec extends AbstractBinaryCodecSyntaxSpec {

  override protected def encodingName: String = "base32"

  override protected def validEncodedString = "VXQ2DXQ="
  override protected def bytes              = new ArraySeq.ofByte(Array(0xad.toByte, 0xe1.toByte, 0xa1.toByte, 0xde.toByte))

  override protected def invalidEncodedString = "ابتث"

  override protected def useStringContext(string: String): ArraySeq.ofByte = base32"$string"
  override protected def useParseExtensionMethod(string: String): Either[DecoderException, ArraySeq.ofByte] =
    string.parseBase32
  override protected def useParseOrThrowExtensionMethod(string: String): ArraySeq.ofByte = string.parseBase32OrThrow
  override protected def useEncodeExtensionMethod[B : BytesLike](bytes: B): String       = bytes.asBase32String

}
