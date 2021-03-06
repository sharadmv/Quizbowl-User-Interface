package com.sharad.quizbowl.ui.client.widget;

import java.util.Date;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sharad.quizbowl.ui.client.json.tossup.Tossup;
import com.sharad.quizbowl.ui.client.widget.event.AnswerEvent;
import com.sharad.quizbowl.ui.client.widget.event.AnswerEventHandler;
import com.sharad.quizbowl.ui.client.widget.event.AnswerInfoEvent;
import com.sharad.quizbowl.ui.client.widget.event.AnswerInfoEventHandler;
import com.sharad.quizbowl.ui.client.widget.event.NewTossupEvent;
import com.sharad.quizbowl.ui.client.widget.event.NewTossupEventHandler;
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
	private boolean reading = false, buzzed = false, idle = true,
			canAnswer = false;
	@UiField
	public Button buzzButton;
	@UiField
	public TextBox answerBox;
	@UiField
	public VerticalPanel mainPanel;
	private Timer readTimer, waitTimer;
	public Tossup currentTossup;
	public static final String BUZZ_TEXT = "Buzz";
	public static final String CONTINUE_TEXT = "Continue";

	interface ReaderUiBinder extends UiBinder<Widget, Reader> {

	}

	public void setTossups(List<Tossup> tossups) {
		this.tossups = tossups;
		count = 0;
		if (tossups.size() > 0) {
			read();
		}
	}

	public void getNewTossup() {
		idle = false;
		if (tossups == null || count == tossups.size()) {
			NewTossupEvent event = new NewTossupEvent(1);
			fireEvent(event);
		} else {
			read();
		}
	}

	public void showAnswer(boolean correct, boolean time) {
		waitTimer.cancel();
		buzzed = false;
		readArea.setFocus(true);
		if (!time) {
			AnswerEvent ae = new AnswerEvent(currentTossup, correct,
					answerBox.getText(), wordCount + 1, new Date());
			fireEvent(ae);
		}
		AnswerInfoEvent aie = new AnswerInfoEvent(new AnswerInfo(correct,
				currentTossup.getAnswer()));
		answerBox.setText("");
		fireEvent(aie);
		readArea.setText(currentTossup.getQuestion() + "\n\nANSWER: "
				+ currentTossup.getAnswer());
		idle = true;
		buzzButton.setVisible(true);
		buzzButton.setText(CONTINUE_TEXT);
		answerBox.setVisible(false);
		canAnswer = false;
	}

	public void read() {
		idle = false;

		currentTossup = tossups.get(count);
		readArea.setText("");
		reading = true;
		wordCount = 0;

		final String[] split = currentTossup.getQuestion().split(" ");
		waitTimer = new Timer() {

			@Override
			public void run() {
				canAnswer = false;
				showAnswer(false, true);
			}

		};
		readTimer = new Timer() {

			@Override
			public void run() {
				if (!buzzed) {
					buzzButton.setText(BUZZ_TEXT);

					if (wordCount > split.length) {
						waitTimer.schedule(5000);
						reading = false;
					} else {
						StringBuilder sb = new StringBuilder();
						String delimiter = "";
						for (int i = 0; i < wordCount; i++) {
							sb.append(delimiter).append(split[i]);
							delimiter = " ";
						}
						// speak(split[wordCount]);
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
		reading = true;
		canAnswer = true;
		ReadEvent event = new ReadEvent(currentTossup);
		fireEvent(event);
	}

	protected native void speak(String string)/*-{
		$wnd.speak(string);
	}-*/;

	private int convertSliderToSleed(float value) {
		return (int) (2019.19 - 19.1919 * value);
	}

	public Reader() {
		speedSlider = new Slider("Speed");
		speedSlider.setVertical(false);
		speedSlider.setMinValue(1);
		speedSlider.setMaxValue(100);
		speedSlider.setNumValues(99);
		speedSlider.setValue(79);
		handlerManager = new HandlerManager(this);
		initWidget(uiBinder.createAndBindUi(this));
		answerBox.setVisible(false);
		answerBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (buzzed && canAnswer) {
					if (event.getNativeKeyCode() == 13
							&& !answerBox.getText().trim().equals("")) {
						checkAnswer(answerBox.getText(),
								currentTossup.getAnswer());
					}
				}

			}

		});
		readArea.setStyleName("readArea");
		readArea.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (idle) {
					if (event.getNativeKeyCode() == 13
							|| event.getNativeKeyCode() == 32) {
						count++;
						getNewTossup();
					}
				} else {
					if (canAnswer
							&& (!event.isControlKeyDown() && event
									.getNativeKeyCode() == 32)) {
						buzz();
					} else if (canAnswer && event.isControlKeyDown()
							&& event.getNativeKeyCode() == 32) {
						count++;
						getNewTossup();
					}
				}

			}

		});
		buzzButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!idle)
					buzz();
				else
					getNewTossup();
			}
		});

	}


	private void buzz() {
		canAnswer = true;
		buzzed = true;
		buzzButton.setVisible(false);
		answerBox.setVisible(true);
		answerBox.setFocus(true);
		answerBox.setAlignment(TextAlignment.CENTER);
		answerBox.setText("");
		waitTimer.schedule(10000);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	public HandlerRegistration addReadEventHandler(
			ReadEventHandler readEventHandler) {
		return handlerManager.addHandler(ReadEvent.TYPE, readEventHandler);
	}

	private native void checkAnswer(String text, String correctText)/*-{
		var that = this;
		$wnd.now
				.checkAnswer(
						text,
						correctText,
						$entry(function(b1) {
							that.@com.sharad.quizbowl.ui.client.widget.Reader::showAnswer(ZZ)(b1,false)
						}));
	}-*/;

	public HandlerRegistration addNewTossupEventHandler(
			NewTossupEventHandler handler) {
		return handlerManager.addHandler(NewTossupEvent.TYPE, handler);
	}

	public HandlerRegistration addAnswerInfoEventHandler(
			AnswerInfoEventHandler handler) {
		return handlerManager.addHandler(AnswerInfoEvent.TYPE, handler);
	}

	public HandlerRegistration addAnswerEventHandler(AnswerEventHandler handler) {
		return handlerManager.addHandler(AnswerEvent.TYPE, handler);
	}
}
