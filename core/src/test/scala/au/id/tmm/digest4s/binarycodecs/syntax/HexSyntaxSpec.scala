package au.id.tmm.digest4s.binarycodecs.syntax

import au.id.tmm.digest4s.binarycodecs.BytesLike
import org.apache.commons.codec.DecoderException

import scala.collection.immutable.ArraySeq

class HexSyntaxSpec extends AbstractBinaryCodecSyntaxSpec {

  override protected def encodingName: String = "hex"

  override protected def validEncodedString = "ADE1A1DE"
  override protected def bytes              = new ArraySeq.ofByte(Array(0xad.toByte, 0xe1.toByte, 0xa1.toByte, 0xde.toByte))

  override protected def invalidEncodedString = "ZZZZ"

  override protected def useStringContext(string: String): ArraySeq.ofByte = hex"$string"
  override protected def useParseExtensionMethod(string: String): Either[DecoderException, ArraySeq.ofByte] =
    string.parseHex
  override protected def useParseOrThrowExtensionMethod(string: String): ArraySeq.ofByte = string.parseHexOrThrow
  override protected def useEncodeExtensionMethod[B : BytesLike](bytes: B): String       = bytes.asHexString.toUpperCase

}
