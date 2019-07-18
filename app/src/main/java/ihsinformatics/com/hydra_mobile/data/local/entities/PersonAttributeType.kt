package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class PersonAttributeType(

    val attributeName: String,
    val uuid: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}