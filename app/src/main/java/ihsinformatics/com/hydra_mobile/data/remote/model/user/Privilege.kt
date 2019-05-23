package ihsinformatics.com.hydra_mobile.data.remote.model.user

data class Privilege(
    val display: String,
    val links: List<Link>,
    val uuid: String,
    val description: String,
    val name: String,
    val resourceVersion: String,
    val retired: Boolean

)