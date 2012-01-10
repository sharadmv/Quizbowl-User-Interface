package com.sharad.quizbowl.ui.client.widget;

import java.util.Comparator;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sharad.quizbowl.ui.client.json.tossup.Tossup;
import com.sharad.quizbowl.ui.client.widget.event.SortEvent;
import com.sharad.quizbowl.ui.client.widget.event.SortEventHandler;

public class SortBar extends Composite {
	private VerticalPanel main;
	private HandlerManager handlerManager;

	public SortBar() {
		handlerManager = new HandlerManager(this);
		main = new VerticalPanel();
		main.setWidth("100%");
		main.clear();
		HTML filterLabel = new HTML("Sort by:");
		filterLabel.setStyleName("filterLabel");

		main.add(filterLabel);
		RadioButton tourButton = new RadioButton("group", "Tournament");
		RadioButton ratingButton = new RadioButton("group", "Rating");
		tourButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					Comparator<Tossup> c = new Comparator<Tossup>() {

						@Override
						public int compare(Tossup o1, Tossup o2) {
							int c = new Integer(o2.getYear())
									.compareTo(new Integer(o1.getYear()));
							return c == 0 ? o1.getTournament().compareTo(
									o2.getTournament()) : c;
						}

					};
					fireEvent(new SortEvent(c));
				}

			}

		});
		ratingButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					Comparator<Tossup> c = new Comparator<Tossup>() {

						@Override
						public int compare(Tossup o1, Tossup o2) {
							return (new Integer(o2.getRating())
									.compareTo(new Integer(o1.getRating())));
						}

					};
					fireEvent(new SortEvent(c));
				}

			}

		});
		main.add(tourButton);
		main.add(ratingButton);
		tourButton.setValue(true, false);
		initWidget(main);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	public HandlerRegistration addSortResultEventHandler(
			SortEventHandler handler) {
		return handlerManager.addHandler(SortEvent.TYPE, handler);
	}

}