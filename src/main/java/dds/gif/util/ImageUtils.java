package dds.gif.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import dds.gif.lib.DisposalMode;
import dds.gif.lib.GifSequenceWriter;

public class ImageUtils {
	private static ClassLoader loader = ImageUtils.class.getClassLoader();

	public static BufferedImage loadImage(String filename) throws IOException {
		return loadImage(filename, "");
	}

	public static BufferedImage loadImage(String filename, String path) throws IOException {
		return ImageIO.read(loader.getResourceAsStream(String.format("%s%s", path, filename)));

	}

	public static BufferedImage[] split(BufferedImage image, int rows, int cols) {
		return split(image, rows, cols, rows * cols, 0);
	}

	public static BufferedImage[] split(BufferedImage image, int rows, int cols, int limit, int maxWidth) {
		BufferedImage[] frames = new BufferedImage[limit];
		int width = image.getWidth() / cols;
		int height = image.getHeight() / rows;
		for (int row = 0; row < rows; row++) {
			for (int col = 0, rowIndex = row * cols + col; col < cols && rowIndex + col < limit; col++) {
				int x = col * width, y = row * height;
				frames[rowIndex + col] = image.getSubimage(x, y, Math.min(width, maxWidth), height);
			}
		}
		return frames;
	}

	public static String stripExtension(String filename) {
		return filename.substring(0, filename.lastIndexOf('.'));
	}

	public static void writeAnimation(BufferedImage[] frames, String name, String path, long delayTime,
			boolean loopForever) throws FileNotFoundException, IOException {
		new File(path).mkdir();
		ImageOutputStream output = new FileImageOutputStream(new File(String.format("%s%s.%s", path, name, "gif")));
		GifSequenceWriter writer = new GifSequenceWriter(output, frames[0].getType(), (int) delayTime, loopForever,
				DisposalMode.RESTORE_BACKGROUND);
		for (BufferedImage frame : frames) {
			writer.writeToSequence(frame);
		}
		writer.close();
		output.close();
	}

	public static void writeImage(BufferedImage image, String name, String ext, String path) throws IOException {
		String filename = String.format("%s%s.%s", path, name, ext);
		new File(path).mkdir();
		ImageIO.write(image, ext, new File(filename));
	}

	public static void writeImage(BufferedImage image, String name) throws IOException {
		writeImage(image, name, "png", "out");
	}

	public static void writeImages(BufferedImage[] frames, String name) throws IOException {
		for (int i = 0; i < frames.length; i++) {
			writeImage(frames[i], String.format("%s_%d", name, i));
		}
	}

	public static BufferedImage[] fitImages(BufferedImage[] frames, int width, int height) {
		BufferedImage[] resized = new BufferedImage[frames.length];
		for (int i = 0; i < frames.length; i++) {
			resized[i] = fitImage(frames[i], width, height);
		}
		return resized;
	}

	public static BufferedImage fitImage(BufferedImage frame, int width, int height) {
		BufferedImage copy = new BufferedImage(width, height, frame.getType());
		int xOffset = (copy.getWidth() - frame.getWidth()) / 2;
		int yOffset = (copy.getHeight() - frame.getHeight()) / 2;
		copy.getGraphics().drawImage(frame, xOffset, yOffset, null);
		return copy;
	}
}
