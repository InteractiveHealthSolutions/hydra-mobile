package ihsinformatics.com.hydra_mobile.data.local.entities.workflow

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
class LocationTag(
    @SerializedName("name")val nameLocationTag: String,
    @SerializedName("uuid")val uuidLocationTag: String,
    @SerializedName("display")val displayLocationTag: String
                 )