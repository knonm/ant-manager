package br.com.kiman.antmanager.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.com.kiman.antmanager.AntManager;
import br.com.kiman.antmanager.Main;

public class ManagerGUI {

	private JFrame wMain;
	private JTextField txtPath;
	private JButton btnPath;
	private JScrollPane pTargets;
	private JList<String> lstTargets;
	private JButton btnLaunch;

	private AntManager antMng;
	
	private boolean populouTargets;

	public ManagerGUI() {
		init();
	}

	private void init() {
		String[] targets;
		this.populouTargets = false;
		
		this.wMain = new JFrame();
		this.txtPath = new JTextField();
		this.btnPath = new JButton();
		this.lstTargets = new JList<String>();
		this.pTargets = new JScrollPane(this.lstTargets);
		this.btnLaunch = new JButton();

		this.wMain.setResizable(false);
		this.wMain.setLayout(null);
		this.wMain.setTitle("Ant Manager");
		this.wMain.addWindowListener(new WMainWindowEvents(this));

		this.txtPath.setText("Diretório com o build.xml");
		this.txtPath.setEditable(false);
		this.txtPath.setBackground(Color.WHITE);

		this.btnPath.setText("...");
		this.btnPath.addActionListener(new BtnPathEvents(this));

		this.lstTargets.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.lstTargets.setLayoutOrientation(JList.VERTICAL);
		this.lstTargets.setVisibleRowCount(-1);

		this.btnLaunch.setText("Launch");
		this.btnLaunch.addActionListener(new BtnLaunchEvents(this));

		initComponentSL(Toolkit.getDefaultToolkit().getScreenSize(),
				this.wMain, 0.5F, 0.5F, 0.25F, 0.25F);
		initComponentSL(this.wMain.getSize(), this.txtPath, 0.75F, 0.05F,
				0.05F, 0.05F);
		initComponentSL(this.wMain.getSize(), this.btnPath, 0.1F, 0.05F, 0.85F,
				0.05F);
		initComponentSL(this.wMain.getSize(), this.pTargets, 0.5F, 0.6F, 0.25F,
				0.175F);
		initComponentSL(this.wMain.getSize(), this.btnLaunch, 0.15F, 0.05F,
				0.425F, 0.85F);

		targets = Main.lerArquivo();
		if(targets.length > 0) {
			populaTargets(targets[0]);
		}
		
		this.wMain.add(this.txtPath);
		this.wMain.add(this.btnPath);
		this.wMain.add(this.btnLaunch);
		this.wMain.add(this.pTargets);

		this.wMain.setVisible(true);
	}

	private void initComponentSL(Dimension parent, Component child,
			float pctWidth, float pctHeight, float pctX, float pctY) {
		int parentWidth = parent.width;
		int parentHeight = parent.height;
		Dimension d = new Dimension(Math.round(parentWidth * pctWidth),
				Math.round(parentHeight * pctHeight));
		Point p = new Point(Math.round(parentWidth * pctX),
				Math.round(parentHeight * pctY));

		child.setSize(d);
		child.setLocation(p);
	}

	void selecionaTargets() {
		try {
			JFileChooser fc = new JFileChooser(new File("C:\\"));
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setAcceptAllFileFilterUsed(false);
			fc.setFileFilter(new FileNameExtensionFilter("Ant build.xml", "xml"));
			if (fc.showOpenDialog(this.wMain) == JFileChooser.APPROVE_OPTION) {
				populaTargets(fc.getSelectedFile()
						.getParentFile().getCanonicalPath());
			}
		} catch (Exception e1) {
			limpaTargets();
		}
	}
	
	void launchTargets() {
		String[] targets = this.lstTargets.getSelectedValuesList().toArray(new String[0]);
		for(String target : targets) {
			if (target != null && !target.isEmpty()) {
				try {
					this.antMng.execTarget(target);
				} catch (IOException e1) {

				}
			}
		}
	}

	void populaTargets(String targetPath) {
		try {
			this.antMng = new AntManager(targetPath);
			this.txtPath.setText(targetPath);
			
			String[] targets = this.antMng.getTargets();
			
			DefaultListModel<String> listModel = new DefaultListModel<String>();
			for (int i = 0; i < targets.length; i++) {
				listModel.addElement(targets[i]);
			}
			this.lstTargets.setModel(listModel);
			
			this.pTargets.revalidate();
			this.pTargets.repaint();
			this.wMain.revalidate();
			this.wMain.repaint();
			
			this.populouTargets = true;
		} catch(Exception ex) {
			
		}
	}

	void limpaTargets() {
		this.lstTargets.setModel(new DefaultListModel<String>());

		this.pTargets.revalidate();
		this.pTargets.repaint();
		this.wMain.revalidate();
		this.wMain.repaint();
	}

	void dispose() {
		if(this.txtPath.getText() != null &&
				!this.txtPath.getText().isEmpty() && this.populouTargets) {
			Main.gravarArquivo(this.txtPath.getText());
		}
		this.wMain.dispose();
	}
	
	private class BtnPathEvents implements ActionListener {
		private ManagerGUI managerGUI;

		@Override
		public void actionPerformed(ActionEvent e) {
			this.managerGUI.selecionaTargets();
		}

		public BtnPathEvents(ManagerGUI managerGUI) {
			this.managerGUI = managerGUI;
		}
	}

	private class BtnLaunchEvents implements ActionListener {
		private ManagerGUI managerGUI;

		@Override
		public void actionPerformed(ActionEvent e) {
			this.managerGUI.launchTargets();
		}

		public BtnLaunchEvents(ManagerGUI managerGUI) {
			this.managerGUI = managerGUI;
		}
	}
	
	private class WMainWindowEvents extends WindowAdapter {
		private ManagerGUI managerGUI;
		
		@Override
		public void windowClosing(WindowEvent e) {
			super.windowClosing(e);
			this.managerGUI.dispose();
		}
		
		public WMainWindowEvents(ManagerGUI managerGUI) {
			this.managerGUI = managerGUI;
		}
	}
}