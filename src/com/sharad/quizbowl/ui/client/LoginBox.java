package com.sharad.quizbowl.ui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sharad.quizbowl.ui.client.widget.event.ChangeWindowEvent;
import com.sharad.quizbowl.ui.client.widget.event.ChangeWindowEventHandler;
import com.sharad.quizbowl.ui.client.widget.event.LoginEvent;
import com.sharad.quizbowl.ui.client.widget.event.LoginEventHandler;
import com.smartgwt.client.widgets.Window;

public class LoginBox extends Window {
	private static LoginBoxUiBinder uiBinder = GWT
			.create(LoginBoxUiBinder.class);
	@UiField
	TextBox userBox, passwordBox;
	@UiField
	Button login;
	@UiField
	Anchor createAccount;
	private HandlerManager handlerManager;

	interface LoginBoxUiBinder extends UiBinder<Widget, LoginBox> {
	}

	public LoginBox() {
		handlerManager = new HandlerManager(this);
		setIsModal(true);
		setTitle("Log In");
		setAutoSize(true);
		setAutoCenter(true);
		addItem(uiBinder.createAndBindUi(this));
		login.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				login();
			}

		});
		userBox.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == 13) {
					login();
				} else if (event.getNativeKeyCode()==KeyCodes.KEY_TAB){
					passwordBox.setFocus(true);
				}

			}

		});
		passwordBox.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == 13) {
					login();
				} else if (event.getNativeKeyCode()==KeyCodes.KEY_TAB){
					userBox.setFocus(true);
				}
			}

		});
		createAccount.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ChangeWindowEvent e = new ChangeWindowEvent("createAccount");
				fireEvent(e);
			}
		});
	}

	public void setFocus() {
		userBox.setFocus(true);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	public HandlerRegistration addLoginEventHandler(LoginEventHandler handler) {
		return handlerManager.addHandler(LoginEvent.TYPE, handler);
	}

	public HandlerRegistration addChangeWindowEventHandler(
			ChangeWindowEventHandler handler) {
		return handlerManager.addHandler(ChangeWindowEvent.TYPE, handler);
	}

	private void login() {
		LoginEvent event = new LoginEvent(userBox.getText(),
				passwordBox.getText());
		fireEvent(event);
	}

}