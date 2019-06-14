package ihsinformatics.com.hydra_mobile.view.widgets.address

import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.core.question.config.AddressConfiguration

class EditTextAddressItem {

    internal var tvTag: TextView
    internal var etValues: EditText
    internal var paramName: String

    constructor(
        linearLayout: RelativeLayout,
        openAddressField: AddressConfiguration.OpenAddressField
    ) {
        tvTag = linearLayout.findViewById<View>(R.id.tvQuestion) as TextView
        etValues = linearLayout.findViewById<View>(R.id.etAnswer) as EditText

        tvTag.setText(openAddressField.getFieldName())
        paramName = openAddressField.getParamName()
    }
}