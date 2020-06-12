package ihsinformatics.com.hydra_mobile.data.remote.APIResponses


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestAllType

@Entity
class LabTestTypeApiResponse {

    @PrimaryKey
    @SerializedName("results")
    val result: List<LabTestAllType>

    constructor(result: List<LabTestAllType>) {
        this.result = result
    }


}