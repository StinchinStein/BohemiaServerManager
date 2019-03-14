package com.raffertynh.window;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DecimalFormat;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.raffertynh.a3server.A3ServerManager;
import com.raffertynh.a3server.ArmaCFGParser;
import com.raffertynh.renderer.ArmaList;
import com.raffertynh.renderer.ArmaMod;
import com.raffertynh.renderer.InventoryRenderer;
import com.raffertynh.server.BohemiaServer;

public class A3ModManager extends JFrame {

	private JPanel contentPane;
	public String winType;
	public JLabel lblModList;
	public JSONObject obj = new JSONObject();
	A3ServerManager parent;
	private ArmaList list;
	private JButton btnSaveVehicle;
	private JTextField textField;
	private DecimalFormat df = new DecimalFormat("#,###.#");
	
	private A3WorkshopWindow workshopWindow;
	
	private BohemiaServer server;
	
	private JSONArray modList;
	
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
	
	public A3ModManager(final A3ServerManager parent, BohemiaServer server) {
		workshopWindow = new A3WorkshopWindow(parent, server);
		this.modList = server.MODS_LIST;
		this.parent = parent;
		this.server = server;
		try {
			setTitle("StinchinStein's A3 Server Manager - Mod Manager");
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 596, 290);
			contentPane = new JPanel();
			contentPane.setBorder(null);
			setContentPane(contentPane);
			contentPane.setLayout(null);
			btnSaveVehicle = new JButton("Done");
			btnSaveVehicle.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			btnSaveVehicle.setBounds(478, 229, 102, 23);
			contentPane.add(btnSaveVehicle);
			
			JButton btnDeleteNote = new JButton("Delete Mode");
			btnDeleteNote.setEnabled(false);
			btnDeleteNote.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
			btnDeleteNote.setBounds(239, 229, 113, 23);
			contentPane.add(btnDeleteNote);
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(11, 28, 569, 194);
			contentPane.add(scrollPane);

			list = new ArmaList();
			DefaultListModel<ArmaMod> md = new DefaultListModel<>();
			list.setModel(md);
			
			list.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					boolean isSelected = !((ArmaMod) list.getModel().getElementAt(list.getSelectedIndex())).isSelected();
					String mod = list.getModel().getElementAt(list.getSelectedIndex()).toString();
					if(isSelected) {
						if(!modList.contains(mod)) {
							modList.add(list.getModel().getElementAt(list.getSelectedIndex()).toString());
						}
					} else {
						if(modList.contains(mod)) {
							modList.remove(list.getModel().getElementAt(list.getSelectedIndex()).toString());
						}
					}
	    			updateModel();
					
				}
			});
			
		    list.setCellRenderer(new InventoryRenderer());
		    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		    
			scrollPane.setViewportView(list);
			
			JButton btnAll = new JButton("All");
			btnAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for(int i = 0; i < list.getModel().getSize(); i++) {
		    			String mod = list.getModel().getElementAt(i).toString();
		    			if(!modList.contains(mod)) {
		    				modList.add(list.getModel().getElementAt(i).toString());
			    		}
		    		}
	    			updateModel();
				}
			});
			btnAll.setBounds(538, 3, 43, 23);
			contentPane.add(btnAll);
			
			JButton btnRefreshMods = new JButton("Refresh Mods");
			btnRefreshMods.setBounds(10, 229, 99, 23);
			contentPane.add(btnRefreshMods);
			
			JButton btnNone_1 = new JButton("None");
			btnNone_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					for(int i = 0; i < list.getModel().getSize(); i++) {
		    			String mod = list.getModel().getElementAt(i).toString();
		    			if(modList.contains(mod)) {
		    				modList.remove(list.getModel().getElementAt(i).toString());
			    		}
		    		}
	    			updateModel();
				}
			});
			btnNone_1.setBounds(481, 3, 57, 23);
			contentPane.add(btnNone_1);
			
			lblModList = new JLabel("Mod Manager");
			lblModList.setBounds(10, 8, 278, 14);
			contentPane.add(lblModList);
			
			textField = new JTextField();
			textField.setBounds(298, 4, 180, 21);
			contentPane.add(textField);
			textField.setForeground(Color.GRAY);
			textField.setText("Search");
			textField.addFocusListener(new FocusListener() {
			    @Override
			    public void focusGained(FocusEvent e) {
			        if (textField.getText().equals("Search")) {
			        	textField.setText("");
			            textField.setForeground(Color.BLACK);
			        }
			    }
			    @Override
			    public void focusLost(FocusEvent e) {
			        if (textField.getText().isEmpty()) {
			        	textField.setForeground(Color.GRAY);
			        	textField.setText("Search");
			        }
			    }
			});
			textField.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent arg0) {
					refreshModList(textField.getText());
					updateModel();
				}
			});
			textField.setColumns(10);
			
			JButton btnSteamWorkshop = new JButton("Steam Workshop");
			btnSteamWorkshop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					openWorkshop();
				}
			});
			btnSteamWorkshop.setBounds(116, 229, 113, 23);
			contentPane.add(btnSteamWorkshop);
			
			
			setLocationRelativeTo(null);
			setResizable(false);
			setVisible(true);

			if(server.getConfig().getParameter("DIRECTORY_SERVER").toString().length() > 0) {
				refreshModList();
			}
			updateModel();
		} catch(Exception e) {
			e.printStackTrace();
		}
	
	}

	private void openWorkshop() {
		if(server.getConfig().getParameter("DIRECTORY_SERVER").toString().length() > 0) {
			System.out.println("VISIB");
			workshopWindow.showWindow();
		} else {
			//parent.steamCMDConsole.append("Workshop directory is empty!\n");
			String s = JOptionPane.showInputDialog(null, "Enter A Valid Workshop Directory ( 'Arma 3/!Workshop' )", "Invalid Workshop Directory", JOptionPane.CLOSED_OPTION);
			System.out.println(s);
			if(s != null) {
				server.getConfig().setParameter("DIRECTORY_SERVER", s);
				server.getConfig().save();
	    		openWorkshop();
			}
		}
	}
	private void updateModel() {
		//save current model to file and update it
		/*parent.config.modsEnabled = parent.MODS_LIST;
		parent.config.save();*/
		for(int i = 0; i < getModel().size(); i++) {
			if(modList.contains(getModel().getElementAt(i).toString())) {
				getModel().getElementAt(i).setSelected(true);
			} else {
				getModel().getElementAt(i).setSelected(false);
			}
		}
		list.repaint();
		server.updateModParameter();
		lblModList.setText("Mods (" + getModel().size() + " total | " + modList.size() + " enabled)");
	}

	private void refreshModList(String filter) {
		getModel().clear();
		File[] fileList = new File(server.getConfig().getParameter("DIRECTORY_SERVER").toString()).listFiles();
		for(File f : fileList) {
			if((f.getName().contains("@") && f.getName().toLowerCase().contains(filter.toLowerCase())) || (f.getName().contains("@") && filter == "")) {
				try {
					File modCpp = new File(f.getAbsolutePath() + File.separator + "mod.cpp");
					if(modCpp.exists()) {
						JSONObject obj = ArmaCFGParser.parse(new File(f.getAbsolutePath() + File.separator + "mod.cpp"));
						if(obj.get("name") != null) {
							ArmaMod aMod = new ArmaMod(f.getName(), obj.get("name").toString());
							float size = A3ServerManager.getFileFolderSize(new File(f.getAbsolutePath() + File.separator)) / 1024f;
							if(size / 1024f > 1024) {
								aMod.fileSize = A3ServerManager.df.format(size / 1024f / 1024f) + "GB";
							} else {
								aMod.fileSize = A3ServerManager.df.format((size / 1024f) / 1024f) + "MB";
							}
							getModel().addElement(aMod);
						} else {
							//normal fallback
							ArmaMod aMod = new ArmaMod(f.getName(), f.getName());
							float size = A3ServerManager.getFileFolderSize(new File(f.getAbsolutePath() + File.separator)) / 1024f;
							if(size / 1024f > 1024) {
								aMod.fileSize = A3ServerManager.df.format(size / 1024f / 1024f) + "GB";
							} else {
								aMod.fileSize = A3ServerManager.df.format((size / 1024f) / 1024f) + "MB";
							}
							getModel().addElement(aMod);
						} 
					} else {
						//fallback fallback
						ArmaMod aMod = new ArmaMod(f.getName(), f.getName());
						float size = A3ServerManager.getFileFolderSize(new File(f.getAbsolutePath() + File.separator)) / 1024f;
						if(size / 1024f > 1024) {
							aMod.fileSize = A3ServerManager.df.format(size / 1024f / 1024f) + "GB";
						} else {
							aMod.fileSize = A3ServerManager.df.format((size / 1024f) / 1024f) + "MB";
						}
						getModel().addElement(aMod);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		list.setModel(getModel());
		//lblModList.setText("Mods (" + getModel().size() + " total | " + modList.size() + " enabled)");
	}
	private void refreshModList() {
		refreshModList("");
	}
	
	private DefaultListModel<ArmaMod> getModel() {
		return (DefaultListModel<ArmaMod>) list.getModel();
	}
}
