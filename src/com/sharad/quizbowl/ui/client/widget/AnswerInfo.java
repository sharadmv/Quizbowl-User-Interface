package com.sharad.quizbowl.ui.client.widget;

public class AnswerInfo {
	private boolean correct;
	public String correctAnswer;

	public AnswerInfo(boolean correct, String correctAnswer) {
		this.setCorrect(correct);
		this.setCorrectAnswer(correctAnswer);
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
}
