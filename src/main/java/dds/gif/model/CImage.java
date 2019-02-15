package dds.gif.model;

public class CImage {
	private String textureSheet;
	private int width;
	private int durationPerFrame;
	private int count;

	public CImage(String textureSheet, int width, int durationPerFrame, int count) {
		this.textureSheet = textureSheet;
		this.width = width;
		this.durationPerFrame = durationPerFrame;
		this.count = count;
	}

	public String getTextureSheet() {
		return textureSheet;
	}

	public void setTextureSheet(String textureSheet) {
		this.textureSheet = textureSheet;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getDurationPerFrame() {
		return durationPerFrame;
	}

	public void setDurationPerFrame(int durationPerFrame) {
		this.durationPerFrame = durationPerFrame;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return String.format("CImage [textureSheet=%s, width=%s, durationPerFrame=%s, count=%s]", textureSheet, width,
				durationPerFrame, count);
	}
}
