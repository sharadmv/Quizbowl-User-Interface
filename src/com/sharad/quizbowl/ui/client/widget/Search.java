package com.sharad.quizbowl.ui.client.widget;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.SplitResult;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sharad.quizbowl.ui.client.util.Resources;
import com.sharad.quizbowl.ui.client.util.guava.Joiner;
import com.sharad.quizbowl.ui.client.widget.event.FilterEvent;
import com.sharad.quizbowl.ui.client.widget.event.FilterEventHandler;
import com.smartgwt.client.widgets.Window;

public class Search extends Composite {
	private HandlerManager handlerManager;
	private Configuration configuration;
	public static final List<String> POSSIBLE_PARAMETERS = Arrays
			.asList(new String[] { "year", "tournament", "difficulty", "round",
					"category", "random", "limit", "answer", "question",
					"condition" });
	private static SimpleSearchUiBinder uiVertical = GWT
			.create(SimpleSearchUiBinder.class);
	private FilterBox advancedBox;
	private Browser browseBox;

	@UiTemplate("SimpleSearch.ui.xml")
	interface SimpleSearchUiBinder extends UiBinder<Widget, Search> {
	}

	private static SimpleSearchHorizontalUiBinder uiHorizontal = GWT
			.create(SimpleSearchHorizontalUiBinder.class);

	@UiTemplate("SimpleSearchHorizontal.ui.xml")
	interface SimpleSearchHorizontalUiBinder extends UiBinder<Widget, Search> {
	}

	@UiField(provided = true)
	public Label title;
	public static final String TITLE_STRING = "Quizbowl Central";

	public enum Configuration {
		HORIZONTAL(uiHorizontal), VERTICAL(uiVertical);
		private UiBinder<Widget, Search> uiBinder;

		private Configuration(UiBinder<Widget, Search> uiBinder) {
			this.uiBinder = uiBinder;
		}

		public UiBinder<Widget, Search> getUiBinder() {
			return uiBinder;
		}
	}

	public static final Configuration DEFAULT_CONFIGURATION = Configuration.VERTICAL;

	public Search(JsArrayInteger years, JsArrayString tournaments,
			JsArrayString difficulties, JsArrayString categories) {
		this("Search", years, tournaments, difficulties, categories);
	}

	// @UiField(provided = true)
	Image logo;
	@UiField(provided = true)
	public Image loading = new Image(Resources.IMAGE_LOADING);
	@UiField(provided = true)
	public Button button;
	@UiField
	public TextBox searchBox;
	public HorizontalPanel main;
	@UiField(provided = true)
	Anchor advanced, browse;
	Window advancedDialog, browseDialog;

	public TextBox getSearchBox() {
		return searchBox;
	}

	public Search(String buttonText, JsArrayInteger years,
			JsArrayString tournaments, JsArrayString difficulties,
			JsArrayString categories) {
		advanced = new Anchor("Advanced Search");
		advanced.setStyleName("searchLink");
		browse = new Anchor("Browse");
		browse.setStyleName("searchLink");
		logo = new Image(Resources.INSTANCE.logoBig());
		title = new Label(TITLE_STRING);
		button = new Button(buttonText);
		main = new HorizontalPanel();
		main.setWidth("100%");
		main.setHeight("100%");
		handlerManager = new HandlerManager(this);
		button.setText(buttonText);
		loading = new Image(Resources.INSTANCE.white());
		loading.setHeight("16px");
		loading.setWidth("16px");
		// loading.setVisible(false);

		initWidget(main);
		advancedBox = new FilterBox(years, tournaments, difficulties,
				categories, true, "Keyword", "Search");
		advancedBox.addFilterEventHandler(new FilterEventHandler() {

			@Override
			public void onTossupsReceived(FilterEvent event) {
				advancedDialog.hide();
				String query = "", delimiter = "";
				for (Map.Entry<String, List<String>> e : event.getParameters()
						.entrySet()) {
					if (e.getValue().size() > 0) {
						query += delimiter + e.getKey() + ":\""
								+ Joiner.on("|").join(e.getValue()) + "\"";
						delimiter = " ";
					}
				}
				searchBox.setText(query);
				doSearch();
			}

		});
		browseBox = new Browser(difficulties);
		advancedDialog = new Window();
		advancedDialog.setTitle("Advanced Search");
		advancedDialog.addItem(advancedBox);
		advancedDialog.setAutoSize(true);
		advancedDialog.setCanDragReposition(true);
		advancedDialog.setCanDragResize(true);

		advanced.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				advancedDialog.show();
				advancedBox.getField().focus();
				advancedDialog.centerInPage();
			}

		});
		browseDialog = new Window();
		browseDialog.setTitle("Browse");
		browseDialog.addItem(browseBox);
		browseDialog.setAutoSize(true);
		browseDialog.setCanDragReposition(true);
		browseDialog.setCanDragResize(true);

		browse.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				browseDialog.show();
				browseDialog.centerInPage();
			}

		});
		browseBox.addFilterEventHandler(new FilterEventHandler() {

			@Override
			public void onTossupsReceived(FilterEvent event) {
				browseDialog.hide();
				String query = "", delimiter = "";
				for (Map.Entry<String, List<String>> e : event.getParameters()
						.entrySet()) {
					if (e.getValue().size() > 0) {
						query += delimiter + e.getKey() + ":\""
								+ Joiner.on("|").join(e.getValue()) + "\"";
						delimiter = " ";
					}
				}
				searchBox.setText(query);
				doSearch();
			}

		});

	}

	@UiHandler("button")
	public void onClick(ClickEvent e) {
		doSearch();
	}

	@UiHandler("searchBox")
	public void onKeyPress(KeyPressEvent e) {
		if (e.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)
			doSearch();
	}

	public void doSearch() {
		HashMap<String, List<String>> parameters = new HashMap<String, List<String>>();
		String terms = searchBox.getValue().trim();
		RegExp term = RegExp.compile("[a-zA-Z]+:");
		SplitResult params = term.split(terms);
		if (params.length() > 1)
			for (int i = 0; i < params.length(); i++) {
				String value = params.get(i).trim();
				if (value != "" && value.length() != 0) {
					String param = terms.substring(0, terms.indexOf(":"))
							.trim();
					if (POSSIBLE_PARAMETERS.contains(param)) {
						if (value.matches("\".*\".*")) {

							value = value.substring(1, value.indexOf("\"", 1));
							terms = terms.substring(terms.indexOf("\"",
									terms.indexOf(value)) + 1);
						} else {
							if (value.indexOf(" ") != -1) {

								value = value.substring(0, value.indexOf(" "));

								terms = terms.substring(terms.indexOf(" ",
										terms.indexOf(value)));

							} else {
								terms = terms.substring(terms.indexOf(value)
										+ value.length());

							}

						}

						parameters.put(param,
								Arrays.asList(new String[] { value.trim() }));
					}
				}
			}
		if (!parameters.keySet().contains("answer"))
			parameters.put("answer", Arrays.asList(new String[] { terms }));
		FilterEvent e = new FilterEvent(parameters);
		fireEvent(e);
	}

	@Override
	public void fireEvent(GwtEvent<?> e) {
		handlerManager.fireEvent(e);
	}

	public HandlerRegistration addFilterEventHandler(FilterEventHandler handler) {
		return handlerManager.addHandler(FilterEvent.TYPE, handler);
	}

	public Image getLoading() {
		return loading;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration config) {
		configuration = config;
		button.removeStyleName("submitButtonVertical");
		button.removeStyleName("submitButtonHorizontal");
		if (configuration.equals(Configuration.VERTICAL)) {
			logo.setResource(Resources.INSTANCE.logoBig());
			title.setStyleName("titleBig");
			main.setStyleName("simpleSearchVertical");
			button.addStyleName("submitButtonVertical");

		} else {
			logo.setResource(Resources.INSTANCE.logoSmall());
			title.setStyleName("titleSmall");
			main.setStyleName("simpleSearchHorizontal");
			button.addStyleName("submitButtonHorizontal");

		}
	}

	public Image getLogo() {
		return logo;
	}
}
