package com.raffertynh.server;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import com.raffertynh.bohemiainteractive.BohemiaServer;

public class ConsoleTab extends BohemiaServer {
	private JTextField textField;
	public JTextArea dtrpnTest;
	
	public ConsoleTab(A3ServerManager parent) {
		super(parent, "Console");

		getConfig().setDefaultParameter("DIRECTORY_SERVER", "");
		getConfig().setDefaultParameter("DIRECTORY_WORKSHOP", "");
		getConfig().setDefaultParameter("DIRECTORY_PROFILE", "");
		getConfig().setDefaultParameter("MODS_PARAMETER", "");
		
		getConfig().finish();
		
		JPanel serverSettings = new JPanel();
		serverSettings.setBackground(Color.WHITE);
		serverSettings.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Server Settings", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		serverSettings.setBounds(359, 11, 156, 154);
		this.add(serverSettings);
		serverSettings.setLayout(null);
		
		JButton btnSave = new JButton("Send");
		/*btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		
				getConfig().setParameter("DIRECTORY_SERVER", txtFieldInstallArma3.getText());
				getConfig().setParameter("DIRECTORY_WORKSHOP", txtFieldWorkshopArma3.getText());
				getConfig().setParameter("DIRECTORY_PROFILE", txtFieldProfileArma3.getText());
				
				getConfig().save();
			}
		});*/
		btnSave.setBounds(299, 142, 57, 22);
		this.add(btnSave);
		
		textField = new JTextField();
		textField.setBounds(10, 143, 286, 20);
		add(textField);
		textField.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 346, 120);
		add(scrollPane);
		
		dtrpnTest = new JTextArea();
		scrollPane.setViewportView(dtrpnTest);
		dtrpnTest.setEditable(false);
		
		/*if(getConfig().getParameter("DIRECTORY_SERVER") != null) txtFieldInstallArma3.setText(getConfig().getParameter("DIRECTORY_SERVER").toString());
		if(getConfig().getParameter("DIRECTORY_WORKSHOP") != null) txtFieldWorkshopArma3.setText(getConfig().getParameter("DIRECTORY_WORKSHOP").toString());
		if(getConfig().getParameter("DIRECTORY_PROFILE") != null) txtFieldProfileArma3.setText(getConfig().getParameter("DIRECTORY_PROFILE").toString());
		
		*/
	}
	
	
	public void append(String msg) {
		dtrpnTest.append(msg + "\n");
	}
	public void run() {
		try {
			SERVER_RUNNING = true;
    		this.onServerStarted();
			serverProcess = Runtime.getRuntime().exec(getConfig().getParameter("DIRECTORY_SERVER") + "\\arma3server_x64.exe " + execString); //INSTALL_DIR + "\\arma3server_x64.exe -name=A3Server -world=altis -config=CONFIG_server.cfg " + MODS_PARAM
			while(serverProcess.isAlive()) {}
    		SERVER_RUNNING = false;
    		this.onServerStopped();
		} catch(Exception e) {
    		SERVER_RUNNING = false;
    		this.onServerStopped();
		}
	}
}
