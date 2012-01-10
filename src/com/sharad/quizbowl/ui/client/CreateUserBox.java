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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sharad.quizbowl.ui.client.widget.event.ChangeWindowEvent;
import com.sharad.quizbowl.ui.client.widget.event.ChangeWindowEventHandler;
import com.sharad.quizbowl.ui.client.widget.event.LoginEvent;
import com.sharad.quizbowl.ui.client.widget.event.LoginEventHandler;

public class CreateUserBox extends com.smartgwt.client.widgets.Window {
	private static CreateUserBoxUiBinder uiBinder = GWT
			.create(CreateUserBoxUiBinder.class);
	@UiField
	TextBox userBox, passwordBox, confirmPasswordBox;
	@UiField
	Button create;
	private HandlerManager handlerManager;
	@UiField
	Anchor createAccount;

	interface CreateUserBoxUiBinder extends UiBinder<Widget, CreateUserBox> {
	}

	public CreateUserBox() {
		handlerManager = new HandlerManager(this);
		setIsModal(true);
		setAutoSize(true);

		setTitle("Create Account");
		setAutoCenter(true);
		addItem(uiBinder.createAndBindUi(this));

		create.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				createUser();
			}

		});
		userBox.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == 13) {
					createUser();
				} else if (event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
					passwordBox.setFocus(true);
				}
			}

		});
		passwordBox.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == 13) {
					createUser();
				} else if (event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
					confirmPasswordBox.setFocus(true);
				}
			}

		});
		confirmPasswordBox.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == 13) {
					createUser();
				} else if (event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
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

	private void createUser() {
		if (passwordBox.getText().equals(confirmPasswordBox.getText())) {
			LoginEvent event = new LoginEvent(userBox.getText(),
					passwordBox.getText());
			fireEvent(event);
		} else {
			Window.alert("Passwords do not match!");
		}
	}
}
