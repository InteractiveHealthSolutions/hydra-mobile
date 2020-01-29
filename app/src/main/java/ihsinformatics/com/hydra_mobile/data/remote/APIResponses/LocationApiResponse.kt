package ihsinformatics.com.hydra_mobile.data.remote.APIResponses


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.data.local.entities.Location
import org.json.JSONArray
import org.json.JSONObject

@Entity
class LocationApiResponse {

    @PrimaryKey
    @SerializedName("results")
    val result: List<com.ihsinformatics.dynamicformsgenerator.data.pojos.Location>

    constructor(result: List<com.ihsinformatics.dynamicformsgenerator.data.pojos.Location>) {
        this.result = result
    }


}