package dds.gif;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dds.gif.model.CEmoticon;
import dds.gif.model.CPack;
import dds.gif.util.ImageUtils;

public class Animator {
	private static final ClassLoader loader = Animator.class.getClassLoader();
	private static final String fileExt = "png";
	private static final String charset = "UTF-8";
	private static final boolean debug = false;
	
	private static String DATA_FILE;
	private static String INPUT_DIR;
	private static String OUTPUT_DIR;
	private static int SHEET_PER_ROW;
	private static int FRAME_MAX_WIDTH;
	private static int FRAME_MAX_HEIGHT;

	public static void main(String[] args) throws IOException {
		setup();
		processSpritesheets(loadData(DATA_FILE), SHEET_PER_ROW);
	}

	private static final void setup() throws IOException {
		Properties props = new Properties();
		props.load(loader.getResourceAsStream("config.properties"));

		DATA_FILE = getStr(props, "data_file");
		INPUT_DIR = getStr(props, "input.dir");
		OUTPUT_DIR = getStr(props, "output.dir");
		SHEET_PER_ROW = getInt(props, "sheet.per_row");
		FRAME_MAX_WIDTH = getInt(props, "frame.max_width");
		FRAME_MAX_HEIGHT = getInt(props, "frame.max_height");

		new File(OUTPUT_DIR).mkdir();
	}

	public static List<CPack> loadData(String filename) throws UnsupportedEncodingException {
		InputStream stream = loader.getResourceAsStream(filename);
		Reader reader = new InputStreamReader(stream, charset);
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
		BufferedImage image = ImageUtils.loadImage(dds, INPUT_DIR);
		int rows = (int) Math.ceil(emoticon.getImage().getCount() / (cols * 1.0d));
		BufferedImage[] frames = ImageUtils.split(image, rows, cols, emoticon.getImage().getCount(), emoticon.getImage().getWidth());
		BufferedImage[] padded = ImageUtils.fitImages(frames, FRAME_MAX_WIDTH, FRAME_MAX_HEIGHT);
		ImageUtils.writeAnimation(padded, name, OUTPUT_DIR, emoticon.getImage().getDurationPerFrame(), true);
		if (debug) {
			ImageUtils.writeImage(image, name, fileExt, OUTPUT_DIR);
			ImageUtils.writeImages(padded, name, fileExt, OUTPUT_DIR);
		}
	}

	private static String getStr(Properties props, String prop) {
		return (String) props.get(prop);
	}
	
	private static int getInt(Properties props, String prop) {
		return Integer.parseInt(getStr(props, prop), 10);
	}
}
