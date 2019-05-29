package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Person(
    val givenName: String,
    val familyName: String,
    val age: Int,
    val dob: String,
    val gender: String,
    val address1: String,
    val address2: String,
    val address3: String,
    val stateProvince: String,
    val cityVillage: String,
    val countyDistrict: String,
    val country: String

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}