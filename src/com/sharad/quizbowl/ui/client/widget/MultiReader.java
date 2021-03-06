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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sharad.quizbowl.ui.client.HomeWidget;
import com.sharad.quizbowl.ui.client.json.tossup.Tossup;
import com.sharad.quizbowl.ui.client.widget.event.AnswerEvent;
import com.sharad.quizbowl.ui.client.widget.event.AnswerEventHandler;
import com.sharad.quizbowl.ui.client.widget.event.AnswerInfoEvent;
import com.sharad.quizbowl.ui.client.widget.event.AnswerInfoEventHandler;
import com.sharad.quizbowl.ui.client.widget.event.NewTossupEvent;
import com.sharad.quizbowl.ui.client.widget.event.NewTossupEventHandler;
import com.sharad.quizbowl.ui.client.widget.event.ReadEvent;
import com.sharad.quizbowl.ui.client.widget.event.ReadEventHandler;

public class MultiReader extends Composite {

	private static MultiReaderUiBinder uiBinder = GWT
			.create(MultiReaderUiBinder.class);

	interface MultiReaderUiBinder extends UiBinder<Widget, MultiReader> {
	}

	VerticalPanel main;
	TextBox text;
	private static final String ACCEPT_DELIMITER = "|";
	@UiField
	public static TextArea readArea;
	private int count;
	private int wordCount;

	private List<Tossup> tossups;
	private static HandlerManager handlerManager;
	@UiField
	public static Button buzzButton;
	@UiField
	public static TextBox answerBox;
	@UiField
	public VerticalPanel mainPanel;
	public Tossup currentTossup;
	public static String currentString = "";
	public static final String BUZZ_TEXT = "Buzz";
	public static final String CONTINUE_TEXT = "Continue";

	interface ReaderUiBinder extends UiBinder<Widget, Reader> {

	}

	public void read(Tossup t) {
		ReadEvent e = new ReadEvent(t);
		fireEvent(e);
	}

	private int convertSliderToSleed(float value) {
		return (int) (2019.19 - 19.1919 * value);
	}

	public MultiReader() {
		MultiReader.exportStaticMethods();
		handlerManager = new HandlerManager(this);
		initWidget(uiBinder.createAndBindUi(this));
		answerBox.setVisible(false);
		answerBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == 13
						&& !answerBox.getText().trim().equals("")) {
					answer(answerBox.getText().trim());
					// showAnswer(true, false);
				}

			}

		});
		readArea.setStyleName("readArea");
		readArea.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == 32) {
					if (HomeWidget.LOGGED_IN)
						buzz();

				}

			}

		});
		buzzButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (HomeWidget.LOGGED_IN)
					buzz();
				// getNewTossup();
			}
		});
		export();
	}

	private static void answerQuestion() {
		buzzButton.setVisible(false);
		answerBox.setVisible(true);
		answerBox.setFocus(true);
		answerBox.setAlignment(TextAlignment.CENTER);
		answerBox.setText("");
	}

	public static void unbuzz() {
		buzzButton.setVisible(true);
		answerBox.setVisible(false);
		setBuzzed(false);
		setCanAnswer(true);
		currentString = "";
		delimiter = "";
	}

	public static native void answer(String answer)/*-{
		$wnd.now.answer(answer);
	}-*/;

	public static native void buzz()/*-{
		$wnd.now.buzz();
	}-*/;

	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	public HandlerRegistration addReadEventHandler(
			ReadEventHandler readEventHandler) {
		return handlerManager.addHandler(ReadEvent.TYPE, readEventHandler);
	}

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

	private static native void setNowName(String prompt)/*-{
		$wnd.now.name = prompt;
	}-*/;

	private static String delimiter = "";

	public static void displayQuestion(String text) {
		currentString += delimiter + text;
		readArea.setText(currentString);
		delimiter = " ";
	}

	public static native void setBuzzed(boolean buzzed)/*-{
		$wnd.now.buzzed = buzzed;
	}-*/;

	public static native boolean canAnswer()/*-{
		return $wnd.now.canAnswer;
	}-*/;

	public static native void setCanAnswer(boolean bool)/*-{
		$wnd.now.canAnswer = bool;
	}-*/;

	public static void completeQuestion(String text) {
		readArea.setText(text);
	}

	public static native void distribute(String text) /*-{
		$wnd.now.distributeMessage(text);
	}-*/;

	public static native void exportStaticMethods() /*-{
		var that = this;
		$wnd.now.updateQuestion = $entry(@com.sharad.quizbowl.ui.client.widget.MultiReader::displayQuestion(Ljava/lang/String;));
		$wnd.now.answerQuestion = $entry(@com.sharad.quizbowl.ui.client.widget.MultiReader::answerQuestion());
		$wnd.now.completeQuestion = $entry(@com.sharad.quizbowl.ui.client.widget.MultiReader::completeQuestion(Ljava/lang/String;));		
		$wnd.now.unbuzz = $entry(@com.sharad.quizbowl.ui.client.widget.MultiReader::unbuzz());		

	}-*/;

	public native void export() /*-{
		var that = this;
		$wnd.now.read = $entry(function(t) {
			that.@com.sharad.quizbowl.ui.client.widget.MultiReader::read(Lcom/sharad/quizbowl/ui/client/json/tossup/Tossup;)(t)
		});

	}-*/;

}
