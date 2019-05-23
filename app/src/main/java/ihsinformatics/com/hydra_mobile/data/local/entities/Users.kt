package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class Users(
    val username: String,
    val fullName: String,
    val roles: String,
    val identifier: String,
    val personUuid: String,
    val privileges: String
    //Todo:add uuid

) : AbstractModel() {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}