package com.sharad.quizbowl.ui.client.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.TreeViewModel;
import com.sharad.quizbowl.ui.client.QuizbowlUI;
import com.sharad.quizbowl.ui.client.widget.event.FilterEvent;
import com.sharad.quizbowl.ui.client.widget.event.FilterEventHandler;

public class Browser extends Composite {
	@UiField(provided = true)
	CellBrowser browser;
	@UiField
	Button search;
	private HandlerManager handlerManager;
	private static BrowserUiBinder uiBinder = GWT.create(BrowserUiBinder.class);

	interface BrowserUiBinder extends UiBinder<Widget, Browser> {
	}

	DatabaseBrowseTreeModel model;

	public Browser(JsArrayString difficulties) {
		model = new DatabaseBrowseTreeModel(difficulties);
		browser = new CellBrowser(model, null);
		browser.setHeight("250px");
		browser.setWidth("650px");
		initWidget(uiBinder.createAndBindUi(this));
		handlerManager = new HandlerManager(this);
		search.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (model.selectionModel.getSelectedSet().size() != 0) {
					HashMap<String, List<String>> params = new HashMap<String, List<String>>();
					List<String> temp = new ArrayList<String>();
					for (Round r : model.selectionModel.getSelectedSet()) {
						if (params.containsKey("tournament")) {
							if (!params.get("tournament").contains(
									r.getTournament()))
								params.get("tournament").add(r.getTournament());
							else
								params.put("tournament", Arrays
										.asList(new String[] { r
												.getTournament() }));
						} else
							params.put("tournament", Arrays
									.asList(new String[] { r.getTournament() }));
						temp.add(r.getName());
					}
					params.put("round", temp);
					FilterEvent e = new FilterEvent(params);
					fireEvent(e);
				}
			}

		});
	}

	public HandlerRegistration addFilterEventHandler(FilterEventHandler handler) {
		return handlerManager.addHandler(FilterEvent.TYPE, handler);
	}

	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	static class Round implements Comparable<Round> {
		String tournament, name;

		public Round(String tournament, String name) {
			this.tournament = tournament;
			this.name = name;
		}

		public String getTournament() {
			return tournament;
		}

		public String getName() {
			return name;
		}

		public static final ProvidesKey<Round> KEY_PROVIDER = new ProvidesKey<Round>() {
			public Object getKey(Round round) {
				return round == null ? null : round.getTournament() + ":"
						+ round.getName();
			}
		};

		@Override
		public int compareTo(Round round) {
			return name.compareTo(round.getName());
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Round) {
				return name.equals(((Round) o).getName())
						&& tournament.equals(((Round) o).getTournament());
			}
			return false;
		}
	}

	static class Tournament {
		String name;

		public Tournament(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	static class Difficulty {
		String name;

		public Difficulty(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public static class DatabaseBrowseTreeModel implements TreeViewModel {
		private final DefaultSelectionEventManager<Round> selectionManager = DefaultSelectionEventManager
				.createCheckboxManager();
		private final MultiSelectionModel<Round> selectionModel;
		private final List<Difficulty> difficulties;
		private Cell<Round> roundCell;

		public DatabaseBrowseTreeModel(JsArrayString difficulties) {
			selectionModel = new MultiSelectionModel<Round>(Round.KEY_PROVIDER);
			this.difficulties = new ArrayList<Difficulty>();
			for (int i = 0; i < difficulties.length(); i++) {
				this.difficulties.add(new Difficulty(difficulties.get(i)));
			}
			List<HasCell<Round, ?>> hasCells = new ArrayList<HasCell<Round, ?>>();
			hasCells.add(new HasCell<Round, Boolean>() {

				private CheckboxCell cell = new CheckboxCell(true, false);

				public Cell<Boolean> getCell() {
					return cell;
				}

				public FieldUpdater<Round, Boolean> getFieldUpdater() {
					return null;
				}

				public Boolean getValue(Round object) {
					return selectionModel.isSelected(object);
				}
			});
			hasCells.add(new HasCell<Round, Round>() {

				private Cell<Round> cell = new AbstractCell<Round>() {

					@Override
					public void render(
							com.google.gwt.cell.client.Cell.Context context,
							Round value, SafeHtmlBuilder sb) {
						if (value != null) {
							sb.appendEscaped(value.getName());
						}

					}

				};

				public Cell<Round> getCell() {
					return cell;
				}

				public Round getValue(Round object) {
					return object;
				}

				@Override
				public FieldUpdater<Round, Round> getFieldUpdater() {
					return null;
				}
			});
			roundCell = new CompositeCell<Round>(hasCells) {
				@Override
				public void render(Context context, Round value,
						SafeHtmlBuilder sb) {
					sb.appendHtmlConstant("<table><tbody><tr>");
					super.render(context, value, sb);
					sb.appendHtmlConstant("</tr></tbody></table>");
				}

				@Override
				protected Element getContainerElement(Element parent) {
					// Return the first TR element in the table.
					return parent.getFirstChildElement().getFirstChildElement()
							.getFirstChildElement();
				}

				@Override
				protected <X> void render(Context context, Round value,
						SafeHtmlBuilder sb, HasCell<Round, X> hasCell) {
					Cell<X> cell = hasCell.getCell();
					sb.appendHtmlConstant("<td>");
					cell.render(context, hasCell.getValue(value), sb);
					sb.appendHtmlConstant("</td>");
				}
			};
		}

		@Override
		public <T> NodeInfo<?> getNodeInfo(T value) {
			if (value == null) {
				ListDataProvider<Difficulty> dataProvider = new ListDataProvider<Difficulty>(
						difficulties);
				Cell<Difficulty> cell = new AbstractCell<Difficulty>() {

					@Override
					public void render(
							com.google.gwt.cell.client.Cell.Context context,
							Difficulty value, SafeHtmlBuilder sb) {
						if (value != null) {
							sb.appendEscaped(value.getName());
						}

					}

				};
				return new DefaultNodeInfo<Difficulty>(dataProvider, cell);
			} else if (value instanceof Difficulty) {
				final Difficulty t = (Difficulty) value;
				AsyncDataProvider<Tournament> dataProvider = new AsyncDataProvider<Tournament>() {

					@Override
					protected void onRangeChanged(HasData<Tournament> display) {
						final Range range = display.getVisibleRange();

						JsonpRequestBuilder r = new JsonpRequestBuilder();
						r.requestObject(
								QuizbowlUI.SERVER_URL
										+ "/browse?term=difficulty&value="
										+ t.getName() + "&alt=json-in-script",
								new AsyncCallback<JsArrayString>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onSuccess(JsArrayString result) {
										int start = range.getStart();
										List<Tournament> data = new ArrayList<Tournament>();
										for (int i = start; i < result.length(); i++) {
											data.add(new Tournament(result
													.get(i)));
										}
										updateRowData(start, data);
									}

								});

					}

				};
				Cell<Tournament> cell = new AbstractCell<Tournament>() {

					@Override
					public void render(
							com.google.gwt.cell.client.Cell.Context context,
							Tournament value, SafeHtmlBuilder sb) {
						if (value != null) {
							sb.appendEscaped(value.getName());
						}

					}

				};
				return new DefaultNodeInfo<Tournament>(dataProvider, cell);
			} else if (value instanceof Tournament) {
				final Tournament t = (Tournament) value;
				AsyncDataProvider<Round> dataProvider = new AsyncDataProvider<Round>() {

					@Override
					protected void onRangeChanged(HasData<Round> display) {
						final Range range = display.getVisibleRange();

						JsonpRequestBuilder r = new JsonpRequestBuilder();
						r.requestObject(
								QuizbowlUI.SERVER_URL
										+ "/browse?term=tournament&value="
										+ t.getName() + "&alt=json-in-script",
								new AsyncCallback<JsArrayString>() {

									@Override
									public void onFailure(Throwable caught) {

									}

									@Override
									public void onSuccess(JsArrayString result) {
										selectionModel.clear();
										int start = range.getStart();
										List<Round> data = new ArrayList<Round>();
										for (int i = start; i < result.length(); i++) {
											data.add(new Round(t.getName(),
													result.get(i)));
										}
										updateRowData(start, data);
									}

								});

					}

				};

				return new DefaultNodeInfo<Round>(dataProvider, roundCell,
						selectionModel, selectionManager, null);
			}
			return null;
		}

		@Override
		public boolean isLeaf(Object value) {
			return value instanceof Round;
		}

	}

}
