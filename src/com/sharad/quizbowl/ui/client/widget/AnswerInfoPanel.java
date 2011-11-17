package com.sharad.quizbowl.ui.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class AnswerInfoPanel extends Composite {

	private static AnswerInfoPanelUiBinder uiBinder = GWT
			.create(AnswerInfoPanelUiBinder.class);
	@UiField
	public HTML correct, correctAnswer;

	interface AnswerInfoPanelUiBinder extends UiBinder<Widget, AnswerInfoPanel> {
	}

	public AnswerInfoPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void loadAnswerInfo(AnswerInfo info) {
		if (info.isCorrect())
			correct.setHTML("<b>CORRECT!</b>");
		else
			correct.setHTML("<b>INCORRECT!</b>");
		correctAnswer.setHTML("ANSWER: " + info.getCorrectAnswer());
	}

}
