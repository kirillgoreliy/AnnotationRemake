package spark_config

import com.fasterxml.jackson.annotation.JsonValue

abstract class AstorageResponseScheme {
  val chrom:String
  val pos: Int
  val last: Int
  val hg19: String
//  @JsonValue("hg19-last")
  val hg19last:String
  val hg38:String
//  @JsonValue("hg38-last")
  val hg38last:String

  override def toString = s"AstorageResponseScheme($chrom, $pos, $last, $hg19, $hg19last, $hg38, $hg38last)"
}
