package ihsinformatics.com.hydra_mobile.network.model.user

data class Role(
    val description: String,
    val display: String,
    val inheritedRoles: List<InheritedRole>,
    val links: List<Link>,
    val name: String,
    val privileges: List<Privilege>,
    val resourceVersion: String,
    val retired: Boolean,
    val uuid: String
)