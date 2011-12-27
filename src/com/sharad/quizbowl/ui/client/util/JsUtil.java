package com.sharad.quizbowl.ui.client.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.user.client.Window;

public class JsUtil {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List getListFromJs(JavaScriptObject js, String type) {
		List list = null;
		if (type.equals("int")) {
			list = new ArrayList<Integer>();
			for (int i = 0; i < ((JsArrayInteger) js).length(); i++) {
				list.add(((JsArrayInteger) js).get(i));
			}
		} else if (type.equals("str")) {
			list = new ArrayList<String>();
			for (int i = 0; i < ((JsArrayString) js).length(); i++) {
				list.add(((JsArrayString) js).get(i));
			}
		}
		return list;
	}
}
