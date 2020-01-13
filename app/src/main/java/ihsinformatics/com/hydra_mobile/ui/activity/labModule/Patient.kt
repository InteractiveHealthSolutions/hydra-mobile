package ihsinformatics.com.hydra_mobile.ui.activity.labModule

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class Patient(

    @PrimaryKey
    @SerializedName("uuid")
    val uuid: String,

    @SerializedName("display")
    val displayName: String,


    @Nullable
    var identifier: String
)
