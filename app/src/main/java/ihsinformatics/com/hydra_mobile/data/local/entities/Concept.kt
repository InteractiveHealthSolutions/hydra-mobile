package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Concept(
    
    val name: String,
    val dataType: String,
    val shortName: String,
    val description: String,
    val uuid:String


) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}