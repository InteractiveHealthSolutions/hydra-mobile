package ihsinformatics.com.hydra_mobile.data.remote.model.patient

import androidx.annotation.Nullable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.utils.Converters

@Entity(tableName = "Patient")
class Patient(
    val display: String = "",
    val identifiers: List<Identifier>,
    val person: Person,
    @PrimaryKey val uuid: String = ""
)