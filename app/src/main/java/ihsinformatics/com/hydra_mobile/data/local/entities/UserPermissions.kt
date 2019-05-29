package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity


@Entity
class UserPermissions(

    val userId: Long,
    val user: User,
    val permissionId: Long,
    val permission: Permission,
    val voided: Boolean
) {


}