package com.sharad.quizbowl.ui.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.sharad.quizbowl.ui.client.json.tossup.Tossup;

public class TossupInfoPanel extends Composite {

	private static TossupInfoPanelUiBinder uiBinder = GWT
			.create(TossupInfoPanelUiBinder.class);
	@UiField(provided = true)
	public DisclosurePanel category;
	@UiField(provided = true)
	public HTML location;
	private HTML average;

	interface TossupInfoPanelUiBinder extends UiBinder<Widget, TossupInfoPanel> {
	}

	public TossupInfoPanel() {
		category = new DisclosurePanel("Show Category");
		category.setAnimationEnabled(true);
		location = new HTML();
		average = new HTML();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void loadTossup(Tossup t) {
		category.setContent(new HTML("<b>Category:</b> " + t.getCategory()));
		location.setHTML("<b>Tournament:</b> " + t.getYear() + " "
				+ t.getTournament() + "<br/><b>Difficulty: </b> "
				+ t.getDifficulty() + "<br/><b>Round: </b>" + t.getRound()
				+ "<br/><b>Question:</b> " + t.getQuestionNum());
	}

	public void setShowCategory(boolean show) {
		category.setVisible(show);
	}

}
