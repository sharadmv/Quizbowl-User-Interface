package com.sharad.quizbowl.ui.client.widget.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.sharad.quizbowl.ui.client.widget.AnswerInfo;

public class NewTossupEvent extends GwtEvent<NewTossupEventHandler> {
	public static final Type<NewTossupEventHandler> TYPE = new Type<NewTossupEventHandler>();
	private int n;

	public NewTossupEvent(int n) {
		super();
		this.n = n;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<NewTossupEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(NewTossupEventHandler handler) {
		handler.onNewTossup(this);
	}

	/**
	 * @return the parameters
	 */
	public int getN() {
		return n;
	}

}
