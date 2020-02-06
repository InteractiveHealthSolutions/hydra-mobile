package ihsinformatics.com.hydra_mobile.data.remote.model.Reports

data class Value(
    val answers: List<Any>,
    val attributes: List<Any>,
    val conceptClass: ConceptClass,
    val datatype: Datatype,
    val descriptions: List<Description>,
    val display: String,
    val mappings: List<Mapping>,
    val name: Name,
    val names: List<NameX>,
    val resourceVersion: String,
    val retired: Boolean,
    val `set`: Boolean,
    val setMembers: List<Any>,
    val uuid: String,
    val version: Any
)