package com.sharad.quizbowl.ui.client.widget.event;

import java.util.Date;

import com.google.gwt.event.shared.GwtEvent;
import com.sharad.quizbowl.ui.client.json.tossup.Tossup;

public class AnswerEvent extends GwtEvent<AnswerEventHandler> {
	public static final Type<AnswerEventHandler> TYPE = new Type<AnswerEventHandler>();
	private Tossup tossup;
	private boolean correct;
	private String answer;
	private int score;
	private Date stamp;

	public AnswerEvent(Tossup tossup, boolean correct, String answer,
			int score, Date stamp) {
		super();
		this.tossup = tossup;
		this.correct = correct;
		this.answer = answer;
		this.score = score;
		this.stamp = stamp;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AnswerEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AnswerEventHandler handler) {
		handler.onAnswerReceived(this);
	}

	public Tossup getTossup() {
		return tossup;
	}

	public boolean isCorrect() {
		return correct;
	}

	public String getAnswer() {
		return answer;
	}

	public int getScore() {
		return score;
	}

	public Date getTimestamp() {
		return stamp;
	}
}
