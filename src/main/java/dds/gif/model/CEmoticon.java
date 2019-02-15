package dds.gif.model;

import java.util.List;

public class CEmoticon {
	private String id;
	private String description;
	private CImage image;
	private List<String> aliases;

	public CEmoticon(String id, String description, CImage image, List<String> aliases) {
		this.id = id;
		this.description = description;
		this.image = image;
		this.aliases = aliases;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CImage getImage() {
		return image;
	}

	public void setImage(CImage image) {
		this.image = image;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	@Override
	public String toString() {
		return String.format("CEmoticon [id=%s, description=%s, image=%s, aliases=%s]", id, description, image,
				aliases);
	}
}