package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab

data class Encounter(
    val display: String,
    val links: List<Link>,
    val uuid: String
)