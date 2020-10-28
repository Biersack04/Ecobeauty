package com.example.ecobeauty.mydeparture;

import com.example.ecobeauty.R;

public class Word {

	private int id;
	private String word;
	private String partOfSpeech;

	public Word( String word, String pos) {
		super();
		this.word = word;
		this.setPartOfSpeech(pos);
	}

	public String getWord() {
		return this.word;
	}

	public String getPartOfSpeech() {
		return partOfSpeech;
	}

	public void setPartOfSpeech(String partOfSpeech) {
		this.partOfSpeech = partOfSpeech;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return R.string.wordWord + word + R.string.partofSpeech + partOfSpeech + R.string.skob;
	}
}