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

	public final native void setCategory(String category) /*-{
		this.category = category;
	}-*/;

	public final native String getPkey() /*-{
		return this.pKey;
	}-*/;

	public final native String getAccept() /*-{
		return this.ACCEPT;
	}-*/;

	public final native String getRating()/*-{
		if (typeof (this.rating) != "string") {
			return '0';
		} else {
			return this.rating;
		}
	}-*/;

	public final native int getUserRating()/*-{
		if (this.user_rating == null) {
			return 0;
		} else {
			return this.user_rating;
		}
	}-*/;

	public final native void setRating(String rating)/*-{
		this.rating = rating;
	}-*/;

	public final native void setUserRating(int rating)/*-{
		this.user_rating = rating;
	}-*/;

	public final String getString() {
		return Joiner.on(";").join(getYear(), getTournament(), getRound(),
				getQuestionNum(), getDifficulty(), getCategory(),
				getQuestion(), getAnswer());
	}

}
