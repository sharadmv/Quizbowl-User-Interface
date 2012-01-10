package com.sharad.quizbowl.ui.client.widget.event;

import com.google.gwt.event.shared.GwtEvent;

public class ChangeWindowEvent extends GwtEvent<ChangeWindowEventHandler> {
	public static final Type<ChangeWindowEventHandler> TYPE = new Type<ChangeWindowEventHandler>();
	private String window;

	public ChangeWindowEvent(String window) {
		super();
		this.window = window;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ChangeWindowEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ChangeWindowEventHandler handler) {
		handler.onWindowChange(this);
	}

	public String getWindow() {
		return window;
	}
}
