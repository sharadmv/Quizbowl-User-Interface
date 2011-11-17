package com.sharad.quizbowl.ui.client.widget;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sharad.quizbowl.ui.client.json.tossup.Tossup;
import com.sharad.quizbowl.ui.client.widget.event.ReadEvent;
import com.sharad.quizbowl.ui.client.widget.event.ReadEventHandler;
import com.smartgwt.client.widgets.Slider;

public class Reader extends Composite {

	private static final String ACCEPT_DELIMITER = "|";
	private static ReaderUiBinder uiBinder = GWT.create(ReaderUiBinder.class);
	@UiField
	public TextArea readArea;
	private int count;
	private int wordCount;

	private List<Tossup> tossups;
	private HandlerManager handlerManager;
	@UiField(provided = true)
	public Slider speedSlider;
	private boolean reading = false, buzzed = false;
	private Timer readTimer;
	@UiField
	public FocusWidget buzzButton;
	@UiField
	public TextBox answerBox;
	@UiField
	public VerticalPanel mainPanel;
	private Timer buzzTimer;
	public Tossup currentTossup;

	interface ReaderUiBinder extends UiBinder<Widget, Reader> {

	}

	public void setTossups(List<Tossup> tossups) {
		this.tossups = tossups;
		count = 0;
		if (tossups.size() > 0) {
			read(tossups.get(0));
		}
	}

	public void read(Tossup tossup) {
		currentTossup = tossup;
		readArea.setText("");
		reading = true;
		wordCount = 0;

		final String[] split = currentTossup.getQuestion().split(" ");
		readTimer = new Timer() {

			@Override
			public void run() {
				if (!buzzed) {
					if (wordCount > split.length) {
						reading = false;
					} else {
						StringBuilder sb = new StringBuilder();
						String delimiter = "";
						for (int i = 0; i < wordCount; i++) {
							sb.append(delimiter).append(split[i]);
							delimiter = " ";
						}
						readArea.setText(sb.toString());
						wordCount++;
						if (reading)
							readTimer.schedule(convertSliderToSleed(speedSlider
									.getValue()));
					}
				}
			}

		};
		readTimer.schedule(convertSliderToSleed(speedSlider.getValue()));
		ReadEvent event = new ReadEvent(tossup);
		fireEvent(event);
	}

	private int convertSliderToSleed(float value) {
		return (int) (1 / value * 1000);
	}

	public Reader() {
		speedSlider = new Slider("Speed");
		speedSlider.setVertical(false);
		speedSlider.setMinValue(1);
		speedSlider.setMaxValue(100);
		speedSlider.setNumValues(99);
		handlerManager = new HandlerManager(this);
		initWidget(uiBinder.createAndBindUi(this));
		answerBox.setVisible(false);
		answerBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (buzzed) {
					if (event.getNativeKeyCode() == 13) {
						buzzTimer.cancel();
						buzzed = false;
						readArea.setFocus(true);
						checkAnswer(answerBox.getText());
						answerBox.setText("");

					}
				}

			}

		});
		readArea.setStyleName("readArea");
		readArea.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == 32) {
					buzz();
				}

			}

		});
		buzzButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				buzz();
			}
		});

	}

	private void buzz() {
		buzzed = true;
		buzzButton.setVisible(false);
		answerBox.setVisible(true);
		buzzTimer = new Timer() {
			@Override
			public void run() {
				buzzed = false;
			}
		};
		answerBox.setFocus(true);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	public HandlerRegistration addReadEventHandler(
			ReadEventHandler readEventHandler) {
		return handlerManager.addHandler(ReadEvent.TYPE, readEventHandler);
	}

	private boolean checkAnswer(String text) {
		if (currentTossup.getAccept() != null) {
			if (text == "")
				return false;
			String[] acceptable = currentTossup.getAccept().split(
					ACCEPT_DELIMITER);
			boolean correct = false;
			for (int i = 0; i < acceptable.length; i++) {
				if (text.equals(acceptable[i])) {
					correct = true;
				}
			}

			return correct;
		}
		return false;

	}
}
