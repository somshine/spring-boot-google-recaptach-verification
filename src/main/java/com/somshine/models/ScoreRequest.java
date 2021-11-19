package com.somshine.models;

public class ScoreRequest {
	private int score;
	private String response;
	
	public ScoreRequest() {
	}
	
	public ScoreRequest(int score, String response) {
		super();
		this.score = score;
		this.response = response;
	}
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "ScoreRequest [score=" + score + ", response=" + response + "]";
	}
}
