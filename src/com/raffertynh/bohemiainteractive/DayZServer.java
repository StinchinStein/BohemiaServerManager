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
import com.raffertynh.window.A3ConfigEditor;

public class DayZServer extends BohemiaServer {

	private JButton btnGlobalsXML, btnTypesXML, btnEventsXML, btnEconomyXML;
	
	private JTextField txtFieldInstallDayZ;
	private JTextField txtFieldWorkshopDayZ;
	
	
	public DayZServer(A3ServerManager parent) {
		super(parent, "DayZ");
		getConfig().setDefaultParameter("DIRECTORY_SERVER", "");
		getConfig().setDefaultParameter("DIRECTORY_WORKSHOP", "");
		getConfig().setDefaultParameter("MODS_PARAMETER", "");
		getConfig().finish();
		
		txtFieldInstallDayZ = new JTextField();
		txtFieldInstallDayZ.setColumns(10);
		txtFieldInstallDayZ.setBounds(10, 24, 339, 20);
		this.add(txtFieldInstallDayZ);
		
		JPanel serverSettingsDayZ = new JPanel();
		serverSettingsDayZ.setBackground(Color.WHITE);
		serverSettingsDayZ.setLayout(null);
		serverSettingsDayZ.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Server Settings", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		serverSettingsDayZ.setBounds(359, 11, 156, 55);
		this.add(serverSettingsDayZ);
		
		JButton btnServerdz = new JButton("DayZ Configuration");
		btnServerdz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new A3ConfigEditor(parent, new File(getConfig().getParameter("DIRECTORY_SERVER") + File.separator + "serverDZ.cfg"));
			}
		});
		btnServerdz.setBounds(10, 21, 136, 23);
		serverSettingsDayZ.add(btnServerdz);

		JLabel lblInstallDirDayZ = new JLabel("Server Install Directory");
		lblInstallDirDayZ.setBounds(10, 5, 111, 14);
		this.add(lblInstallDirDayZ);
		
		txtFieldWorkshopDayZ = new JTextField();
		txtFieldWorkshopDayZ.setColumns(10);
		txtFieldWorkshopDayZ.setBounds(10, 69, 339, 20);
		this.add(txtFieldWorkshopDayZ);
		
		JLabel lblWorkshopDirDayZ = new JLabel("Workshop Directory");
		lblWorkshopDirDayZ.setBounds(10, 50, 95, 14);
		this.add(lblWorkshopDirDayZ);
		
		JButton btnSaveDayZ = new JButton("Save");
		btnSaveDayZ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				getConfig().setParameter("DIRECTORY_SERVER", txtFieldInstallDayZ.getText());
				getConfig().setParameter("DIRECTORY_WORKSHOP", txtFieldWorkshopDayZ.getText());
				
				getConfig().save();
			}
		});
		btnSaveDayZ.setBounds(458, 142, 57, 23);
		this.add(btnSaveDayZ);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Server Settings", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(10, 90, 292, 78);
		this.add(panel);
		
		btnTypesXML = new JButton("Types.XML");
		btnTypesXML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new A3ConfigEditor(parent, new File(getConfig().getParameter("DIRECTORY_SERVER") + "\\mpmissions\\dayzOffline.chernarusplus\\db\\types.xml"));
			}
		});
		btnTypesXML.setBounds(10, 46, 136, 23);
		panel.add(btnTypesXML);
		
		btnGlobalsXML = new JButton("Globals.XML");
		btnGlobalsXML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new A3ConfigEditor(parent, new File(getConfig().getParameter("DIRECTORY_SERVER") + "\\mpmissions\\dayzOffline.chernarusplus\\db\\globals.xml"));
			}
		});
		btnGlobalsXML.setBounds(10, 20, 136, 23);
		panel.add(btnGlobalsXML);
		
		btnEventsXML = new JButton("Events.XML");
		btnEventsXML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new A3ConfigEditor(parent, new File(getConfig().getParameter("DIRECTORY_SERVER") + "\\mpmissions\\dayzOffline.chernarusplus\\db\\events.xml"));
			}
		});
		btnEventsXML.setBounds(146, 46, 136, 23);
		panel.add(btnEventsXML);
		
		btnEconomyXML = new JButton("Economy.XML");
		btnEconomyXML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new A3ConfigEditor(parent, new File(getConfig().getParameter("DIRECTORY_SERVER") + "\\mpmissions\\dayzOffline.chernarusplus\\db\\economy.xml"));
			}
		});
		btnEconomyXML.setBounds(146, 20, 136, 23);
		panel.add(btnEconomyXML);
		
		if(getConfig().getParameter("DIRECTORY_SERVER") != null) txtFieldInstallDayZ.setText(getConfig().getParameter("DIRECTORY_SERVER").toString());
		if(getConfig().getParameter("DIRECTORY_WORKSHOP") != null) txtFieldWorkshopDayZ.setText(getConfig().getParameter("DIRECTORY_WORKSHOP").toString());
		
	}

	public void onServerStopped() {
		btnGlobalsXML.setEnabled(true);
		btnTypesXML.setEnabled(true);
		btnEventsXML.setEnabled(true);
		btnEconomyXML.setEnabled(true);
	}
	public void onServerStarted() {
		btnGlobalsXML.setEnabled(false);
		btnTypesXML.setEnabled(false);
		btnEventsXML.setEnabled(false);
		btnEconomyXML.setEnabled(false);
	}
	
	public void run() {
		try {
			SERVER_RUNNING = true;
    		this.onServerStarted();
			serverProcess = Runtime.getRuntime().exec(getConfig().getParameter("DIRECTORY_SERVER") + "\\DayZServer_x64.exe -config=serverDZ.cfg -port=2302 -dologs -adminlog -netlog -freezecheck " + MODS_PARAM); //INSTALL_DIR + "\\arma3server_x64.exe -name=A3Server -world=altis -config=CONFIG_server.cfg " + MODS_PARAM
			while(serverProcess.isAlive()) {}
    		SERVER_RUNNING = false;
    		this.onServerStopped();
		} catch(Exception e) {
    		SERVER_RUNNING = false;
    		this.onServerStopped();
		}
	}

}
