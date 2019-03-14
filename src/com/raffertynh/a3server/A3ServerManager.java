package com.raffertynh.a3server;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.raffertynh.server.Arma3Server;
import com.raffertynh.window.A3ConfigEditor;
import com.raffertynh.window.A3ModManager;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class A3ServerManager extends JFrame {

	private JPanel contentPane;
	private A3ServerManager instance;
	private JTextField txtFieldInstallArma3;
	private JTextField txtFieldWorkshopArma3;
	private JTextField txtFieldProfileArma3;
	
	private JButton btnStartServer;
	
	private JButton btnGlobalsXML, btnTypesXML, btnEventsXML, btnEconomyXML;
	
	private JTextField txtFieldInstallDayZ;
	private JTextField txtFieldWorkshopDayZ;
	
	private JTabbedPane tabbedPane;

	private Arma3Server a3Server;
	private DayZServer dayZServer;
	
	public static DecimalFormat df = new DecimalFormat("#,###.#");
	
	public static void main(String[] args) {
		new A3ServerManager();
	}
	
	public A3ServerManager() {

		a3Server = new Arma3Server(this);
		dayZServer = new DayZServer(this);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {}
		instance = this;
		
		//Config Setup
		
		
		//Create JFrame
		setTitle("Bohemia Interactive Server Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(556, 314);
		setLocationRelativeTo(null);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panelArma = new JPanel();
		panelArma.setBackground(Color.WHITE);
		panelArma.setBounds(10, 45, 520, 206);
		
		btnStartServer = new JButton("Start Server");
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				switch(tabbedPane.getSelectedIndex()) {
					case 0: //arma
						if(a3Server.SERVER_RUNNING) {
							a3Server.stopServer();
							btnStartServer.setText("Start Server");
						} else {
							a3Server.startServer("-name=A3Server -world=altis -config=CONFIG_server.cfg "/* + MODS_PARAM*/);
							btnStartServer.setText("Stop Server");
						}
					break;
					case 1: //dayz
						if(dayZServer.SERVER_RUNNING) {
							dayZServer.stopServer();
							btnStartServer.setText("Start Server");
						} else {
							dayZServer.startServer("-config=serverDZ.cfg -port=2302 -dologs -adminlog -netlog -freezecheck "/* + MODS_PARAM*/);
							btnStartServer.setText("Stop Server");
						}
						 
					break;
				}
			}
		});
		btnStartServer.setBounds(449, 251, 91, 23);
		contentPane.add(btnStartServer);
		

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				switch(tabbedPane.getSelectedIndex()) {
					case 0:
						if(a3Server.SERVER_RUNNING) 
							btnStartServer.setText("Stop Server");
						else 
							btnStartServer.setText("Start Server");
					break;
					case 1:
						if(dayZServer.SERVER_RUNNING) 
							btnStartServer.setText("Stop Server");
						else 
							btnStartServer.setText("Start Server");
					break;
				}
			}
		});
		tabbedPane.addTab("Arma 3", panelArma);
		panelArma.setLayout(null);
		
		JPanel panelDayZ = new JPanel();
		panelDayZ.setBackground(Color.WHITE);
		tabbedPane.addTab("DayZ", null, panelDayZ, null);
		panelDayZ.setLayout(null);
		
		txtFieldInstallArma3 = new JTextField();
		txtFieldInstallArma3.setBounds(10, 24, 339, 20);
		panelArma.add(txtFieldInstallArma3);
		txtFieldInstallArma3.setColumns(10);
		
		JPanel serverSettings = new JPanel();
		serverSettings.setBackground(Color.WHITE);
		serverSettings.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Server Settings", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		serverSettings.setBounds(359, 11, 156, 80);
		panelArma.add(serverSettings);
		serverSettings.setLayout(null);
		
		JButton btnNewButton_1 = new JButton("Arma 3 Configuration");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new A3ConfigEditor(instance, new File(a3Server.getConfig().getParameter("DIRECTORY_SERVER") + File.separator + "CONFIG_server.cfg"));
			}
		});
		btnNewButton_1.setBounds(10, 21, 136, 23);
		serverSettings.add(btnNewButton_1);
		
		JButton btnArmaprofile = new JButton("Server - Arma3Profile");
		btnArmaprofile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new A3ConfigEditor(instance, new File(a3Server.getConfig().getParameter("DIRECTORY_PROFILE") + File.separator + "A3Server\\A3Server.Arma3Profile"));
			}
		});
		btnArmaprofile.setBounds(10, 46, 136, 23);
		serverSettings.add(btnArmaprofile);
		
		JLabel lblNewLabel = new JLabel("Server Install Directory");
		lblNewLabel.setBounds(10, 5, 111, 14);
		panelArma.add(lblNewLabel);
		
		txtFieldWorkshopArma3 = new JTextField();
		txtFieldWorkshopArma3.setColumns(10);
		txtFieldWorkshopArma3.setBounds(10, 69, 339, 20);
		panelArma.add(txtFieldWorkshopArma3);
		
		JLabel lblWorkshopDirectory = new JLabel("Workshop Directory");
		lblWorkshopDirectory.setBounds(10, 50, 95, 14);
		panelArma.add(lblWorkshopDirectory);
		
		txtFieldProfileArma3 = new JTextField();
		txtFieldProfileArma3.setColumns(10);
		txtFieldProfileArma3.setBounds(10, 114, 339, 20);
		panelArma.add(txtFieldProfileArma3);
		
		JLabel lblProfileDirectory = new JLabel("Profile Directory");
		lblProfileDirectory.setBounds(10, 95, 77, 14);
		panelArma.add(lblProfileDirectory);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				a3Server.getConfig().setParameter("DIRECTORY_SERVER", txtFieldInstallArma3.getText());
				a3Server.getConfig().setParameter("DIRECTORY_WORKSHOP", txtFieldWorkshopArma3.getText());
				a3Server.getConfig().setParameter("DIRECTORY_PROFILE", txtFieldProfileArma3.getText());
				
				a3Server.getConfig().save();
			}
		});
		btnSave.setBounds(458, 142, 57, 23);
		panelArma.add(btnSave);
		tabbedPane.setBounds(10, 36, 530, 204);
		contentPane.add(tabbedPane);
		
		
		txtFieldInstallDayZ = new JTextField();
		txtFieldInstallDayZ.setColumns(10);
		txtFieldInstallDayZ.setBounds(10, 24, 339, 20);
		panelDayZ.add(txtFieldInstallDayZ);
		
		JPanel serverSettingsDayZ = new JPanel();
		serverSettingsDayZ.setBackground(Color.WHITE);
		serverSettingsDayZ.setLayout(null);
		serverSettingsDayZ.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Server Settings", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		serverSettingsDayZ.setBounds(359, 11, 156, 55);
		panelDayZ.add(serverSettingsDayZ);
		
		JButton btnServerdz = new JButton("DayZ Configuration");
		btnServerdz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new A3ConfigEditor(instance, new File(dayZServer.getConfig().getParameter("DIRECTORY_SERVER") + File.separator + "serverDZ.cfg"));
			}
		});
		btnServerdz.setBounds(10, 21, 136, 23);
		serverSettingsDayZ.add(btnServerdz);
		
		JLabel lblInstallDirDayZ = new JLabel("Server Install Directory");
		lblInstallDirDayZ.setBounds(10, 5, 111, 14);
		panelDayZ.add(lblInstallDirDayZ);
		
		txtFieldWorkshopDayZ = new JTextField();
		txtFieldWorkshopDayZ.setColumns(10);
		txtFieldWorkshopDayZ.setBounds(10, 69, 339, 20);
		panelDayZ.add(txtFieldWorkshopDayZ);
		
		JLabel lblWorkshopDirDayZ = new JLabel("Workshop Directory");
		lblWorkshopDirDayZ.setBounds(10, 50, 95, 14);
		panelDayZ.add(lblWorkshopDirDayZ);
		
		JButton btnSaveDayZ = new JButton("Save");
		btnSaveDayZ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				dayZServer.getConfig().setParameter("DIRECTORY_SERVER", txtFieldInstallDayZ.getText());
				dayZServer.getConfig().setParameter("DIRECTORY_WORKSHOP", txtFieldWorkshopDayZ.getText());
				
				dayZServer.getConfig().save();
			}
		});
		btnSaveDayZ.setBounds(458, 142, 57, 23);
		panelDayZ.add(btnSaveDayZ);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Server Settings", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(10, 90, 292, 78);
		panelDayZ.add(panel);
		
		btnTypesXML = new JButton("Types.XML");
		btnTypesXML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new A3ConfigEditor(instance, new File(dayZServer.getConfig().getParameter("DIRECTORY_SERVER") + "\\mpmissions\\dayzOffline.chernarusplus\\db\\types.xml"));
			}
		});
		btnTypesXML.setBounds(10, 46, 136, 23);
		panel.add(btnTypesXML);
		
		btnGlobalsXML = new JButton("Globals.XML");
		btnGlobalsXML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new A3ConfigEditor(instance, new File(dayZServer.getConfig().getParameter("DIRECTORY_SERVER") + "\\mpmissions\\dayzOffline.chernarusplus\\db\\globals.xml"));
			}
		});
		btnGlobalsXML.setBounds(10, 20, 136, 23);
		panel.add(btnGlobalsXML);
		
		btnEventsXML = new JButton("Events.XML");
		btnEventsXML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new A3ConfigEditor(instance, new File(dayZServer.getConfig().getParameter("DIRECTORY_SERVER") + "\\mpmissions\\dayzOffline.chernarusplus\\db\\events.xml"));
			}
		});
		btnEventsXML.setBounds(146, 46, 136, 23);
		panel.add(btnEventsXML);
		
		btnEconomyXML = new JButton("Economy.XML");
		btnEconomyXML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new A3ConfigEditor(instance, new File(dayZServer.getConfig().getParameter("DIRECTORY_SERVER") + "\\mpmissions\\dayzOffline.chernarusplus\\db\\economy.xml"));
			}
		});
		btnEconomyXML.setBounds(146, 20, 136, 23);
		panel.add(btnEconomyXML);
		
		JLabel lblBohemiaInteractiveServer = new JLabel("Bohemia Interactive Server Manager");
		lblBohemiaInteractiveServer.setFont(new Font("Dubai", Font.PLAIN, 18));
		lblBohemiaInteractiveServer.setBounds(278, 11, 262, 32);
		contentPane.add(lblBohemiaInteractiveServer);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(10, 251, 176, 23);
		contentPane.add(progressBar);
		
		JLabel lblCreatedByBj = new JLabel("Created by BJ Rafferty");
		lblCreatedByBj.setBounds(10, 11, 112, 14);
		contentPane.add(lblCreatedByBj);
		
		JButton btnNewButton_2 = new JButton("Mod Manager");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				switch(tabbedPane.getSelectedIndex()) {
					case 0:
						new A3ModManager(instance, a3Server);
					break;
					case 1:
						new A3ModManager(instance, dayZServer);
					break;
				}
			}
		});
		btnNewButton_2.setBounds(351, 251, 97, 23);
		contentPane.add(btnNewButton_2);


		//load config values
		System.out.print("Loading Stuff");
		if(a3Server.getConfig().getParameter("DIRECTORY_SERVER") != null) txtFieldInstallArma3.setText(a3Server.getConfig().getParameter("DIRECTORY_SERVER").toString());
		if(a3Server.getConfig().getParameter("DIRECTORY_WORKSHOP") != null) txtFieldWorkshopArma3.setText(a3Server.getConfig().getParameter("DIRECTORY_WORKSHOP").toString());
		if(a3Server.getConfig().getParameter("DIRECTORY_PROFILE") != null) txtFieldProfileArma3.setText(a3Server.getConfig().getParameter("DIRECTORY_PROFILE").toString());
		if(dayZServer.getConfig().getParameter("DIRECTORY_SERVER") != null) txtFieldInstallDayZ.setText(dayZServer.getConfig().getParameter("DIRECTORY_SERVER").toString());
		if(dayZServer.getConfig().getParameter("DIRECTORY_WORKSHOP") != null) txtFieldWorkshopDayZ.setText(dayZServer.getConfig().getParameter("DIRECTORY_WORKSHOP").toString());
		
		
		setVisible(true);
	}
	
	public void onServerStopped(ServerType serverType) {
		btnStartServer.setText("Start Server");
		switch(serverType) {
		case DAY_Z_SERVER:
			btnGlobalsXML.setEnabled(true);
			btnTypesXML.setEnabled(true);
			btnEventsXML.setEnabled(true);
			btnEconomyXML.setEnabled(true);
		}
	}
	public void onServerStarted(ServerType serverType) {
		switch(serverType) {
			case DAY_Z_SERVER:
				btnGlobalsXML.setEnabled(false);
				btnTypesXML.setEnabled(false);
				btnEventsXML.setEnabled(false);
				btnEconomyXML.setEnabled(false);
		}
	}
	
	private Image getScaledImage(Image img , int w , int h) {
	    BufferedImage resizedimage = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedimage.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
	    g2.drawImage(img, 0, 0,w,h,null);
	    g2.dispose();
	    return resizedimage;
	}

	public static long getFileFolderSize(File dir) {
		long size = 0;
		if (dir.isDirectory()) {
			for (File file : dir.listFiles()) {
				if (file.isFile()) {
					size += file.length();
				} else
					size += getFileFolderSize(file);
			}
		} else if (dir.isFile()) {
			size += dir.length();
		}
		return size;
	}
}
