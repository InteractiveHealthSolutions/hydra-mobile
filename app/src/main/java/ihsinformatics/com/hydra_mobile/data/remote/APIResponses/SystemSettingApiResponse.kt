package ihsinformatics.com.hydra_mobile.data.remote.APIResponses


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.ihsinformatics.dynamicformsgenerator.data.pojos.SystemSettings
@Entity
class SystemSettingApiResponse {

    @PrimaryKey
    @SerializedName("results")
    val result: List<SystemSettings>

    constructor(result: List<SystemSettings>) {
        this.result = result
    }


}