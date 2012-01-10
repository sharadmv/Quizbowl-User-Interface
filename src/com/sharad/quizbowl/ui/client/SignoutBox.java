package com.sharad.quizbowl.ui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Window;

public class SignoutBox extends Window {

	private static SignoutBoxUiBinder uiBinder = GWT
			.create(SignoutBoxUiBinder.class);
	@UiField
	public Button yesButton, noButton;

	interface SignoutBoxUiBinder extends UiBinder<Widget, SignoutBox> {
	}

	public SignoutBox() {
		setIsModal(true);
		setAutoSize(true);
		setTitle("Sign Out");
		setAutoCenter(true);

		addItem(uiBinder.createAndBindUi(this));

	}
	

}
