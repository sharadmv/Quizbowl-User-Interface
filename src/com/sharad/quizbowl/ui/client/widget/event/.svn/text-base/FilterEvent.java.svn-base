package com.sharad.quizbowl.ui.client.widget.event;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.event.shared.GwtEvent;

public class FilterEvent extends GwtEvent<FilterEventHandler> {

	public static final Type<FilterEventHandler> TYPE = new Type<FilterEventHandler>();
	private HashMap<String, List<String>> parameters;

	public FilterEvent(HashMap<String, List<String>> parameters) {
		super();
		this.parameters = parameters;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<FilterEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FilterEventHandler handler) {
		handler.onTossupsReceived(this);
	}

	/**
	 * @return the parameters
	 */
	public HashMap<String, List<String>> getParameters() {
		return parameters;
	}

}
