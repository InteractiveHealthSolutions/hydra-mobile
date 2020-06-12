package ihsinformatics.com.hydra_mobile.data.remote.model.workflow


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.ihsinformatics.dynamicformsgenerator.data.core.Form
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.*
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Component
import org.json.JSONObject

@Entity
class FormResultApiResponse {

    @PrimaryKey
    @SerializedName("results")
    val result: String

    constructor(result: String) {
        this.result = result
    }


}