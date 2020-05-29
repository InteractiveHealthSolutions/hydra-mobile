package ihsinformatics.com.hydra_mobile.data.local.entities.workflow

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class NameAndUUID(


    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("name")
    val name: String

)