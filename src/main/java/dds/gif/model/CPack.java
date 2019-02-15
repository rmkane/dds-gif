package dds.gif.model;

import java.util.List;

public class CPack {
	private String name;
	private List<CEmoticon> emoticons;

	public CPack(String name, List<CEmoticon> emoticons) {
		this.name = name;
		this.emoticons = emoticons;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CEmoticon> getEmoticons() {
		return emoticons;
	}

	public void setEmoticons(List<CEmoticon> emoticons) {
		this.emoticons = emoticons;
	}

	@Override
	public String toString() {
		return String.format("CPack [name=%s, emoticons=%s]", name, emoticons);
	}
}
