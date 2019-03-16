package com.raffertynh.server;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.raffertynh.bohemiainteractive.Arma3Server;
import com.raffertynh.bohemiainteractive.BohemiaServer;
import com.raffertynh.bohemiainteractive.DayZServer;
import com.raffertynh.window.A3ModManager;

public class A3ServerManager extends JFrame {

	private JPanel contentPane;
	private A3ServerManager instance;
	
	private JButton btnStartServer;
	
	public JTabbedPane tabbedPane;
	
	public ArrayList<BohemiaServer> SERVERS = new ArrayList<BohemiaServer>();
	public ConsoleTab gameConsole;
	private int prevTab;
	
	public static DecimalFormat df = new DecimalFormat("#,###.#");
	
	public static void main(String[] args) {
		new A3ServerManager();
	}
	
	public A3ServerManager() {
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
		
		btnStartServer = new JButton("Start Server");
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(tabbedPane.getSelectedIndex() != 2) {
					startServer(tabbedPane.getSelectedIndex());
				} else {
					startServer(prevTab);
				}
			}
		});
		btnStartServer.setBounds(449, 251, 91, 23);
		contentPane.add(btnStartServer);
		

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		/*tabbedPane.addChangeListener(new ChangeListener() {
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
		});*/
		
		//Start after TabbedPane to auto-add to it. NULLPTR if started before.
		addCustomServers();
		
		gameConsole = new ConsoleTab(this);

		tabbedPane.setBounds(10, 36, 530, 204);
		contentPane.add(tabbedPane);
		
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
				new A3ModManager(instance, SERVERS.get(tabbedPane.getSelectedIndex()));
			}
		});
		btnNewButton_2.setBounds(351, 251, 97, 23);
		contentPane.add(btnNewButton_2);


		//load config values
		System.out.println("Loading Stuff");
		
		gameConsole.append("Finished loading");
		
		setVisible(true);
	}
	
	public void addCustomServers() {
		SERVERS.add(new Arma3Server(this));
		SERVERS.add(new DayZServer(this));
	}
	
	public void startServer(int index) {
		String PARAM = "";
		if(SERVERS.get(index).SERVER_RUNNING) {
			SERVERS.get(index).stopServer();
			btnStartServer.setText("Start Server");
			gameConsole.append("Stopping " + SERVERS.get(index).getClass().getSimpleName() + " server");
			tabbedPane.setSelectedIndex(prevTab);
		} else {
			SERVERS.get(index).startServer("");
			btnStartServer.setText("Stop Server");
			gameConsole.append("Starting " + SERVERS.get(index).getClass().getSimpleName() + " server");
			prevTab = index;
			tabbedPane.setSelectedIndex(SERVERS.size());
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
