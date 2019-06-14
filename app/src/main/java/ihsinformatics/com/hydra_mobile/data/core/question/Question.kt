package ihsinformatics.com.hydra_mobile.data.core.question

import ihsinformatics.com.hydra_mobile.data.core.question.config.Configuration
import ihsinformatics.com.hydra_mobile.utils.InputWidgetsType

data class Question(
    val isMandatory: Boolean,
    val formTypeId: Int,
    val questionId: Int,
    val questionNumber: String,
    val questionType: InputWidgetsType,
    val initialVisibility: Int,
    val validationFunction: String,
    val text: String,
    val paramName: String,
    val questionConfiguration: Configuration
) {


}