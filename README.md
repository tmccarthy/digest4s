# `digest4s`

[![Maven Central](https://img.shields.io/maven-central/v/au.id.tmm.digest4s/digest4s-core_2.13.svg)](https://repo.maven.apache.org/maven2/au/id/tmm/digest4s/digest4s-core_2.13/)

A set of utilities for generating digests and handling binary codecs in Scala. Built on top of [Apache Commons Codec](https://commons.apache.org/proper/commons-codec/).

## Getting started

Add the following to your `build.sbt` file. The latest version of the library should be in the Maven badge above. 
Otherwise you can checkout [the artefact listing on `mvnrepository.com`](https://mvnrepository.com/artifact/au.id.tmm.digest4s/digest4s-core).

```scala
libraryDependencies += "au.id.tmm.digest4s" %% "digest4s-core" % "0.1.0"
```

The simplest way to use the library is with the syntax import:

```scala
import au.id.tmm.digest4s.syntax._
```

## Binary codec utilities

### Parsing bytes from encoded strings

```scala
import au.id.tmm.digest4s.binarycodecs.syntax._

import org.apache.commons.codec.DecoderException
import scala.collection.immutable.ArraySeq

// Parse binary encoded Strings safely

val byteArray: Either[DecoderException, ArraySeq[Byte]] = "VXQ2DXQ=".parseBase32
val byteArray: Either[DecoderException, ArraySeq[Byte]] = "reGh3g==".parseBase64
val byteArray: Either[DecoderException, ArraySeq[Byte]] = "11011110101000011110000110101101".parseBinary
val byteArray: Either[DecoderException, ArraySeq[Byte]] = "ADE1A1DE".parseHex

// Parse unsafely, throw if encoding is invalid:

val byteArray: ArraySeq[Byte] = "VXQ2DXQ=".parseBase32OrThrow
val byteArray: ArraySeq[Byte] = "reGh3g==".parseBase64OrThrow
val byteArray: ArraySeq[Byte] = "11011110101000011110000110101101".parseBinaryOrThrow
val byteArray: ArraySeq[Byte] = "ADE1A1DE".parseHexOrThrow

// Use StringContext (throws if invalid)

val byteArray: ArraySeq[Byte] = base32"VXQ2DXQ="
val byteArray: ArraySeq[Byte] = base64"reGh3g=="
val byteArray: ArraySeq[Byte] = binary"11011110101000011110000110101101"
val byteArray: ArraySeq[Byte] = hex"ADE1A1DE"
```

### Encoding bytes into string representations

```scala
import au.id.tmm.digest4s.binarycodecs.syntax._

import scala.collection.immutable.ArraySeq

// Encode bytes to String

val bytes: Array[Byte] = Array[Byte](0xad.toByte, 0xe1.toByte, 0xa1.toByte, 0xde.toByte)

bytes.asBase32String // "VXQ2DXQ="
bytes.asBase64String // "reGh3g=="
bytes.asBinaryString // "11011110101000011110000110101101"
bytes.asHexString    // "ADE1A1DE"

// Can be used for different collection types

bytes.to(ArraySeq).asHexString // "ADE1A1DE"
bytes.toVector.asHexString     // "ADE1A1DE"
bytes.toList.asHexString       // "ADE1A1DE"
```

## Digest utilities

### Digest value classes

There are a number of value classes in the `au.id.tmm.digest4s.digest` package which can improve the semantic clarity of
checksum fields:

```scala
import au.id.tmm.digest4s.digest._

case class Document(
  checksum: Array[Byte], // 🤷 Bad, not clear what the algorithm is
)

case class Document(
  checksum: MD5Digest,   // 😎 Clearer, describes the algorithm
)
```

### Computing digests

```scala
import java.io.IOException

import au.id.tmm.digest4s.digest._
import au.id.tmm.digest4s.digest.syntax._

// Compute digests for safe types

val md5Digest: MD5Digest       = "Hello".md5
val sha256Digest: SHA256Digest = Array[Byte](0xad.toByte, 0xe1.toByte, 0xa1.toByte, 0xde.toByte).sha256
val sha512Digest: SHA512Digest = List[Byte](0xad.toByte, 0xe1.toByte, 0xa1.toByte, 0xde.toByte).sha512

// Compute digests for types where you might hit an IOException:

val md5Digest: Either[IOException, MD5Digest] = java.nio.file.Paths.get("test").md5OrError
```
