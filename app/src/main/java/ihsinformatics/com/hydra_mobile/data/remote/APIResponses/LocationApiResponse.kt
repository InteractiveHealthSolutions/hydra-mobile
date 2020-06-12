package ihsinformatics.com.hydra_mobile.data.remote.APIResponses


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.ihsinformatics.dynamicformsgenerator.data.pojos.Location
import com.ihsinformatics.dynamicformsgenerator.data.pojos.LocationDTO
import org.json.JSONArray
import org.json.JSONObject

@Entity
class LocationApiResponse {

    @PrimaryKey
    @SerializedName("results")
    val result: List<LocationDTO>

    constructor(result: List<LocationDTO>) {
        this.result = result
    }


}