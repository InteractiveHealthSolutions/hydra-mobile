package ihsinformatics.com.hydra_mobile.data.local.entities


import androidx.annotation.Nullable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.LocationTag
import ihsinformatics.com.hydra_mobile.utils.Converters
import java.util.*

@Entity
class Location(
    @SerializedName("name")var name: String,
    @SerializedName("country")var country: String,
    @SerializedName("stateProvince")var stateProvince: String,
//    @Nullable @SerializedName("cityVillage")var cityVillage: String,
//    @Nullable @SerializedName("primaryContact")var primaryContact: String,
//    @Nullable @SerializedName("address1")var address1: String,
//    @Nullable @SerializedName("address2")var address2: String,
//    @Nullable @SerializedName("address3")var address3: String,
    @SerializedName("description")var description: String,
    @PrimaryKey @SerializedName("uuid")var uuid: String,
    @TypeConverters(Converters::class)
    @SerializedName("tags")var tags: List<LocationTag>


)
