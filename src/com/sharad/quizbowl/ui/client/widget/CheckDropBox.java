package com.sharad.quizbowl.ui.client.widget;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

public class CheckDropBox<D> extends Composite {
	private SelectItem box;

	public CheckDropBox(List<D> list) {
		this(list, "", true, false);
	}

	public CheckDropBox(List<D> list, String title) {
		this(list, title, true);
	}

	public CheckDropBox(List<D> list, String title, boolean multi) {
		this(list, title, multi, true);
	}

	public CheckDropBox(List<D> list, String title, boolean multi,
			boolean showTitle) {
		box = new SelectItem();

		box.setMultiple(true);
		if (multi)
			box.setMultipleAppearance(MultipleAppearance.PICKLIST);
		else
			box.setMultiple(false);
		LinkedHashMap<String, String> yearsMap = new LinkedHashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			yearsMap.put(list.get(i).toString(), list.get(i).toString());
		}
		box.setValueMap(yearsMap);
		DynamicForm form = new DynamicForm();

		form.setItems(box);
		if (showTitle) {
			box.setTitle(title);
		}
		box.setTitleOrientation(TitleOrientation.TOP);
		initWidget(form);
	}

	public List<String> getItems() {
		return Arrays.asList(box.getValues());
	}

	public void setValue(String value) {
		box.setValue(value);
	}

	public HandlerRegistration addChangedHandler(ChangedHandler handler) {
		return box.addChangedHandler(handler);
	}
}
