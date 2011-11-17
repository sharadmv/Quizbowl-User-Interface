package com.sharad.quizbowl.ui.client.widget.event;

import com.google.gwt.event.shared.GwtEvent;
import com.sharad.quizbowl.ui.client.widget.AnswerInfo;

public class AnswerInfoEvent extends GwtEvent<AnswerInfoEventHandler> {
	public static final Type<AnswerInfoEventHandler> TYPE = new Type<AnswerInfoEventHandler>();
	private AnswerInfo info;

	public AnswerInfoEvent(AnswerInfo info) {
		super();
		this.info = info;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AnswerInfoEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AnswerInfoEventHandler handler) {
		handler.onAnswerInfoReceived(this);
	}

	/**
	 * @return the parameters
	 */
	public AnswerInfo getInfo() {
		return info;
	}

}
