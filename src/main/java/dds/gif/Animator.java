package dds.gif;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dds.gif.model.CEmoticon;
import dds.gif.model.CPack;
import dds.gif.util.ImageUtils;

public class Animator {
	private static ClassLoader loader = Animator.class.getClassLoader();

	private static int MAX_FRAME_WIDTH = 50;
	private static int MAX_FRAME_HEIGHT = 50;

	public static void main(String[] args) throws IOException {
		processSpritesheets(loadData("emoji-packs.json"), 4);
	}

	public static List<CPack> loadData(String filename) throws UnsupportedEncodingException {
		InputStream stream = loader.getResourceAsStream(filename);
		Reader reader = new InputStreamReader(stream, "UTF-8");
		Type resultType = new TypeToken<List<CPack>>() { }.getType();
		return new Gson().fromJson(reader, resultType);
	}

	public static void processSpritesheets(List<CPack> packs, int cols) throws IOException {
		for (CPack pack : packs) {
			for (CEmoticon emoticon : pack.getEmoticons()) {
				processSpritesheet(emoticon, pack.getName(), cols);
			}
		}
	}

	public static void processSpritesheet(CEmoticon emoticon, String packName, int cols) throws IOException {
		String dds = String.format("%s.dds", emoticon.getImage().getTextureSheet());
		String name = String.format("%s %s", packName, emoticon.getDescription());
		BufferedImage image = ImageUtils.loadImage(dds, "images/");
		int rows = (int) Math.ceil(emoticon.getImage().getCount() / (cols * 1.0d));
		BufferedImage[] frames = ImageUtils.fitImages(
				ImageUtils.split(image, rows, cols, emoticon.getImage().getCount(), emoticon.getImage().getWidth()),
				MAX_FRAME_WIDTH, MAX_FRAME_HEIGHT);
		ImageUtils.writeAnimation(frames, name, "out/", emoticon.getImage().getDurationPerFrame(), true);
		// ImageUtils.writeImage(image, name);
		// ImageUtils.writeImages(frames, name);
	}
}
