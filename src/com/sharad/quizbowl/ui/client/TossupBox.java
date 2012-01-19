package com.sharad.quizbowl.ui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sharad.quizbowl.ui.client.json.tossup.Tossup;
import com.sharad.quizbowl.ui.client.widget.CategoryWidget;
import com.sharad.quizbowl.ui.client.widget.Rater;

public class TossupBox extends Composite {
	private TossupBoxUiBinder uiBinder = GWT.create(TossupBoxUiBinder.class);
	@UiField(provided = true)
	public Label tossupLabel;
	@UiField(provided = true)
	public Label tossupText, tossupAnswer;
	@UiField(provided = true)
	public CategoryWidget category;
	@UiField
	public HorizontalPanel labelPanel;
	@UiField(provided = true)
	public Rater rater;
	private Tossup tossup;

	@UiTemplate("TossupBox.ui.xml")
	interface TossupBoxUiBinder extends UiBinder<Widget, TossupBox> {
	}

	public TossupBox(Tossup t) {
		this.tossup = t;
		category = new CategoryWidget(t);
		tossupLabel = new Label(t.getYear() + " " + t.getTournament() + " - "
				+ t.getRound() + " (Question #" + t.getQuestionNum() + ") "
				+ "[" + t.getDifficulty() + "]");

		tossupText = new HTML(t.getQuestion());
		tossupAnswer = new HTML("ANSWER: " + t.getAnswer());
		tossupText.setStyleName("tossupText");
		tossupAnswer.setStyleName("tossupAnswer");
		rater = new Rater();
		rater.load(tossup);
		initWidget(uiBinder.createAndBindUi(this));
		labelPanel.setStyleName("tossupLabel");
		setStyleName("tossupBox");

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(tossupLabel.getText()).append("\\n");
		sb.append(tossupText.getText()).append("\\n");
		sb.append(tossupAnswer.getText());
		return sb.toString();
	}

	public Tossup getTossup() {
		return tossup;
	}

	public native void exportStaticMethods() /*-{
	}-*/;
}