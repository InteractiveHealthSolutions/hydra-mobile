package ihsinformatics.com.hydra_mobile.data.remote.APIResponses


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.data.local.entities.Location
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestAllType

@Entity
class LocationApiResponse {

    @PrimaryKey
    @SerializedName("results")
    val result: List<Location>

    constructor(result: List<Location>) {
        this.result = result
    }


}