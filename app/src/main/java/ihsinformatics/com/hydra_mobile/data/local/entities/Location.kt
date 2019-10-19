package ihsinformatics.com.hydra_mobile.data.local.entities


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity
class Location(
    val name: String,
    val country: String,
    val stateProvince: String,
    val countryDistrict: String,
    val cityVillage: String,
    val primaryContact: String,
    val address1: String,
    val address2: String,
    val address3: String,
    val description: String,
    val parentLocation: Long,
    val uuid: String,
    val voided: Boolean
   /* @TypeConverters(DateConverter.class)
    val dateCreated: Date*/


) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}