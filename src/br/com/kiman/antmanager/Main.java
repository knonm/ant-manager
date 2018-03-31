package br.com.kiman.antmanager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.com.kiman.antmanager.view.ManagerGUI;

public class Main {

	private static final String PATH_ARQ = "./path.am";

	public static String[] lerArquivo() {
		List<String> dadosArq = new ArrayList<String>();

		try {
			Scanner sc = new Scanner(new File(Main.PATH_ARQ));

			while (sc.hasNext()) {
				dadosArq.add(sc.nextLine());
			}

			sc.close();
		} catch (FileNotFoundException ex) {

		}

		return dadosArq.toArray(new String[0]);
	}

	public static void gravarArquivo(String path) {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(new File(Main.PATH_ARQ))));
			bw.write(path);
			bw.close();
		} catch (Exception e) {

		}
	}

	public static void main(String[] args) {
		new ManagerGUI();
	}

}