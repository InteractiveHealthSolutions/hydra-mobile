package ihsinformatics.com.hydra_mobile.data.remote.model.Reports

data class EncounterType(
    val description: String,
    val display: String,
    val name: String,
    val resourceVersion: String,
    val retired: Boolean,
    val uuid: String
)