package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab

data class OrderType(
    val conceptClasses: List<Any>,
    val description: String,
    val display: String,
    val javaClassName: String,
    val links: List<Link>,
    val name: String,
    val parent: Any,
    val resourceVersion: String,
    val retired: Boolean,
    val uuid: String
)