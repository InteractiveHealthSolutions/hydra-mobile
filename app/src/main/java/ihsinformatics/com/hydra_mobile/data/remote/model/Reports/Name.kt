package ihsinformatics.com.hydra_mobile.data.remote.model.Reports

data class Name(
    val conceptNameType: String,
    val display: String,
    val locale: String,
    val localePreferred: Boolean,
    val name: String,
    val resourceVersion: String,
    val uuid: String
)