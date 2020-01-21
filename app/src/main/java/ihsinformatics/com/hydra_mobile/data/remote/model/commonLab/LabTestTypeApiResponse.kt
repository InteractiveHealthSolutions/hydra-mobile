package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.ihsinformatics.dynamicformsgenerator.data.core.Form
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.*
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Component

@Entity
class LabTestTypeApiResponse {

    @PrimaryKey
    @SerializedName("results")
    val result: List<LabTestAllType>

    constructor(result: List<LabTestAllType>) {
        this.result = result
    }


}