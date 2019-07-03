package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class User(
    val username: String,
    @Embedded
    val roles: Role?,
    val systemId: String,
    val uuid: String
) {
    constructor(
        username: String,
        fullName: String,
        roles: Role,
        password: String,
        personUuid: String,
        sessionId: String,
        labUUID: String,
        identifier: String,
        privileges: String,
        systemId: String,
        uuid: String,
        providerUuid: String,
        voided: Boolean

    ) : this(username, roles, systemId, uuid) {

    }

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}