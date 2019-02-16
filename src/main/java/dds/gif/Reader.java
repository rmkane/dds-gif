package dds.gif;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.GsonBuilder;

import dds.gif.model.CEmoticon;
import dds.gif.model.CImage;
import dds.gif.model.CPack;

public class Reader {
	private static final ClassLoader loader = Animator.class.getClassLoader();
	private static final String SUFFIX = "_anim";
	
	private static Map<String, String> gameStrings;
	private static List<CPack> packList;
	private static List<CEmoticon> emoticonList;
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		gameStrings = readData("data/GameStrings.txt");
		emoticonList = readEmoticonData("data/EmoticonData.xml");
		packList = readPackData("data/EmoticonPackData.xml");
		
		try (Writer writer = new FileWriter("out/emoji-packs.json")) {
		    new GsonBuilder().setPrettyPrinting().create().toJson(packList, writer);
		}
	}

	private static Map<String, String> readData(String filename) {
		Map<String, String> data = new HashMap<String, String>();
		Scanner scanner = new Scanner(loader.getResourceAsStream(filename));
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			int index = line.indexOf('=');
			data.put(line.substring(0, index), line.substring(index + 1));
		}
		scanner.close();
		return data;
	}

	public static List<CPack> readPackData(String filename) throws ParserConfigurationException, SAXException, IOException {
		List<CPack> results = new ArrayList<CPack>();
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(loader.getResourceAsStream(filename));
		doc.getDocumentElement().normalize();
		
		NodeList emoticonPacks = doc.getElementsByTagName("CEmoticonPack");
		for (int i = 0; i < emoticonPacks.getLength(); i++) {
			Node node = emoticonPacks.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element emoticonPack = (Element) node;
				String id = emoticonPack.getAttribute("id");
				NodeList emoticonArray = emoticonPack.getElementsByTagName("EmoticonArray");
				boolean containsAnimation = false;
				if (emoticonArray.getLength() > 0) {
					for (int j = 0; j < emoticonArray.getLength(); j++) {
						Element emoticon = (Element) emoticonArray.item(j);
						String value = emoticon.getAttribute("value");
						if (value.endsWith(SUFFIX)) {
							containsAnimation = true;
							break;
						}
					}
				}
				if (containsAnimation) {
					String name = gameStrings.get(String.format("EmoticonPack/Name/%s", id));
					List<CEmoticon> emoticons = new ArrayList<>();
					
					if (emoticonArray.getLength() > 0) {
						for (int j = 0; j < emoticonArray.getLength(); j++) {
							Element emoticon = (Element) emoticonArray.item(j);
							String value = emoticon.getAttribute("value");
							CEmoticon found = emoticonList.stream().filter(e -> e.getId().equals(value)).findFirst().get();
							emoticons.add(found);
						}
					}
					
					results.add(new CPack(name, emoticons));
				}
			}
		}
		return results;
	}

	public static List<CEmoticon> readEmoticonData(String filename) throws ParserConfigurationException, SAXException, IOException {
		List<CEmoticon> results = new ArrayList<CEmoticon>();
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(loader.getResourceAsStream(filename));
		doc.getDocumentElement().normalize();

		NodeList emoticons = doc.getElementsByTagName("CEmoticon");
		for (int i = 0; i < emoticons.getLength(); i++) {
			Node node = emoticons.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				String id = element.getAttribute("id");
				
				if (id.endsWith(SUFFIX)) {
					String description = lookupDescription(id);
					List<String> aliases = new ArrayList<String>();
					String textureSheet = null;
					int width = 0;
					int durationPerFrame = 0;
					int count = 0;

					NodeList images = element.getElementsByTagName("Image");
					if (images.getLength() == 1) {
						Element image = (Element) images.item(0);
						textureSheet = image.getAttribute("TextureSheet");
						count = Integer.parseInt(image.getAttribute("Count"), 10);
						durationPerFrame = Integer.parseInt(image.getAttribute("DurationPerFrame"), 10);
						width = Integer.parseInt(image.getAttribute("Width"), 10);
					}
					NodeList aliasArray = element.getElementsByTagName("UniversalAliasArray");
					if (aliasArray.getLength() > 0) {
						Element alias = (Element) aliasArray.item(0);
						aliases.add(alias.getAttribute("value"));
					}
					CImage image = new CImage(textureSheet, width, durationPerFrame, count);
					CEmoticon emoticon = new CEmoticon(id, description, image, aliases);
					results.add(emoticon);
				}
			}
		}
		return results;
	}

	public static String lookupDescription(String id) {
		String description = gameStrings.get(String.format("Emoticon/Description/%s", id));
		int aliasStart = description.indexOf(':');
		if (aliasStart > -1) {
			return description.substring(0, aliasStart - 1);
		}
		return description;
	}
}
