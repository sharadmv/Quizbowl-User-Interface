package com.sharad.quizbowl.ui.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sharad.quizbowl.ui.client.json.tossup.Tossup;
import com.sharad.quizbowl.ui.client.json.tossup.TossupsPackage;
import com.sharad.quizbowl.ui.client.util.Resources;
import com.sharad.quizbowl.ui.client.util.guava.Joiner;
import com.sharad.quizbowl.ui.client.widget.AnswerInfoPanel;
import com.sharad.quizbowl.ui.client.widget.FilterBar;
import com.sharad.quizbowl.ui.client.widget.FilterBox;
import com.sharad.quizbowl.ui.client.widget.Reader;
import com.sharad.quizbowl.ui.client.widget.SimpleSearch;
import com.sharad.quizbowl.ui.client.widget.SimpleSearch.Configuration;
import com.sharad.quizbowl.ui.client.widget.TossupInfoPanel;
import com.sharad.quizbowl.ui.client.widget.event.AnswerInfoEvent;
import com.sharad.quizbowl.ui.client.widget.event.AnswerInfoEventHandler;
import com.sharad.quizbowl.ui.client.widget.event.FilterEvent;
import com.sharad.quizbowl.ui.client.widget.event.FilterEventHandler;
import com.sharad.quizbowl.ui.client.widget.event.FilterResultEvent;
import com.sharad.quizbowl.ui.client.widget.event.FilterResultEventHandler;
import com.sharad.quizbowl.ui.client.widget.event.LoginEvent;
import com.sharad.quizbowl.ui.client.widget.event.LoginEventHandler;
import com.sharad.quizbowl.ui.client.widget.event.NewTossupEvent;
import com.sharad.quizbowl.ui.client.widget.event.NewTossupEventHandler;
import com.sharad.quizbowl.ui.client.widget.event.ReadEvent;
import com.sharad.quizbowl.ui.client.widget.event.ReadEventHandler;

public class HomeWidget extends Composite {

	private static HomeWidgetUiBinder uiBinder = GWT
			.create(HomeWidgetUiBinder.class);

	interface HomeWidgetUiBinder extends UiBinder<Widget, HomeWidget> {
	}

	public LayoutPanel main;

	public static String USERNAME = null;
	private JsArrayInteger years;
	private JsArrayString tournaments, difficulties, categories;
	private FilterBar filterBar;
	public Widget simpleSearch;
	@UiField(provided = true)
	public FlowPanel horizontalPanel;
	public LayoutPanel centerPanel;
	@UiField
	DockLayoutPanel searchPanel;
	public SimpleSearch search;
	private TossupPanel tossupPanel;
	@UiField(provided = true)
	FilterBox readerBox;
	@UiField
	Reader reader;
	@UiField
	TossupInfoPanel tossupInfoPanel;
	@UiField
	AnswerInfoPanel answerInfoPanel;
	@UiField
	Anchor login;
	LoginBox loginBox = new LoginBox();
	@UiField
	static TabLayoutPanel tabPanel;

	public HomeWidget(JsArrayInteger years, JsArrayString tournaments,
			JsArrayString difficulties, JsArrayString categories) {
		readerBox = new FilterBox(years, tournaments, difficulties, categories,
				false, "", "Generate");
		readerBox.addFilterEventHandler(new FilterEventHandler() {

			@Override
			public void onTossupsReceived(FilterEvent event) {
				event.getParameters().put("limit",
						Arrays.asList(new String[] { "1" }));
				event.getParameters().put("random",
						Arrays.asList(new String[] { "true" }));
				readTossups(event.getParameters());

			}

		});
		this.years = years;
		this.tournaments = tournaments;
		this.difficulties = difficulties;
		this.categories = categories;
		search = new SimpleSearch();
		search.addFilterEventHandler(new FilterEventHandler() {

			@Override
			public void onTossupsReceived(FilterEvent event) {
				searchTossups(event.getParameters());

			}

		});
		main = new LayoutPanel();
		horizontalPanel = new FlowPanel();
		centerPanel = new LayoutPanel();
		searchPanel = new DockLayoutPanel(Unit.PX);

		main.add(uiBinder.createAndBindUi(this));
		setSearchConfiguration(SimpleSearch.DEFAULT_CONFIGURATION);
		reader.addReadEventHandler(new ReadEventHandler() {
			@Override
			public void onRead(ReadEvent event) {
				tossupInfoPanel.loadTossup(event.getTossup());
			}

		});
		reader.addAnswerInfoEventHandler(new AnswerInfoEventHandler() {

			@Override
			public void onAnswerInfoReceived(AnswerInfoEvent event) {
				answerInfoPanel.loadAnswerInfo(event.getInfo());
			}

		});
		reader.addNewTossupEventHandler(new NewTossupEventHandler() {

			@Override
			public void onNewTossup(NewTossupEvent event) {
				readerBox.generate();
			}

		});
		initWidget(main);
		loginBox.addLoginEventHandler(new LoginEventHandler() {

			@Override
			public void onLogin(LoginEvent event) {
				Window.alert(event.getUser());

			}

		});
		login.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				DialogBox d = new DialogBox();
				d.setAnimationEnabled(false);
				d.setGlassEnabled(true);
				d.setModal(true);
				d.setAutoHideEnabled(true);
				d.setTitle("Log In");
				d.setHTML("Log In");
				d.add(loginBox);
				d.center();
				d.show();
				loginBox.setFocus();
			}

		});
		tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {

			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				setHash(((HTML) tabPanel.getTabWidget(event.getSelectedItem()))
						.getText().toLowerCase());

			}

		});
		if (getHash() != "") {
			changeTab(getHash());
		}
		HomeWidget.addHashHandler();
	}

	public void readTossups(HashMap<String, List<String>> params) {
		String parameters = "";
		String delimiter = "";
		for (String s : params.keySet()) {
			if (params.get(s).size() != 0) {
				parameters += delimiter
						+ s
						+ "="
						+ URL.encodeQueryString((Joiner.on("|").join(params
								.get(s))));
				delimiter = "&";
			}
		}
		JsonpRequestBuilder tossupGrabber = new JsonpRequestBuilder();
		tossupGrabber.setTimeout(30000);
		tossupGrabber.requestObject(QuizbowlUI.SERVER_URL
				+ "/query?alt=json-in-script&" + parameters,
				new AsyncCallback<TossupsPackage>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}

					@Override
					public void onSuccess(TossupsPackage result) {
						List<Tossup> list = new ArrayList<Tossup>();
						for (int i = 0; i < result.getTossups().length(); i++) {
							list.add(result.getTossups().get(i));
						}
						reader.setTossups(list);
					}
				});

	}

	public void searchTossups(HashMap<String, List<String>> params) {

		String parameters = "";
		String delimiter = "";
		for (String s : params.keySet()) {
			if (params.get(s).size() != 0) {
				parameters += delimiter
						+ s
						+ "="
						+ URL.encodeQueryString((Joiner.on("|").join(params
								.get(s))));
				delimiter = "&";
			}
		}
		JsonpRequestBuilder tossupGrabber = new JsonpRequestBuilder();
		tossupGrabber.setTimeout(30000);
		tossupGrabber.requestObject(QuizbowlUI.SERVER_URL
				+ "/query?alt=json-in-script&" + parameters,
				new AsyncCallback<TossupsPackage>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}

					@Override
					public void onSuccess(TossupsPackage result) {
						search.getLoading().setVisible(false);
						List<Tossup> list = new ArrayList<Tossup>();
						if (filterBar == null) {
							filterBar = new FilterBar();
							filterBar.setStyleName("filterBar");
							filterBar
									.addFilterResultEventHandler(new FilterResultEventHandler() {

										@Override
										public void onFilter(
												FilterResultEvent event) {
											tossupPanel.filter(
													event.getDifficulties(),
													event.getTournaments(),
													event.getCategories());
										}

									});
						}
						setSearchConfiguration(Configuration.HORIZONTAL);
						for (int i = 0; i < result.getTossups().length(); i++) {
							list.add(result.getTossups().get(i));
						}
						if (tossupPanel != null)
							tossupPanel.setTossups(list, false);
						else {
							tossupPanel = new TossupPanel(list);
							tossupPanel
									.addFilterResultEventHandler(new FilterResultEventHandler() {

										@Override
										public void onFilter(
												FilterResultEvent event) {
											filterBar.reconfigure(
													event.getDifficulties(),
													event.getTournaments(),
													event.getCategories());
										}

									});

							searchPanel.add(tossupPanel);
						}
						filterBar.reconfigure(tossupPanel.getDifficulties(),
								tossupPanel.getTournaments(),
								tossupPanel.getCategories());

					}
				});

		search.getLoading().setUrl(Resources.IMAGE_LOADING);
		search.getLoading().setVisible(true);
	}

	public void setSearchConfiguration(Configuration config) {
		if (!config.equals(search.getConfiguration())) {
			horizontalPanel.clear();
			centerPanel.clear();
			searchPanel.clear();
			simpleSearch = config.getUiBinder().createAndBindUi(search);
			if (config.equals(Configuration.HORIZONTAL)) {
				RootLayoutPanel.get().setStyleName("nonSplashStyle");
				horizontalPanel.add(simpleSearch);
				simpleSearch.setStyleName("horizontalSearch");
				horizontalPanel.setVisible(true);
				horizontalPanel.setWidth("100%");
				horizontalPanel.setHeight("100%");
				centerPanel.setVisible(false);
				searchPanel.addNorth(horizontalPanel, 60);
				searchPanel.addWest(filterBar, 200);
			} else if (config.equals(Configuration.VERTICAL)) {
				RootLayoutPanel.get().setStyleName("splashStyle");
				centerPanel.add(simpleSearch);
				centerPanel.setVisible(true);
				horizontalPanel.setVisible(false);
				HTML label = new HTML(
						"Copyright © 2011 Sharad Vikram. All Rights Reserved. Styles and CSS done by <a href=\"http://www.geraldfong.com\">Gerald Fong</a>.");
				label.setStyleName("copyrightText");
				HorizontalPanel temp = new HorizontalPanel();
				temp.setWidth("100%");
				temp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				temp.add(label);
				searchPanel.addSouth(temp, 20);
				searchPanel.add(centerPanel);

			}
			search.setConfiguration(config);
		}
	}

	public static void changeTab(String tab) {
		tab = tab.replaceAll("#", "");

		for (int i = 0; i < tabPanel.getWidgetCount(); i++) {
			if (tab.equals(((HTML) tabPanel.getTabWidget(i)).getText()
					.toLowerCase())) {
				tabPanel.selectTab(i);
				break;
			}
		}
	}

	public final static native void addHashHandler()/*-{
		$wnd.changetab =
          $entry(@com.sharad.quizbowl.ui.client.HomeWidget::changeTab(Ljava/lang/String;));
		$wnd.onhashchange = function() {
			$wnd.changetab($wnd.location.hash);
		}
		
	}-*/;

	public final native String getHash()/*-{
		return $wnd.location.hash;
	}-*/;

	public final native void setHash(String hash)/*-{
		$wnd.location.hash = hash;
	}-*/;

}
