package ihsinformatics.com.hydra_mobile.data.remote.model.patient

import androidx.annotation.Nullable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.utils.Converters

@Entity(tableName = "Patient")
data class Patient(
    @SerializedName("display")val patientDisplay: String = "",
//    @TypeConverters(Converters::class)
//    @Embedded
//    @SerializedName("identifiers")val patientIdentifiers: List<Identifier>,
    @Nullable var identifier: String,
    @TypeConverters(Converters::class)
    @Embedded
    @SerializedName("person")val patientPerson: Person,
    @SerializedName("uuid")@PrimaryKey val patientUUID: String = ""
)