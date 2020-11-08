package au.id.tmm.digest4s

object syntax
    extends AnyRef
    with binarycodecs.syntax.BinaryCodecsStringContext.Syntax
    with binarycodecs.syntax.BytesLikeOps.Syntax
    with binarycodecs.syntax.StringOps.Syntax
    with digest.syntax.SafeDigestibleOps.Syntax
    with digest.syntax.UnsafeDigestibleOps.Syntax
