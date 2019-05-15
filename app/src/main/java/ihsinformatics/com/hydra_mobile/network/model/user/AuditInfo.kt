package ihsinformatics.com.hydra_mobile.network.model.user

data class AuditInfo(
    val changedBy: ChangedBy,
    val creator: Creator,
    val dateChanged: String,
    val dateCreated: String
)