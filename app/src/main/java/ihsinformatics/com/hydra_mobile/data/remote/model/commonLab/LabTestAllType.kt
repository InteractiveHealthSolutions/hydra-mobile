package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.utils.Converters

@Entity(tableName = "LabTestAllType")
data class LabTestAllType(
    @SerializedName("display")var displayName: String = "",
    @SerializedName("name")var nameLabTest: String = "",
    @SerializedName("testGroup")var testGroup: String = "",
    @SerializedName("uuid") @PrimaryKey var uuidLabTest: String = "",
    @SerializedName("requiresSpecimen") var requiresSpecimen: Boolean = false,
    @TypeConverters(Converters::class)
    @Embedded
    @SerializedName("referenceConcept")  var referenceConcept: ReferenceConcept
)
