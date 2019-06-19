package ihsinformatics.com.hydra_mobile.view.widgets

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
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
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.HashMap

import android.content.Context.INPUT_METHOD_SERVICE
import ihsinformatics.com.hydra_mobile.view.activity.BaseActivity

class SpinnerWidget(context: Context, question: Question, layoutId: Int) : InputWidget(context, question, layoutId),
    OnItemSelectedListener {

    internal lateinit var temp: TextView
    private val spAnswer: Spinner
    private lateinit var mAdapter: ArrayAdapter<String>
    private lateinit var mOptions: HashMap<String, Option>
    private var oldOptionScore: Int = 0
    private lateinit var dataList: ArrayList<String>

    init {
        spAnswer = findViewById<Spinner>(R.id.spAnswer)
        spAnswer.onItemSelectedListener = this
        //Todo : handle the data provider class
        options = null //DataProvider.getInstance(context).getOptions(question.getQuestionId());
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
                val option = data[i]
                val optionText = option.text
                val translatedText = Translator.getInstance()
                    .Translate(optionText, GlobalPreferences.getinstance(context).findLanguagePrferenceValue())
                if (translatedText != null) {
                    mOptions[translatedText] = option
                    dataList.add(translatedText)
                } else {
                    mOptions[optionText] = option
                    dataList.add(optionText)
                }
            }
            mAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, dataList)
            spAnswer!!.adapter = mAdapter
        }
    }

    private fun addOption(value: Option) {
        mOptions[value.text] = value
        dataList.add(value.text)
        mAdapter!!.notifyDataSetChanged()
        spAnswer!!.setSelection(dataList.indexOf(value.text))
    }

    @Throws(JSONException::class)
    override fun getAnswer(): JSONObject {
        val param = JSONObject()
        if (isValidInput(question.isMandatory)) {
            dismissMessage()
            if (spAnswer!!.selectedItem.toString() == "<Select an option>" || spAnswer.selectedItem.toString() == "") {
                param.put(question.paramName, null)
            } else {
                val ans = mOptions[spAnswer.selectedItem.toString()]
                if (ans!!.uuid == "" || ans.uuid == null) {
                    param.put(question.paramName, ans.text)
                } else {
                    param.put(question.paramName, ans.uuid)
                }
            }
        } else {
            // activity.addValidationError(getQuestionId(), "Invalid input");
        }
        return param
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, arg2: Int, arg3: Long) {
        if (Constant.SCOREABLE_QUESTIONS.contains(question.questionId)) {
            val o = options[arg2]
            if (o == null) {
                println("asd")
            }
            // activity.addInRating(oldOptionScore - (oldOptionScore * 2));
            // activity.addInRating(o.getScore());
            oldOptionScore = o!!.score
        }
        // TODO onItemClickListener can optimize the performance or try to make use of onNothingSelected
        try {
            val showables = options[arg2].opensQuestions
            val hideables = options[arg2].hidesQuestions
            // if(getVisibility()==View.VISIBLE)
            (getContext() as BaseActivity).onChildViewItemSelected(showables, hideables);

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

    fun setOther(value: Option?) {
        if (value == null) {
            // TODO make last selected item selected here in future
            spAnswer!!.setSelection(0)
            return
        }
        try {
            if (!Validation.getInstance().areAllSpacesOrNull(value.text)) {
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
                // ((BaseActivity) getContext()).onChildViewItemSelected(showables, hideables);
            } else {
                val hideables = findAlldependantHideAbles()
                //((BaseActivity) getContext()).onChildViewItemSelected(null, hideables);
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

    private fun findAllDependantShowAbles(): IntArray {
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

    fun setSelectedIndex(index: Int) {
        spAnswer!!.setSelection(index)
    }

    override fun setEnabled(enabled: Boolean) {
        for (lol in spAnswer!!.touchables) {
            lol.isEnabled = enabled
        }
        super.setEnabled(enabled)
    }

    override fun onFocusGained() {
        spAnswer!!.performClick()
        val inputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun setAnswer(answer: String, uuid: String, language: Translator.LANGUAGE) {
        var answer = answer
        answer = Translator.getInstance().Translate(answer, language)
        val i = dataList.indexOf(answer)
        if (i == -1) {
            addOption(Option(question.questionId, -1, null, null, uuid, answer, -1))
            spAnswer!!.setSelection(dataList.indexOf(answer))
            return
        }
        spAnswer!!.setSelection(dataList.indexOf(answer))
        visibility = View.VISIBLE
    }

    private fun findOptionByText(text: String): Option? {
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
