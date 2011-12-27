package com.sharad.quizbowl.ui.client.widget;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sharad.quizbowl.ui.client.widget.event.FilterResultEvent;
import com.sharad.quizbowl.ui.client.widget.event.FilterResultEventHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

public class FilterBar extends Composite {
	private VerticalPanel main;
	private HandlerManager handlerManager;
	private CheckDropBox<String> dBox, tBox, cBox;
	public static final String ALL_STRING = "All";

	public FilterBar() {
		handlerManager = new HandlerManager(this);
		main = new VerticalPanel();
		// reconfigure(difficulties, tournaments, categories);
		main.setWidth("100%");
		initWidget(main);
	}

	public void reconfigure(Set<String> difficulties, Set<String> tournaments,
			Set<String> categories) {
		VerticalPanel temp = new VerticalPanel();
		main.clear();
		HTML filterLabel = new HTML("Filter by:");
		main.add(filterLabel);
		List<String> list;
		filterLabel.setStyleName("filterLabel");
		list = new ArrayList<String>();
		list.add(ALL_STRING);
		for (String string : difficulties) {
			list.add(string);
		}
		dBox = new CheckDropBox<String>(list, "Difficulty");

		dBox.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				fireChangeEvent();

			}

		});
		dBox.setValue(ALL_STRING);
		temp.add(dBox);
		main.add(temp);
		temp.setStyleName("filterBarBox");
		temp = new VerticalPanel();
		list = new ArrayList<String>();
		list.add(ALL_STRING);
		for (String string : tournaments) {
			list.add(string);
		}
		tBox = new CheckDropBox<String>(list, "Tournament");

		tBox.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				fireChangeEvent();

			}

		});
		tBox.setValue(ALL_STRING);

		temp.add(tBox);
		main.add(temp);
		temp.setStyleName("filterBarBox");
		temp = new VerticalPanel();
		list = new ArrayList<String>();
		for (String string : categories) {
			list.add(string);
		}
		list.add(ALL_STRING);
		cBox = new CheckDropBox<String>(list,"Category");

		for (String string : categories) {
			list.add(string);
		}
		cBox.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				fireChangeEvent();

			}

		});
		temp.add(cBox);
		cBox.setValue(ALL_STRING);

		main.add(temp);
		temp.setStyleName("filterBarBox");
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	public HandlerRegistration addFilterResultEventHandler(
			FilterResultEventHandler handler) {
		return handlerManager.addHandler(FilterResultEvent.TYPE, handler);
	}

	private void fireChangeEvent() {

		Set<String> difficulties = new HashSet<String>(dBox.getItems());
		Set<String> tournaments = new HashSet<String>(tBox.getItems());
		Set<String> categories = new HashSet<String>(cBox.getItems());
		FilterResultEvent event = new FilterResultEvent(difficulties,
				tournaments, categories);
		fireEvent(event);
	}
}