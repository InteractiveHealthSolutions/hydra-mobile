package ihsinformatics.com.hydra_mobile.data.remote.model.patient


import com.google.gson.annotations.SerializedName

class Identifier(
     val display: String,
    val identifier: String,
    val identifierType: IdentifierType,
    val location: Location,
    val preferred: Boolean,
    val uuid: String,
    val voided: Boolean
)