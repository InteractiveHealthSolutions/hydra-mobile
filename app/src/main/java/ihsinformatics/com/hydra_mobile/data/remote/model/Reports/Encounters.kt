package ihsinformatics.com.hydra_mobile.data.remote.model.Reports

data class Encounters(
    val display: String,
    val encounterDatetime: String,
    val encounterType: EncounterType,
    val obs: List<Ob>,
    val uuid: String
)