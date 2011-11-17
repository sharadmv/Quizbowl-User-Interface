package com.sharad.quizbowl.ui.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {
	public static final Resources INSTANCE = GWT.create(Resources.class);

	@Source("logo_small.png")
	ImageResource logoSmall();

	@Source("logo_big.png")
	ImageResource logoBig();

	@Source("white.png")
	ImageResource white();

	public static final String IMAGE_LOADING = "com.sharad.quizbowl.ui.QuizbowlUI/sc/skins/Enterprise/images/loadingSmall.gif";
	public static final String IMAGE_LOGO = "logo.png";
}
