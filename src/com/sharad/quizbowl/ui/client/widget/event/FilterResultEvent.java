package com.sharad.quizbowl.ui.client.widget.event;

import java.util.Set;

import com.google.gwt.event.shared.GwtEvent;

public class FilterResultEvent extends GwtEvent<FilterResultEventHandler> {

	public static final Type<FilterResultEventHandler> TYPE = new Type<FilterResultEventHandler>();
	private Set<String> tournaments, difficulties, categories;

	public FilterResultEvent(Set<String> difficulties, Set<String> tournaments,
			Set<String> categories) {
		super();
		this.difficulties = difficulties;
		this.tournaments = tournaments;
		this.categories = categories;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<FilterResultEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FilterResultEventHandler handler) {
		handler.onFilter(this);
	}

	public Set<String> getTournaments() {
		return tournaments;
	}

	public Set<String> getDifficulties() {
		return difficulties;
	}

	public Set<String> getCategories() {
		return categories;
	}

}
