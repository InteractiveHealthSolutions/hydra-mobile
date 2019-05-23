package ihsinformatics.com.hydra_mobile.data.remote.model.user

data class Result(
    val allRoles: List<AllRole>,
    val auditInfo: AuditInfo,
    val display: String,
    val links: List<Link>,
    val person: Person,
    val privileges: List<Privilege>,
    val proficientLocales: List<Any>,
    val resourceVersion: String,
    val retired: Boolean,
    val roles: List<Role>,
    val systemId: String,
    val userProperties: UserProperties,
    val username: String,
    val uuid: String
)