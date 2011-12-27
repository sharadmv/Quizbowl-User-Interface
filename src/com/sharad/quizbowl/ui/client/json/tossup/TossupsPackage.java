package com.sharad.quizbowl.ui.client.json.tossup;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class TossupsPackage extends JavaScriptObject {
	protected TossupsPackage() {

	}

	public final native JsArray<Tossup> getTossups()/*-{
		return this['tossup-array'];
	}-*/;

}
