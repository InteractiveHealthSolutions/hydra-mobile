package ihsinformatics.com.hydra_mobile.data.remote.model.user

data class AllRole(
    val description: String,
    val display: String,
    val inheritedRoles: List<Any>,
    val links: List<Link>,
    val name: String,
    val privileges: List<Privilege>,
    val resourceVersion: String,
    val retired: Boolean,
    val uuid: String
)