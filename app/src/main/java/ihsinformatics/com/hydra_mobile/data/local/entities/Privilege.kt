package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Privilege(
    val privilege: String,
    val description: String
) {

    @PrimaryKey(autoGenerate = true)
    var id:Int =0
}