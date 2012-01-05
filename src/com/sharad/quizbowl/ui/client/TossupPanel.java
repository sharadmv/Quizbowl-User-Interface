package com.sharad.quizbowl.ui.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sharad.quizbowl.ui.client.json.tossup.Tossup;
import com.sharad.quizbowl.ui.client.widget.FilterBar;
import com.sharad.quizbowl.ui.client.widget.event.FilterResultEvent;
import com.sharad.quizbowl.ui.client.widget.event.FilterResultEventHandler;

public class TossupPanel extends Composite {
	private static TossupPanelUiBinder uiBinder = GWT
			.create(TossupPanelUiBinder.class);

	@UiTemplate("TossupPanel.ui.xml")
	interface TossupPanelUiBinder extends UiBinder<Widget, TossupPanel> {
	}

	@UiField(provided = true)
	public VerticalPanel container;
	@UiField
	public HorizontalPanel tossupPanel;
	private List<Tossup> tossups;
	@UiField(provided = true)
	Button exportpdf;
	@UiField(provided = true)
	Button exporttxt;
	public Set<String> tournaments, difficulties, categories;
	@UiField(provided = true)
	public Label countLabel;
	private HandlerManager handlerManager;

	public enum Formats {
		PDF(".pdf"), TXT(".txt"), RTF(".rtf");
		String format;

		Formats(String format) {
			this.format = format;
		}

		public String getFormat() {
			return format;
		}
	}

	public TossupPanel(List<Tossup> tossups) {
		handlerManager = new HandlerManager(this);
		exportpdf = new Button("Export as .pdf");
		exporttxt = new Button("Export as .txt");
		countLabel = new Label();
		this.tossups = tossups;
		container = new VerticalPanel();
		setTossups(tossups, false);
		initWidget(uiBinder.createAndBindUi(this));
		tossupPanel.setStyleName("tossupPanel");
		countLabel.setStyleName("countLabel");
	}

	public void setTossups(List<Tossup> tossups, boolean filter) {
		countLabel.setText(tossups.size() + " results returned");
		container.clear();
		difficulties = new HashSet<String>();
		tournaments = new HashSet<String>();
		categories = new HashSet<String>();
		for (int i = 0; i < tossups.size(); i++) {
			Tossup temp = tossups.get(i);
			TossupBox box = new TossupBox(temp);
			categories.add(temp.getCategory());
			tournaments.add(temp.getYear() + " " + temp.getTournament());
			difficulties.add(temp.getDifficulty());
			container.add(box);
		}
		if (!filter) {
			FilterResultEvent event = new FilterResultEvent(difficulties,
					tournaments, categories);
			fireEvent(event);
			this.tossups = tossups;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < container.getWidgetCount(); i++) {
			sb.append(container.getWidget(i).toString()).append("\\n\\n");
		}
		return sb.toString();
	}

	@UiHandler("exportpdf")
	void exportPdf(ClickEvent e) {
		export(e, Formats.PDF);
	}

	@UiHandler("exporttxt")
	void exportTxt(ClickEvent e) {
		export(e, Formats.TXT);
	}

	void export(ClickEvent e, Formats format) {
		FormPanel form = new FormPanel();
		if (!form.isAttached())
			RootPanel.get().add(form);
		form.setMethod(FormPanel.METHOD_GET);
		VerticalPanel temp = new VerticalPanel();
		form.setAction(QuizbowlUI.SERVER_URL + "/download");
		form.setVisible(false);
		TextBox t = new TextBox();
		t.setName("text");
		t.setText(toString());
		temp.add(t);
		TextBox f = new TextBox();
		f.setName("format");
		f.setText(format.getFormat());
		temp.add(f);
		form.add(temp);
		form.submit();
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	public HandlerRegistration addFilterResultEventHandler(
			FilterResultEventHandler handler) {
		return handlerManager.addHandler(FilterResultEvent.TYPE, handler);
	}

	public void sort(Comparator<Tossup> c) {
		Collections.sort(tossups,c);
		setTossups(tossups, false);
	}

	public void filter(Set<String> difficulties, Set<String> tournaments,
			Set<String> categories) {
		this.difficulties = difficulties;
		this.tournaments = tournaments;
		this.categories = categories;
		List<Tossup> list = new ArrayList<Tossup>();
		for (int i = 0; i < tossups.size(); i++) {
			boolean add = true;
			Tossup temp = tossups.get(i);
			if (add)
				for (String d : difficulties) {
					if (!d.equals(FilterBar.ALL_STRING)) {
						if (temp.getDifficulty().equals(d)) {
							add = true;
							break;
						} else {
							add = false;
						}
					} else {
						add = true;
						break;
					}
				}
			if (add)
				for (String d : tournaments) {
					if (!d.equals(FilterBar.ALL_STRING)) {
						if ((temp.getYear() == (Integer.parseInt(d.substring(0,
								4))) && temp.getTournament().equals(
								d.substring(5)))) {
							add = true;
							break;
						} else {
							add = false;
						}
					} else {
						add = true;
						break;
					}

				}
			if (add)
				for (String d : categories) {
					if (!d.equals(FilterBar.ALL_STRING)) {
						if (temp.getCategory().equals(d)) {
							add = true;
							break;
						} else {
							add = false;
						}
					} else {
						add = true;
						break;
					}

				}
			if (add)
				list.add(temp);
		}
		setTossups(list, true);
	}

	public Set<String> getDifficulties() {
		return difficulties;
	}

	public Set<String> getTournaments() {
		return tournaments;
	}

	public Set<String> getCategories() {
		return categories;
	}
}
