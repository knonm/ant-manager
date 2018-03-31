package br.com.kiman.antmanager;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import br.com.kiman.antmanager.util.QuickSort;

import java.io.File;
import java.io.IOException;

public class AntManager {

	private String pathAnt;

	public AntManager(String pathAnt) {
		this.pathAnt = pathAnt;
	}

	public void execTarget(String target) throws IOException {
		Runtime.getRuntime().exec(
				"cmd /c start " + "/D \"" + this.pathAnt + "\" ant \"" + target
						+ "\"");
	}

	public String[] getTargets() throws Exception {
		File f = new File(this.pathAnt + "\\build.xml");

		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = dBuilder.parse(f);
		String[] targets = null;
		if (doc.hasChildNodes()) {
			NodeList nl = doc.getElementsByTagName("target");
			targets = new String[nl.getLength()];
			for (int i = targets.length - 1; i > -1; i--) {
				targets[i] = nl.item(i).getAttributes().getNamedItem("name")
						.getNodeValue();
			}
		}

		(new QuickSort<String>(String.CASE_INSENSITIVE_ORDER)).ordena(targets);

		return targets;
	}
}