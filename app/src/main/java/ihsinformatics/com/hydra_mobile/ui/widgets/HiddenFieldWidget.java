package ihsinformatics.com.hydra_mobile.ui.widgets;

import android.content.Context;
import android.view.View;
import ihsinformatics.com.hydra_mobile.data.core.options.Option;
import ihsinformatics.com.hydra_mobile.data.core.question.Question;
import ihsinformatics.com.hydra_mobile.utils.Translator;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HiddenFieldWidget extends InputWidget {

    private String answer;

    public HiddenFieldWidget(Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        setVisibility(View.GONE);
        options = null;//DataProvider.getInstance(context).getOptions(question.getQuestionId());
        answer = options.get(0).getText();
    }

    @Override
    public boolean isValidInput(boolean isMendatory) {

        return true;
    }

    @Override
    public void setOptionsOrHint(Option... data) {

    }

	/*public void setAnswer(String answer) {
		this.answer = answer;
        onTextChanged(answer);

	}*/

    private void onTextChanged(String ans) {
        if (Integer.parseInt(ans) > 0) {
            showAll();
        } else {
            hideAll();
        }
    }

    @Override
    public JSONObject getAnswer() throws JSONException {
        JSONObject param = new JSONObject();
        if (isValidInput(question.isMandatory())) {
            dismissMessage();
            param.put(question.getParamName(), answer);
        } else {
            //activity.addValidationError(getQuestionId(), "Invalid input");
        }

        return param;
    }

    @Override
    public void setAnswer(String answer, String uuid, Translator.LANGUAGE language) {
        this.answer = answer;
        onTextChanged(answer);
    }

    @Override
    public void onFocusGained() {

    }

    @Override
    public void destroy() {

    }

    private void showAll() {
        // if (getVisibility() == View.VISIBLE) {
        ArrayList<Integer> allShowAbles = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
            int[] temp = options.get(i).getOpensQuestions();
            if (temp != null) {
                for (int j = 0; j < temp.length; j++) {
                    allShowAbles.add(temp[j]);
                }
            }
        }
        int[] showables = new int[allShowAbles.size()];
        for (int i = 0; i < allShowAbles.size(); i++) {
            showables[i] = allShowAbles.get(i);
        }

        int[] hideables = new int[]{};
        // ((BaseActivity) getContext()).onChildViewItemSelected(showables, hideables);
        //  }
    }

    private void hideAll() {
        //if (getVisibility() == View.VISIBLE) {
        if (options != null) {
            ArrayList<Integer> allShowAbles = new ArrayList<>();
            for (int i = 0; i < options.size(); i++) {
                int[] temp = options.get(i).getOpensQuestions();
                if (temp != null) {
                    for (int j = 0; j < temp.length; j++) {
                        allShowAbles.add(temp[j]);
                    }
                }
            }
            int[] showables = new int[allShowAbles.size()];
            for (int i = 0; i < allShowAbles.size(); i++) {
                showables[i] = allShowAbles.get(i);
            }

            int[] hideables = new int[]{};
            // ((BaseActivity) getContext()).onChildViewItemSelected(hideables, showables);
            //}
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(View.GONE);
        hideAll();
    }
}
