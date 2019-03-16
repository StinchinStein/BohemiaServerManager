package com.raffertynh.bohemiainteractive;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import com.raffertynh.server.A3ServerManager;
import com.raffertynh.server.ServerType;
import com.raffertynh.window.A3ConfigEditor;

public class Arma3Server extends BohemiaServer {
	
	private JTextField txtFieldInstallArma3;
	private JTextField txtFieldWorkshopArma3;
	private JTextField txtFieldProfileArma3;
	
	public Arma3Server(A3ServerManager parent) {
		super(parent, "Arma 3");

		getConfig().setDefaultParameter("DIRECTORY_SERVER", "");
		getConfig().setDefaultParameter("DIRECTORY_WORKSHOP", "");
		getConfig().setDefaultParameter("DIRECTORY_PROFILE", "");
		getConfig().setDefaultParameter("MODS_PARAMETER", "");
		
		getConfig().finish();
		

		txtFieldInstallArma3 = new JTextField();
		txtFieldInstallArma3.setBounds(10, 24, 339, 20);
		this.add(txtFieldInstallArma3);
		txtFieldInstallArma3.setColumns(10);
		
		JPanel serverSettings = new JPanel();
		serverSettings.setBackground(Color.WHITE);
		serverSettings.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Server Settings", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		serverSettings.setBounds(359, 11, 156, 80);
		this.add(serverSettings);
		serverSettings.setLayout(null);
		
		JButton btnNewButton_1 = new JButton("Arma 3 Configuration");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new A3ConfigEditor(parent, new File(getConfig().getParameter("DIRECTORY_SERVER") + File.separator + "CONFIG_server.cfg"));
			}
		});
		btnNewButton_1.setBounds(10, 21, 136, 23);
		serverSettings.add(btnNewButton_1);
		
		JButton btnArmaprofile = new JButton("Server - Arma3Profile");
		btnArmaprofile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new A3ConfigEditor(parent, new File(getConfig().getParameter("DIRECTORY_PROFILE") + File.separator + "A3Server\\A3Server.Arma3Profile"));
			}
		});
		btnArmaprofile.setBounds(10, 46, 136, 23);
		serverSettings.add(btnArmaprofile);
		
		JLabel lblNewLabel = new JLabel("Server Install Directory");
		lblNewLabel.setBounds(10, 5, 111, 14);
		this.add(lblNewLabel);
		
		txtFieldWorkshopArma3 = new JTextField();
		txtFieldWorkshopArma3.setColumns(10);
		txtFieldWorkshopArma3.setBounds(10, 69, 339, 20);
		this.add(txtFieldWorkshopArma3);
		
		JLabel lblWorkshopDirectory = new JLabel("Workshop Directory");
		lblWorkshopDirectory.setBounds(10, 50, 95, 14);
		this.add(lblWorkshopDirectory);
		
		txtFieldProfileArma3 = new JTextField();
		txtFieldProfileArma3.setColumns(10);
		txtFieldProfileArma3.setBounds(10, 114, 339, 20);
		this.add(txtFieldProfileArma3);
		
		JLabel lblProfileDirectory = new JLabel("Profile Directory");
		lblProfileDirectory.setBounds(10, 95, 77, 14);
		this.add(lblProfileDirectory);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				getConfig().setParameter("DIRECTORY_SERVER", txtFieldInstallArma3.getText());
				getConfig().setParameter("DIRECTORY_WORKSHOP", txtFieldWorkshopArma3.getText());
				getConfig().setParameter("DIRECTORY_PROFILE", txtFieldProfileArma3.getText());
				
				getConfig().save();
			}
		});
		btnSave.setBounds(458, 142, 57, 23);
		this.add(btnSave);
		
		if(getConfig().getParameter("DIRECTORY_SERVER") != null) txtFieldInstallArma3.setText(getConfig().getParameter("DIRECTORY_SERVER").toString());
		if(getConfig().getParameter("DIRECTORY_WORKSHOP") != null) txtFieldWorkshopArma3.setText(getConfig().getParameter("DIRECTORY_WORKSHOP").toString());
		if(getConfig().getParameter("DIRECTORY_PROFILE") != null) txtFieldProfileArma3.setText(getConfig().getParameter("DIRECTORY_PROFILE").toString());
		
		
	}
	
	public void run() {
		try {
			SERVER_RUNNING = true;
    		this.onServerStarted();
			serverProcess = Runtime.getRuntime().exec(getConfig().getParameter("DIRECTORY_SERVER") + "\\arma3server_x64.exe -name=A3Server -world=altis -config=CONFIG_server.cfg " + MODS_PARAM); //INSTALL_DIR + "\\arma3server_x64.exe -name=A3Server -world=altis -config=CONFIG_server.cfg " + MODS_PARAM
			while(serverProcess.isAlive()) {}
    		SERVER_RUNNING = false;
    		this.onServerStopped();
		} catch(Exception e) {
    		SERVER_RUNNING = false;
    		this.onServerStopped();
		}
	}

}
