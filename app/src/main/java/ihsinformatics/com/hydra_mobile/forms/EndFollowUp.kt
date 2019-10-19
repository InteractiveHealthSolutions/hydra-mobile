package ihsinformatics.com.hydra_mobile.forms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.Toast
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.widgets.EditTextWidget
import ihsinformatics.com.hydra_mobile.widgets.SingleSelect
import ihsinformatics.com.hydra_mobile.widgets.Widgets
import kotlin.collections.ArrayList

class EndFollowUp : AppCompatActivity() {

    var widget = hashMapOf<Int, Widgets>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_end_followup)


        val main_scroll = findViewById(R.id.ll_layout) as LinearLayout



//        widget.put(1,DateTimeWidget(this,"1","Form Entry Start Date/Time","Y"))
//        widget.put(2,DateTimeWidget(this,"2","User ID","Y"))
//        widget.put(3,DateTimeWidget(this,"3","Patient ID","Y"))
//        widget.put(4,LabelWidget(this,"4","Location",""))
//        widget.put(5,LabelWidget(this,"5","Longitude",""))
        widget.put(6,SingleSelect(this,"6","Latitude",""))
     //   widget.put(7,LabelWidget(this,"7","End of Followup for","Y"))
     //   widget.put(8,LabelWidget(this,"8","TB Investigation Outcome",""))
        widget.put(9,SingleSelect(this,"9","Patient's TB Investigation Outcome","Y"))
     //   widget.put(10,DateTimeWidget(this,"10","Other Reason/Remarks","N"))
        widget.put(11,SingleSelect(this,"11","If lost to follow-up, why was the patient's investigation not completed?","Y"))
        widget.put(12,EditTextWidget(this,"12","If reason for lost to follow-up 'Other', then specify:","Y"))
        widget.put(13,SingleSelect(this,"13","If died, then provide date of death:","N"))
        widget.put(14,EditTextWidget(this,"14","If died: then provide suspected primary cause of death:","Y"))
     //   widget.put(15,DateTimeWidget(this,"15","If primary cause of death other than TB, then specify:","Y"))
        widget.put(16,SingleSelect(this,"16","If not evaluated, was the patient transferred out?","Y"))
        widget.put(17,EditTextWidget(this,"17","If not transferred out, why does the patient have this outcome?","Y"))
        widget.put(18,SingleSelect(this,"18","TB Treatment Outcome",""))
        widget.put(19,EditTextWidget(this,"19","Patient's TB Treatment Outcome","Y"))
     //   widget.put(20,DateTimeWidget(this,"20","Other Reason/Remarks","N"))
        widget.put(21,SingleSelect(this,"21","Location of transfer out","Y"))
        widget.put(22,EditTextWidget(this,"22","If lost to follow-up, why was the patient's treatment interrupted?","Y"))
        widget.put(23,SingleSelect(this,"23","If reason for lost of follow-up 'Other', then specify:","Y"))
        widget.put(24,SingleSelect(this,"24","Treatment initiated at Referral / Transfer site","Y"))
        widget.put(25,EditTextWidget(this,"25","Reason treatment not initiated at referral site","Y"))
        widget.put(26,SingleSelect(this,"26","TB Registration No","N"))
        widget.put(27,SingleSelect(this,"27","Please specify, if other","Y"))
        widget.put(28,EditTextWidget(this,"28","DR Confirmation","Y"))
        widget.put(29,EditTextWidget(this,"29","ENRS Number","Y"))
        widget.put(30,SingleSelect(this,"30","Provide name and contact number of person at referral / transfer site who provided details about the patient",""))
        widget.put(31,EditTextWidget(this,"31","First name","Y"))
     //   widget.put(32,DateTimeWidget(this,"32","Last name","Y"))
        widget.put(33,EditTextWidget(this,"33","Contact number","Y"))
        widget.put(34,EditTextWidget(this,"34","If died, then provide date of death:","N"))
        widget.put(35,EditTextWidget(this,"35","If died: then provide suspected primary cause of death:","Y"))
     //   widget.put(36,DateTimeWidget(this,"36","If primary cause of death other than TB, then specify:","Y"))
        widget.put(37,SingleSelect(this,"37","If failed, then reason for failure:","Y"))
        widget.put(38,EditTextWidget(this,"38","If reason for failure 'Other', then specify:","Y"))
        widget.put(39,SingleSelect(this,"39","PET Investigation Outcome",""))
        widget.put(40,EditTextWidget(this,"40","Patient's PET Investigation Outcome","Y"))
     //   widget.put(41,DateTimeWidget(this,"41","Other Reason/Remarks","N"))
        widget.put(42,SingleSelect(this,"42","Location of transfer out","Y"))
        widget.put(43,EditTextWidget(this,"43","If lost to follow-up, why was the patient's treatment interrupted?","Y"))
        widget.put(44,SingleSelect(this,"44","If reason for lost to follow-up 'Other', then specify:","Y"))
        widget.put(45,SingleSelect(this,"45","If died, then provide date of death:","N"))
        widget.put(46,EditTextWidget(this,"46","If died: then provide suspected primary cause of death:","Y"))
     //   widget.put(47,DateTimeWidget(this,"47","If primary cause of death other than TB, then specify:","Y"))
        widget.put(48,SingleSelect(this,"48","If not evaluated, was the patient transferred out?","Y"))
        widget.put(49,EditTextWidget(this,"49","If not transferred out, why does the patient have this outcome?","Y"))
        widget.put(50,SingleSelect(this,"50","PET Treatment Outcome",""))
        widget.put(51,EditTextWidget(this,"51","Patient's PET Treatment Outcome","Y"))
      //  widget.put(52,DateTimeWidget(this,"52","Other Reason/Remarks","N"))
        widget.put(53,SingleSelect(this,"53","Location of transfer out","Y"))
        widget.put(54,EditTextWidget(this,"54","If lost to follow-up, why was the patient's treatment interrupted?","Y"))
        widget.put(55,SingleSelect(this,"55","If reason for lost ot follow-up 'Other', then specify:","Y"))
        widget.put(56,SingleSelect(this,"56","Treatment initiated at Referral / Transfer site","Y"))
        widget.put(57,EditTextWidget(this,"57","Reason treatment not initiated at referral site","Y"))
        widget.put(58,SingleSelect(this,"58","Please specify, if other","Y"))
        widget.put(59,SingleSelect(this,"59","If died, then provide date of death:","N"))
        widget.put(60,EditTextWidget(this,"60","If died: then provide suspected primary cause of death:","Y"))
     //   widget.put(61,DateTimeWidget(this,"61","","Y"))


        // checkLogics()
//        widgets.forEach { (key, value) ->
//
//            if (value.getVisibility()) {
//                main_scroll.addView(value.getView())
//            }
//        }
//        var it: Iterator<Int> = widgets.it
        for ((key, value) in widget) {
            if (value.getVisibility())
                main_scroll.addView(value.getView())

        }
        //main_scroll.addView(widgets.getView())


    }

//    fun checkLogics() {
//
//        for (widget in widgets) {
//            for (changeWidget in widgets) {
//                if (widget.getID().equals("1") && changeWidget.getID().equals("8") && widget.getValue().equals("TB Investigation")) {
//                    widgets[7].setVisiblity(false)
//                } else {
//                    widgets[7].setVisiblity(true)
//                }
//            }
//        }
//    }


}