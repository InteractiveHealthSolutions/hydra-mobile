package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab

data class Patient(
    val display: String,
    val links: List<Link>,
    val uuid: String
)