package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Order(
    val accessionNumber: String,
    val creator: Long,
    //val user: User,
    val patientId: Long,
   // val patient: Patient,
    val orderTypeId: Long,
   // val orderType: OrderType,
    val encounterId: Long,
    //val encounter: Encounter,
    val isUploaded: Boolean,
    val voided: Boolean,
    val uuid: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}