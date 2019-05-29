package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class User(
    val username: String,
    val fullName: String,
    val roles: String,
    val identifier: String,
    val personUuid: String,
    val privileges: String,
    val systemId: String,
    val password: String,
    val uuid: String,
    val providerUuid: String,
    val sessionId: String,
    val labUUID: String,
    val voided: Boolean

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}