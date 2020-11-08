# `digest4s`

[![CircleCI](https://circleci.com/gh/tmccarthy/digest4s/tree/main.svg?style=shield)](https://circleci.com/gh/tmccarthy/digest4s/tree/main)
[![Maven Central](https://img.shields.io/maven-central/v/au.id.tmm.digest4s/digest4s-core_2.13.svg)](https://repo.maven.apache.org/maven2/au/id/tmm/digest4s/digest4s-core_2.13/)

A set of utilities for generating digests and handling binary codecs in Scala. Built on top of [Apache Commons Codec](https://commons.apache.org/proper/commons-codec/).

## Getting started

Add the following to your `build.sbt` file. The latest version of the library should be in the Maven badge above. 
Otherwise you can checkout [the artefact listing on `mvnrepository.com`](https://mvnrepository.com/artifact/au.id.tmm.digest4s/digest4s-core).

```scala
libraryDependencies += "au.id.tmm.tmm-digest4s" %% "digest4s-core" % "0.0.1"
```

The simplest way to use the library is with the syntax import:

```scala
import au.id.tmm.digest4s.syntax._
```

## Binary codec utilities

### Parsing bytes from encoded strings

### Encoding bytes into string representations

## Digest utilities

### Digest value classes

### Computing digests
