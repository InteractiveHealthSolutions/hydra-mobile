package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
class Visit(

    val visitDate: Date,
    val patientId: Long,
    val patient: Patient,
    val creator: Long,
    val user: User,
    val voided: Boolean

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}