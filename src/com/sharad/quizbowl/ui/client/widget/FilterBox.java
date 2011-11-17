package com.sharad.quizbowl.ui.client.widget;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sharad.quizbowl.ui.client.util.JsUtil;
import com.sharad.quizbowl.ui.client.util.Resources;
import com.sharad.quizbowl.ui.client.widget.event.FilterEvent;
import com.sharad.quizbowl.ui.client.widget.event.FilterEventHandler;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class FilterBox extends Composite {
	JsArrayInteger years;
	JsArrayString tournaments, difficulties, categories;

	@UiField(provided = true)
	CheckDropBox<Integer> yearsBox;

	@UiField(provided = true)
	CheckDropBox<String> tournamentsBox, difficultiesBox, categoriesBox,
			conditionsBox;

	DynamicForm field;

	Button submit;

	// @UiField(provided = true)
	Image logo;
	@UiField(provided = true)
	Label titleLabel;

	HandlerManager handlerManager;
	private TextItem fieldItem;

	private static FilterBoxUiBinder uiBinder = GWT
			.create(FilterBoxUiBinder.class);

	@UiField
	HorizontalPanel horizontalPanel;

	interface FilterBoxUiBinder extends UiBinder<Widget, FilterBox> {
	}

	public FilterBox(JsArrayInteger years, JsArrayString tournaments,
			JsArrayString difficulties, JsArrayString categories) {
		this(years, tournaments, difficulties, categories, "", "Submit");
	}

	public FilterBox(JsArrayInteger years, JsArrayString tournaments,
			JsArrayString difficulties, JsArrayString categories, String title,
			String buttonTitle) {
		this(years, tournaments, difficulties, categories, true, title,
				buttonTitle);

	}

	public FilterBox(JsArrayInteger years, JsArrayString tournaments,
			JsArrayString difficulties, JsArrayString categories,
			boolean includeField) {
		this(years, tournaments, difficulties, categories, true, "", "");
	}

	public FilterBox(JsArrayInteger years, JsArrayString tournaments,
			JsArrayString difficulties, JsArrayString categories,
			boolean includeField, String title, String buttonTitle) {
		logo = new Image(Resources.INSTANCE.logoSmall());
		titleLabel = new Label(SimpleSearch.TITLE_STRING);
		titleLabel.setStyleName("titleSmall");
		handlerManager = new HandlerManager(this);
		this.years = years;
		this.tournaments = tournaments;
		this.difficulties = difficulties;
		this.categories = categories;
		yearsBox = new CheckDropBox<Integer>(
				JsUtil.getListFromJs(years, "int"), "Years");
		tournamentsBox = new CheckDropBox<String>(JsUtil.getListFromJs(
				tournaments, "str"), "Tournaments");
		difficultiesBox = new CheckDropBox<String>(JsUtil.getListFromJs(
				difficulties, "str"), "Difficulties");
		categoriesBox = new CheckDropBox<String>(JsUtil.getListFromJs(
				categories, "str"), "Categories");
		conditionsBox = new CheckDropBox<String>(Arrays.asList(new String[] {
				"Answer", "Question", "All" }), "Condition", false);
		conditionsBox.setValue("Answer");
		fieldItem = new TextItem();
		fieldItem.setTitle(title);

		submit = new Button(buttonTitle);
		fieldItem.setTitleOrientation(TitleOrientation.TOP);
		submit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				HashMap<String, List<String>> parameters = new HashMap<String, List<String>>();
				parameters.put("year", yearsBox.getItems());
				parameters.put("tournament", tournamentsBox.getItems());
				parameters.put("category", categoriesBox.getItems());
				parameters.put("difficulty", difficultiesBox.getItems());
				String fieldName = "answer";
				if (conditionsBox.getItems().get(0).equals("Question"))
					fieldName = "question";
				else if (conditionsBox.getItems().get(0).equals("All"))
					parameters.put("condition",
							Arrays.asList(new String[] { "all" }));
				parameters.put(fieldName, Arrays.asList(fieldItem
						.getValueAsString() == null ? new String[] {}
						: new String[] { fieldItem.getValueAsString() }));
				FilterEvent e = new FilterEvent(parameters);
				fireEvent(e);

			}

		});
		field = new DynamicForm();
		field.setFields(fieldItem);

		initWidget(uiBinder.createAndBindUi(this));
		if (includeField) {
			horizontalPanel.add(field);
			horizontalPanel.setCellVerticalAlignment(field,
					HasVerticalAlignment.ALIGN_BOTTOM);
		}
		submit.addStyleName("generateButton");
		horizontalPanel.add(submit);
		horizontalPanel.setCellVerticalAlignment(submit,
				HasVerticalAlignment.ALIGN_BOTTOM);
		setStylePrimaryName("simpleSearchHorizontal");

	}

	@Override
	public void fireEvent(GwtEvent<?> e) {
		handlerManager.fireEvent(e);
	}

	public HandlerRegistration addFilterEventHandler(FilterEventHandler handler) {
		return handlerManager.addHandler(FilterEvent.TYPE, handler);
	}
}
