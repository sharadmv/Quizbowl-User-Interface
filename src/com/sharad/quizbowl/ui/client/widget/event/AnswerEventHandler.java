package com.sharad.quizbowl.ui.client.widget.event;

import com.google.gwt.event.shared.EventHandler;

public interface AnswerEventHandler extends EventHandler {
	void onAnswerReceived(AnswerEvent event);
}
