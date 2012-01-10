package com.sharad.quizbowl.ui.client.widget;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.sharad.quizbowl.ui.client.QuizbowlUI;
import com.sharad.quizbowl.ui.client.json.tossup.Tossup;

public class CategoryWidget extends Composite {
	HTML text;
	Anchor wrong;
	ListBox categories;
	HorizontalPanel wrapper;
	Tossup t;

	public CategoryWidget(Tossup tossup) {
		HorizontalPanel main = new HorizontalPanel();
		wrong = new Anchor("Incorrect category?");
		t = tossup;
		wrapper = new HorizontalPanel();
		text = new HTML("{" + t.getCategory() + "}");
		categories = new ListBox();
		for (String cat : QuizbowlUI.CATEGORIES_LIST) {
			categories.addItem(cat);
		}
		categories.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				changeCategory(
						categories.getItemText(categories.getSelectedIndex()),
						t.getPkey());
			}
		});
		wrong.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				wrapper.remove(text);
				wrapper.add(categories);
				for (int i = 0; i < categories.getItemCount(); i++) {
					if (categories.getItemText(i).equals(t.getCategory())) {
						categories.setSelectedIndex(i);
					}
				}
			}
		});
		wrapper.add(text);
		main.setStyleName("categoryWidget");
		text.setStyleName("categoryText");
		wrong.setStyleName("categoryLink");
		categories.setStyleName("categoryBox");
		main.add(wrapper);
		wrapper.setHeight("100%");
		main.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		main.add(wrong);
		initWidget(main);
	}

	public void changeCategory(final String category, String pk) {
		JsonpRequestBuilder changer = new JsonpRequestBuilder();
		changer.requestObject(QuizbowlUI.SERVER_URL
				+ "/changeCategory?category=" + category + "&pKey="
				+ QuizbowlUI.replaceInvalidCharacters(pk)
				+ "&alt=json-in-script", new AsyncCallback<Result>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Result result) {
				if (result.getMessage().equals("success")) {
					wrapper.remove(categories);
					t.setCategory(category);
					text.setText("{" + category + "}");
					wrapper.add(text);
				} else {
					Window.alert("Category change error: " + result.getError());
				}
			}

		});

	}

}
