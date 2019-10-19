package ihsinformatics.com.hydra_mobile.data.core.options;

import ihsinformatics.com.hydra_mobile.utils.SkipRange;

/**
 * Created by Owais on 11/8/2017.
 */
public class RangeOption extends Option {
    private SkipRange skipRange;

    public RangeOption(int questionId, int optionId, int[] opensQuestions, int[] hidesQuestions, String uuid, String text, int score, SkipRange skipRange) {
        super(questionId, optionId, opensQuestions, hidesQuestions, uuid, text, score);
        this.skipRange = skipRange;
    }

    public SkipRange getSkipRange() {
        return skipRange;
    }

    public void setSkipRange(SkipRange skipRange) {
        this.skipRange = skipRange;
    }
}
