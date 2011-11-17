package com.sharad.quizbowl.ui.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NowJSWidget extends Composite {
	VerticalPanel main;
	static VerticalPanel chatPanel;
	TextBox text;

	public NowJSWidget() {
		NowJSWidget.exportStaticMethod();
		main = new VerticalPanel();
		// setNowName(Window.prompt("What's your name?", ""));
		setNowName("Hello");
		chatPanel = new VerticalPanel();
		main.add(chatPanel);
		HorizontalPanel panel = new HorizontalPanel();
		text = new TextBox();
		Button submit = new Button("Submit");
		submit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				distribute(text.getText());
				text.setText("");
			}

		});
		panel.add(text);
		panel.add(submit);
		main.add(panel);
		initWidget(main);
	}

	private static native void setNowName(String prompt)/*-{
		$wnd.now.name = prompt;
	}-*/;

	public static void displayText(String text) {
		chatPanel.add(new Label(text));

	}

	public static native void distribute(String text) /*-{
		$wnd.now.distributeMessage(text);
	}-*/;

	public static native void exportStaticMethod() /*-{
		$wnd.displayText = $entry(@com.sharad.quizbowl.ui.client.widget.NowJSWidget::displayText(Ljava/lang/String;));
	}-*/;
}
