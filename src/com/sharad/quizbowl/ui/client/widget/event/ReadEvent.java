package com.sharad.quizbowl.ui.client.widget.event;

import com.google.gwt.event.shared.GwtEvent;
import com.sharad.quizbowl.ui.client.json.tossup.Tossup;

public class ReadEvent extends GwtEvent<ReadEventHandler> {

	public static final Type<ReadEventHandler> TYPE = new Type<ReadEventHandler>();
	private Tossup tossup;

	public ReadEvent(Tossup tossup) {
		super();
		this.tossup = tossup;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ReadEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ReadEventHandler handler) {
		handler.onRead(this);
	}

	/**
	 * @return the parameters
	 */
	public Tossup getTossup() {
		return tossup;
	}

}
