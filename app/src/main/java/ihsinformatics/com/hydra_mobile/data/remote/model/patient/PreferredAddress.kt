package ihsinformatics.com.hydra_mobile.data.remote.model.patient


import com.google.gson.annotations.SerializedName

data class PreferredAddress(
    @SerializedName("address1")
    val address1: String,
    @SerializedName("address10")
    val address10: Any,
    @SerializedName("address11")
    val address11: Any,
    @SerializedName("address12")
    val address12: Any,
    @SerializedName("address13")
    val address13: Any,
    @SerializedName("address14")
    val address14: Any,
    @SerializedName("address15")
    val address15: Any,
    @SerializedName("address2")
    val address2: String,
    @SerializedName("address3")
    val address3: Any,
    @SerializedName("address4")
    val address4: Any,
    @SerializedName("address5")
    val address5: Any,
    @SerializedName("address6")
    val address6: Any,
    @SerializedName("address7")
    val address7: Any,
    @SerializedName("address8")
    val address8: Any,
    @SerializedName("address9")
    val address9: Any,
    @SerializedName("cityVillage")
    val cityVillage: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("countyDistrict")
    val countyDistrict: String,
    @SerializedName("display")
    val display: String,
    @SerializedName("endDate")
    val endDate: Any,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("postalCode")
    val postalCode: Any,
    @SerializedName("preferred")
    val preferred: Boolean,
    @SerializedName("startDate")
    val startDate: Any,
    @SerializedName("stateProvince")
    val stateProvince: String,
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("voided")
    val voided: Boolean
)