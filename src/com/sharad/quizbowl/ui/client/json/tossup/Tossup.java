package com.sharad.quizbowl.ui.client.json.tossup;

import com.google.gwt.core.client.JavaScriptObject;
import com.sharad.quizbowl.ui.client.util.guava.Joiner;

public class Tossup extends JavaScriptObject {
	protected Tossup() {
	}

	public final native String getTournament() /*-{
		return this.tournament;
	}-*/;

	public final native int getYear() /*-{
		return this.year;
	}-*/;

	public final native String getQuestion() /*-{
		return this.question;
	}-*/;

	public final native String getAnswer() /*-{
		return this.answer;
	}-*/;

	public final native String getRound() /*-{
		return this.round;
	}-*/;

	public final native int getQuestionNum() /*-{
		return this.question_num;
	}-*/;

	public final native String getDifficulty() /*-{
		return this.difficulty;
	}-*/;

	public final native String getCategory() /*-{
		return this.category;
	}-*/;

	public final native String getAccept() /*-{
		return this.ACCEPT;
	}-*/;

	public final String getString() {
		return Joiner.on(";").join(getYear(), getTournament(), getRound(),
				getQuestionNum(), getDifficulty(), getCategory(),
				getQuestion(), getAnswer());
	}
}
