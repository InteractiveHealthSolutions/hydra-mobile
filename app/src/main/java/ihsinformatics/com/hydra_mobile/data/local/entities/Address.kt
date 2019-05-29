package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Address(
    val division: String,
    val district: String,
    val upazilla: String,
    val union: String,
    val address: String,
    val uuid: String,
    val voided: Boolean
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}