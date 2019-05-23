package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.PrimaryKey

class Location(
    val name: String,
    val locationId: String,
    val primaryContact: String,
    val fastLocation: String,
    val pmdtLocation: String,
    val petLocation: String,
    val comorbiditiesLocation: String,
    val childhoodTbLocation: String,
    val address1: String,
    val address2: String,
    val address3: String,
    val district: String,
    val city: String,
    val province: String,
    val description: String,
    val zttsLocation: String

):AbstractModel() {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}