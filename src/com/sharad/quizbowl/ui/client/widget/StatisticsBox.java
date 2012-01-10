package com.sharad.quizbowl.ui.client.widget;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;

public class StatisticsBox extends Composite {

	private static StatisticsBoxUiBinder uiBinder = GWT
			.create(StatisticsBoxUiBinder.class);

	interface StatisticsBoxUiBinder extends UiBinder<Widget, StatisticsBox> {
	}

	public StatisticsBox() {
		LineChart chart;
		Date date = new Date();
		DataTable table = DataTable.create();
		table.addColumn(ColumnType.DATE);
		initWidget(uiBinder.createAndBindUi(this));
	}

}
