package ihsinformatics.com.hydra_mobile.network.model.user

data class Person(
    val age: Any,
    val attributes: List<Any>,
    val birthdate: Any,
    val birthdateEstimated: Boolean,
    val birthtime: Any,
    val causeOfDeath: Any,
    val dead: Boolean,
    val deathDate: Any,
    val deathdateEstimated: Boolean,
    val display: String,
    val gender: String,
    val links: List<Link>,
    val preferredAddress: Any,
    val preferredName: PreferredName,
    val resourceVersion: String,
    val uuid: String,
    val voided: Boolean
)