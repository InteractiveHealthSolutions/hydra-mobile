package ihsinformatics.com.hydra_mobile.data.remote.model.workflow


import com.google.gson.annotations.SerializedName

class PhaseComponentApiResponse {

    @SerializedName("PhaseComponentsMap")
    val phaseComponentMap: List<PhaseComponentMap>

    constructor(phaseComponentMap: List<PhaseComponentMap>) {
        this.phaseComponentMap = phaseComponentMap
    }

    constructor() {
        this.phaseComponentMap = ArrayList()
    }


}