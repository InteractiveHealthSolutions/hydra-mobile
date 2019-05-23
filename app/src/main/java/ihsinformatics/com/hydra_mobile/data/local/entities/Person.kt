package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.PrimaryKey

class Person(
    val givenName: String,
    val familyName: String,
    val age: Int,
    val birthdate: String,
    val gender: String,
    val address1: String,
    val address2: String,
    val address3: String,
    val stateProvince: String,
    val cityVillage: String,
    val countyDistrict: String,
    val country: String,
    val addressUuid: String
) : AbstractModel() {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}