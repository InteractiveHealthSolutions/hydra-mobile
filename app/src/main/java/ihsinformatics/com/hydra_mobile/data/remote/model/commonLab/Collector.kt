package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab

data class Collector(
    val attributes: List<Any>,
    val display: String,
    val identifier: String,
    val resourceVersion: String,
    val retired: Boolean,
    val uuid: String
)