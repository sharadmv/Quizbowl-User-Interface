package com.sharad.quizbowl.ui.client.widget.event;

import com.google.gwt.event.shared.EventHandler;

public interface AnswerInfoEventHandler extends EventHandler {
	void onAnswerInfoReceived(AnswerInfoEvent event);
}
