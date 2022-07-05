package au.id.tmm.digest4s

object syntax
    extends AnyRef
    with binarycodecs.syntax.BinaryCodecsStringContext.Syntax
    with binarycodecs.syntax.BytesLikeOps.Syntax
    with binarycodecs.syntax.StringOps.Syntax
    with digest.SafeDigestible.Syntax
    with digest.UnsafeDigestible.Syntax
