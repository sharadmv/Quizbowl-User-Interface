package com.sharad.quizbowl.ui.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sharad.quizbowl.ui.client.HomeWidget;

public class Chatroom extends Composite {
	@UiField
	public static VerticalPanel chatPanel;
	@UiField
	public static ScrollPanel scrollPanel;
	@UiField
	public TextBox inputBox;
	@UiField
	Button sendButton;
	@UiField
	public VerticalPanel wrapper;
	@UiField
	public static Label users;
	private static ChatroomUiBinder uiBinder = GWT
			.create(ChatroomUiBinder.class);

	interface ChatroomUiBinder extends UiBinder<Widget, Chatroom> {
	}

	public Chatroom() {
		initWidget(uiBinder.createAndBindUi(this));
		scrollPanel.setStyleName("scrollPanel");
		chatPanel.setStyleName("chatPanel");

		Chatroom.exportStaticMethods();
		sendButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				sendMessage();
			}
		});
		inputBox.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == 13) {
					sendMessage();
				}

			}

		});
		wrapper.setStyleName("chatroomWrapper");
	}

	protected void sendMessage() {
		if (HomeWidget.LOGGED_IN) {
			if (!inputBox.getText().equals("")) {
				distributeMessage(inputBox.getText());
				inputBox.setText("");
			}

		} else {
			Window.alert("Please login first");
		}

	}

	public static void displayServerMessage(String message) {
		Label label = new Label(message);
		label.setStyleName("serverMessage");
		displayMessage(label);
	}

	public static void displayChatMessage(String user, String message) {
		Label userLabel = new Label(user + ":");
		Label messageLabel = new Label(message);

		userLabel.setStyleName("userMessage");
		messageLabel.setStyleName("chatMessage");
		HorizontalPanel chat = new HorizontalPanel();
		chat.setStyleName("chat");
		chat.add(userLabel);
		chat.add(messageLabel);
		displayMessage(chat);
	}

	private static void displayMessage(Widget message) {
		chatPanel.add(message);
		scrollPanel.scrollToBottom();
	}

	public static native void exportStaticMethods() /*-{
		$wnd.now.displayServerMessage = $entry(@com.sharad.quizbowl.ui.client.widget.Chatroom::displayServerMessage(Ljava/lang/String;));
		$wnd.now.displayChatMessage = $entry(@com.sharad.quizbowl.ui.client.widget.Chatroom::displayChatMessage(Ljava/lang/String;Ljava/lang/String;));
		$wnd.now.updateUsers = $entry(@com.sharad.quizbowl.ui.client.widget.Chatroom::setUsers(Ljava/lang/String;));
	}-*/;

	public static native void distributeMessage(String message) /*-{
		$wnd.now.distributeMessage(message);
	}-*/;

	public static void setUsers(String text) {
		users.setText(text);
	}

}
