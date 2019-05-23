package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.PrimaryKey

class Concept(
    val name: String,
    val dataType: String,
    val shortName: String,
    val description: String


) : AbstractModel() {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}