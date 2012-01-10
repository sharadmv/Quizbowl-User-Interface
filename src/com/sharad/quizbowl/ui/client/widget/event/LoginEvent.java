package com.sharad.quizbowl.ui.client.widget.event;

import com.google.gwt.event.shared.GwtEvent;

public class LoginEvent extends GwtEvent<LoginEventHandler> {
	public static final Type<LoginEventHandler> TYPE = new Type<LoginEventHandler>();
	private String user, password;

	public LoginEvent(String user, String password) {
		super();
		this.user = user;
		this.password = password;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<LoginEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginEventHandler handler) {
		handler.onLogin(this);
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}
}
