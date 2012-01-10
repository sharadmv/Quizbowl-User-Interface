package com.sharad.quizbowl.ui.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UserBox extends Composite {

	private static UserBoxUiBinder uiBinder = GWT.create(UserBoxUiBinder.class);

	interface UserBoxUiBinder extends UiBinder<Widget, UserBox> {
	}

	@UiField
	public Anchor signoutLink;

	public UserBox() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
