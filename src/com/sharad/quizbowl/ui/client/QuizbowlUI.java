/*
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sharad.quizbowl.ui.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.sharad.quizbowl.ui.client.json.data.DataPackage;

/**
 * type filter text Entry point classes define <code>onModuleLoad()</code>.
 */
public class QuizbowlUI implements EntryPoint {
	public static final String SERVER_URL = "http://ec2-50-17-57-153.compute-1.amazonaws.com:8080";
	public static JsArrayInteger YEARS;
	public static JsArrayString TOURNAMENTS, DIFFICULTIES, CATEGORIES;
	public static int NUM_QUESTIONS, NUM_USERS, NUM_SCORES;
	public static final List<String> CATEGORIES_LIST = new ArrayList<String>();;

	public void onModuleLoad() {
		JsonpRequestBuilder dataGrabber = new JsonpRequestBuilder();
		dataGrabber.setTimeout(30000);
		dataGrabber.requestObject(SERVER_URL + "/data?alt=json-in-script",
				new AsyncCallback<DataPackage>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());

					}

					@Override
					public void onSuccess(DataPackage result) {
						YEARS = result.getYears();
						TOURNAMENTS = result.getTournaments();
						DIFFICULTIES = result.getDifficulties();
						CATEGORIES = result.getCategories();
						NUM_QUESTIONS = result.getNumQuestions();
						NUM_USERS = result.getNumUsers();
						NUM_SCORES = result.getNumScores();
						for (int i = 0; i < CATEGORIES.length(); i++) {
							CATEGORIES_LIST.add(CATEGORIES.get(i));
						}
						final HomeWidget home = new HomeWidget(YEARS,
								TOURNAMENTS, DIFFICULTIES, CATEGORIES);
						RootLayoutPanel.get().add(home);

						Timer timer = new Timer() {

							@Override
							public void run() {
								home.search.searchBox.setFocus(true);

							}

						};
						timer.schedule(400);

					}
				});
	}

	public static String replaceInvalidCharacters(String s) {
		return s.replaceAll("�", "\'").replaceAll("[']", "''");

	}
}
