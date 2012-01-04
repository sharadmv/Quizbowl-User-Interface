package com.sharad.quizbowl.ui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.sharad.quizbowl.ui.client.json.tossup.Tossup;
import com.sharad.quizbowl.ui.client.widget.CategoryWidget;

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
	@UiField
	public SimplePanel arrowUp;
	@UiField
	public SimplePanel arrowDown;
	@UiField
	public HTML rating;
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

		initWidget(uiBinder.createAndBindUi(this));
		labelPanel.setStyleName("tossupLabel");
		setStyleName("tossupBox");
		arrowUp.sinkEvents(Event.ONCLICK);
		arrowUp.addHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				updateRating(1);
			}

		}, ClickEvent.getType());

		arrowDown.sinkEvents(Event.ONCLICK);
		arrowDown.addHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				updateRating(-1);
			}

		}, ClickEvent.getType());
		if (HomeWidget.LOGGED_IN) {
			if (tossup.getUserRating() == 1) {
				arrowUp.setStyleName("arrow up selected");
				arrowDown.setStyleName("arrow down");

			} else if (tossup.getUserRating() == -1) {
				arrowDown.setStyleName("arrow down selected");
				arrowUp.setStyleName("arrow up");

			}
		}
		rating.setHTML(Integer.parseInt(tossup.getRating()) >= 0 ? "+"
				+ tossup.getRating() : tossup.getRating());
		rating.setStyleName("rating");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(tossupLabel.getText()).append("\\n");
		sb.append(tossupText.getText()).append("\\n");
		sb.append(tossupAnswer.getText());
		return sb.toString();
	}

	private void updateRating(int value) {
		if (HomeWidget.LOGGED_IN) {
			if (tossup.getUserRating() != value) {
				addRating(value, HomeWidget.USERNAME, tossup.getPkey());
			} else {
				addRating(0, HomeWidget.USERNAME, tossup.getPkey());
			}
		} else {
			Window.alert("Please login first");
		}
	}

	public native void addRating(int value, String username, String question)/*-{
		var that = this;
		$wnd.now
				.addRating(
						value,
						username,
						question,
						$entry(function(val, rating) {
							that.@com.sharad.quizbowl.ui.client.TossupBox::updateDisplay(ILjava/lang/String;)(val,rating)
						}));
	}-*/;

	public void updateDisplay(int value, String r) {
		tossup.setUserRating(value);
		tossup.setRating(r);
		rating.setHTML(Integer.parseInt(tossup.getRating()) >= 0 ? "+"
				+ tossup.getRating() : tossup.getRating());
		if (HomeWidget.LOGGED_IN) {
			if (value == 1) {
				arrowUp.setStyleName("arrow up selected");
				arrowDown.setStyleName("arrow down");
			} else if (value == -1) {
				arrowUp.setStyleName("arrow up");
				arrowDown.setStyleName("arrow down selected");
			} else {
				arrowUp.setStyleName("arrow up");
				arrowDown.setStyleName("arrow down");
			}
		}
	}

	public Tossup getTossup() {
		return tossup;
	}

	public native void exportStaticMethods() /*-{
	}-*/;
}