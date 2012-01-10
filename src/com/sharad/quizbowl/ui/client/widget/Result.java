package com.sharad.quizbowl.ui.client.widget;

import com.google.gwt.core.client.JavaScriptObject;

public class Result extends JavaScriptObject {
	protected Result() {

	}

	public final native String getMessage()/*-{
		return this.message;
	}-*/;

	public final native String getError()/*-{
		return this.error;
	}-*/;

}
