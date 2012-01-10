package com.sharad.quizbowl.ui.client.widget.event;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.sharad.quizbowl.ui.client.json.tossup.Tossup;

public class SortEvent extends GwtEvent<SortEventHandler> {

	public static final Type<SortEventHandler> TYPE = new Type<SortEventHandler>();
	private Comparator<Tossup> comparator;

	public SortEvent(Comparator<Tossup> comparator) {
		super();
		this.comparator = comparator;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SortEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SortEventHandler handler) {
		handler.onSort(this);
	}

	/**
	 * @return the parameters
	 */
	public Comparator<Tossup> getComparator() {
		return comparator;
	}

}
