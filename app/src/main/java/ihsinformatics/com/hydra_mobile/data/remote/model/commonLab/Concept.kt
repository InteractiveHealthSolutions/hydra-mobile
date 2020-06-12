package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab

data class Concept(
    val display: String, val retired: Boolean, val uuid: String, val answers: List<AttributesAnswers>
                  )

data class AttributesAnswers(
    val display: String, val uuid: String
                            )