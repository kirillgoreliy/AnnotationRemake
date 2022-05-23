package spark_config

object MySqlDatabaseList extends Enumeration {
  type MySqlDatabaseList = Value
  val ensembl,
  ensembl_hg38,
  forome,
  hgmd_phenbase,
  hgmd_pro,
  hgmd_snp,
  hgmd_views = Value
}
