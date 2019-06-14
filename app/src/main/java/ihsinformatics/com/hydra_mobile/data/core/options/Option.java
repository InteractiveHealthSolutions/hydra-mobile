package ihsinformatics.com.hydra_mobile.data.core.options;

public class Option {

	protected int questionId;
	protected int optionId;
	protected int[] opensQuestions;
	protected int[] hidesQuestions;
	protected String uuid;
	protected String text;
	protected int score;


	public Option(int questionId, int optionId, int[] opensQuestions, int[] hidesQuestions,
                  String uuid, String text, int score) {
		super();
		this.questionId = questionId;
		this.optionId = optionId;
		this.opensQuestions = opensQuestions;
		this.hidesQuestions = hidesQuestions;
		this.uuid = uuid;
		this.text = text;
		this.score = score;
	}

	public Option(int questionId, int optionId, int[] opensQuestions, int[] hidesQuestions, String uuid, String text) {
		this(questionId, optionId, opensQuestions, hidesQuestions, uuid, text, -1);

	}


	public int getQuestionId() {
		return questionId;
	}


	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}


	public int getOptionId() {
		return optionId;
	}


	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}


	public int[] getOpensQuestions() {
		return opensQuestions;
	}


	public void setOpensQuestions(int[] opensQuestions) {
		this.opensQuestions = opensQuestions;
	}

	

	public int[] getHidesQuestions() {
		return hidesQuestions;
	}


	public void setHidesQuestions(int[] hidesQuestions) {
		this.hidesQuestions = hidesQuestions;
	}


	public String getUuid() {
		return uuid;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return getText();
	}
}
