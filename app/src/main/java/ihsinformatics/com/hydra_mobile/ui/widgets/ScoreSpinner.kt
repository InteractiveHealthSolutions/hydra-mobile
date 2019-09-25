package ihsinformatics.com.hydra_mobile.ui.widgets

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.core.options.Option
import ihsinformatics.com.hydra_mobile.data.core.question.Question
import ihsinformatics.com.hydra_mobile.utils.GlobalPreferences
import ihsinformatics.com.hydra_mobile.utils.Translator
import ihsinformatics.com.hydra_mobile.utils.Validation
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.HashMap

class ScoreSpinner(context: Context, question: Question, layoutId: Int) : InputWidget(context, question, layoutId),
    OnItemSelectedListener {

    private var temp: TextView? = null
    private val spAnswer: Spinner?
    private var mAdapter: ArrayAdapter<String>? = null
    private lateinit var mOptions: HashMap<String, String>
    private var oldOptionScore: Int = 0
    private lateinit var dataList: ArrayList<String>

    init {
        spAnswer = findViewById<View>(R.id.spAnswer) as Spinner
        // setBackgroundColor(getResources().getColor(android.R.color.transparent));
        spAnswer.onItemSelectedListener = this
        spAnswer.visibility = View.GONE
        findViewById<View>(R.id.llQuestion).visibility = View.GONE
        //Todo:handle the data provider class
        options = null//DataProvider.getInstance(context).getOptions(question.getQuestionId());

        setOptionsOrHint(*options.toTypedArray())
    }

    override fun isValidInput(isMendatory: Boolean): Boolean {
        val validation = Validation.getInstance()
        return validation.validate(spAnswer, question.validationFunction, isMendatory)
    }

    override fun setOptionsOrHint(vararg data: Option) {
        if (data.size > 0) {
            dataList = ArrayList()
            mOptions = HashMap()

            for (i in data.indices) {
                val translatedText = Translator.getInstance()
                    .Translate(data[i].text, GlobalPreferences.getinstance(context).findLanguagePrferenceValue())
                if (translatedText != null) {
                    mOptions[translatedText] = data[i].text
                    dataList.add(translatedText)
                } else {
                    mOptions[data[i].text] = data[i].text
                    dataList.add(data[i].text)

                }


            }

            mAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, dataList)
            spAnswer!!.adapter = mAdapter
        }
    }

    fun addOption(value: String) {
        mOptions[value] = value
        dataList.add(value)
        mAdapter!!.notifyDataSetChanged()
        spAnswer!!.setSelection(dataList.indexOf(value))
    }

    @Throws(JSONException::class)
    override fun getAnswer(): JSONObject {
        val param = JSONObject()
        if (isValidInput(question.isMandatory)) {
            dismissMessage()
            if (spAnswer!!.selectedItem.toString() == "<Select an option>") {
                param.put(question.paramName, null)
            } else {
                val lang = GlobalPreferences.getinstance(context).findLanguagePrferenceValue()
                if (lang != Translator.LANGUAGE.ENGLISH) {
                    val ans = mOptions[spAnswer.selectedItem.toString()]
                    param.put(question.paramName, ans)
                } else {
                    param.put(question.paramName, spAnswer.selectedItem.toString())
                }
            }

        } else {
           // activity.addValidationError(questionId, "Invalid input")
        }

        return param
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, arg2: Int, arg3: Long) {
        callOnItemSelectionChange()

    }

    private fun callOnItemSelectionChange() {
        val `val` = spAnswer!!.selectedItem.toString()
        val arg2 = spAnswer.selectedItemPosition
        if (Constant.SCOREABLE_QUESTIONS.contains(question.questionId)) {
            val o = findOptionByText(mOptions[`val`])
            if (o == null) {
                println("asd")
            }
           // activity.addInRating(oldOptionScore - oldOptionScore * 2)
           // activity.addInRating(o!!.score)
            //oldOptionScore = o.score
        }
        // TODO onItemClickListener can optimize the performance or try to make use of onNothingSelected
        try {
            val showables = options[arg2].opensQuestions
            val hideables = options[arg2].hidesQuestions
            // if(getVisibility()==View.VISIBLE)
           // (getContext() as BaseActivity).onChildViewItemSelected(showables, hideables)

            // TODO disables this feature for CAD4TB project
            /*if("other".equals(val.toLowerCase(Locale.US))) {
				temp = (TextView) arg1;
				activity.startActivityForResult(new Intent(context, ManualInput.class), getQuestionId());
			}*/

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onNothingSelected(arg0: AdapterView<*>) {
        // TODO Auto-generated method stub

    }

    fun setOther(value: String?) {
        if (value == null) {
            // TODO make last selected item selected here in future
            spAnswer!!.setSelection(0)
            return
        }
        try {
            if (!Validation.getInstance().areAllSpacesOrNull(value)) {
                addOption(value)

            } else {
                // TODO make last selected item selected here in future
                spAnswer!!.setSelection(0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun setVisibility(visibility: Int) {
        if (spAnswer != null) {
            // int[] showables = findAlldependantShowAbles();
            if (visibility == View.VISIBLE) {
                val showables = options[spAnswer.selectedItemPosition].opensQuestions
                val hideables = options[spAnswer.selectedItemPosition].hidesQuestions
              //  (getContext() as BaseActivity).onChildViewItemSelected(showables, hideables)
            } else {
                val hideables = findAlldependantHideAbles()
               // (getContext() as BaseActivity).onChildViewItemSelected(null, hideables)
            }
        }

        /*if(visibility == View.GONE) {
			if(Global.SCOREABLE_QUESTIONS.contains(question.getQuestionId())) {

				activity.addInRating(oldOptionScore-(oldOptionScore*2));
				activity.addInRating(0);
				// oldOptionScore = o.getScore();
			}
		} else {
			String val = spAnswer.getSelectedItem().toString();
			if(Global.SCOREABLE_QUESTIONS.contains(question.getQuestionId())) {
				Option o = findOptionByText(mOptions.get(val));
				if(o==null) {
					System.out.println("asd");
				}
				activity.addInRating(oldOptionScore-(oldOptionScore*2));
				activity.addInRating(o.getScore());
				oldOptionScore = o.getScore();
			}
		}*/

        super.setVisibility(visibility)
    }

    private fun findAlldependantShowAbles(): IntArray {
        var toReturn = IntArray(0)
        for (o in options) {
            val array = o.opensQuestions
            if (array != null) {
                val temp = toReturn
                toReturn = IntArray(array.size + toReturn.size)

                for (i in toReturn.size - array.size until toReturn.size) {
                    toReturn[i] = array[i - temp.size]
                }

                for (i in temp.indices) {
                    toReturn[i] = temp[i]
                }

            }
        }

        return toReturn
    }

    private fun findAlldependantHideAbles(): IntArray {
        var toReturn = IntArray(0)
        for (o in options) {
            val array = o.hidesQuestions
            if (array != null) {
                val temp = toReturn
                toReturn = IntArray(array.size + toReturn.size)
                for (i in toReturn.size - array.size until toReturn.size) {
                    toReturn[i] = array[i - temp.size]
                }
                for (i in temp.indices) {
                    toReturn[i] = temp[i]
                }
            }
        }

        return toReturn
    }

    override fun setClickable(clickable: Boolean) {
        spAnswer!!.isClickable = clickable
        super.setClickable(clickable)
    }

    fun setselectedIndex(index: Int) {
        spAnswer!!.setSelection(index)
        callOnItemSelectionChange()
    }

    override fun setEnabled(enabled: Boolean) {

        for (lol in spAnswer!!.touchables) {
            lol.isEnabled = enabled
        }

        super.setEnabled(enabled)
    }

    override fun onFocusGained() {
        spAnswer!!.performClick()

    }

    override fun setAnswer(answer: String, uuid: String, language: Translator.LANGUAGE) {
        var answer = answer
        answer = Translator.getInstance().Translate(answer, language)
        val i = dataList.indexOf(answer)
        if (i == -1) {
            addOption(answer)
            spAnswer!!.setSelection(dataList.indexOf(answer))
            return
        }

        spAnswer!!.setSelection(dataList.indexOf(answer))
        visibility = View.VISIBLE

    }

    private fun findOptionByText(text: String?): Option? {
        for (o in options) {
            if (o.text == text) {
                return o
            }
        }

        return null
    }

    override fun destroy() {
        // TODO Auto-generated method stub

    }


}
