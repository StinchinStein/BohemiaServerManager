package com.raffertynh.window;

import java.awt.Canvas;
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
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;

import com.raffertynh.bohemiainteractive.ArmaCFGParser;
import com.raffertynh.bohemiainteractive.BohemiaServer;
import com.raffertynh.renderer.ArmaList;
import com.raffertynh.renderer.ArmaMod;
import com.raffertynh.renderer.InventoryRenderer;
import com.raffertynh.server.A3ServerManager;

public class A3WorkshopWindow extends JFrame {

	private JPanel contentPane;
	private DefaultTableModel tblModel;
	private Canvas canvas;
	private int noteID = 0;
	public String winType;
	public JSONObject obj = new JSONObject();
	A3ServerManager parent;
	private ArmaList list;
	private JButton btnSaveVehicle;
	private ArrayList<String> WORKSHOP_SELECTED = new ArrayList<String>();
	private JTextField textField;
	
	private BohemiaServer server;
	
	class CopyFilesThread extends Thread {
		public void run() {
			btnSaveVehicle.setEnabled(false);
			dispose();
			//COPY MODS FROM ARMA WORKSHOP TO SERVER FOLDER!
			int containIndex = 0;
			for(String mod : WORKSHOP_SELECTED) {
				if(installDirMods().contains(mod)) {
					containIndex++;
				}
				//TODO CHECK FOLDER
			}
			if(containIndex > 0) {
				int opt = JOptionPane.showConfirmDialog(null, "Overwrite " + containIndex + " mods?\nThis may take a while.", "Overwrite Files", JOptionPane.OK_OPTION);
				System.out.println("OPTION : " + opt);
				if(opt == 1) return;
			}
			updateServerMods();
			if(WORKSHOP_SELECTED.size() > 0) {
				JOptionPane.showConfirmDialog(null, "Finished Copying " + WORKSHOP_SELECTED.size() + " files!", "Finished Copying", JOptionPane.CLOSED_OPTION);
			}
			btnSaveVehicle.setEnabled(true);
			//dispose();
		}
	}
	
	public ArrayList<String> installDirMods() {
		ArrayList<String> ml = new ArrayList<String>();
		File[] fileList = new File(server.getConfig().getParameter("DIRECTORY_SERVER").toString()).listFiles();
		for(File f : fileList) {
			if(f.getName().contains("@")) {
				ml.add(f.getName());
			}
		}
		return ml;
	}
	public void updateServerMods() {
		for(String mod : WORKSHOP_SELECTED) {
			try {
				File modFile = new File(server.getConfig().getParameter("DIRECTORY_SERVER").toString() + File.separator + mod + File.separator);
				File modWorkshopFile = new File(server.getConfig().getParameter("DIRECTORY_WORKSHOP").toString() + File.separator + mod + File.separator);
				if(modFile.exists()) {/*
					int op = JOptionPane.showConfirmDialog(null, "The mod '" + mod + "' already exists! Replace?", "File Copy Failure", JOptionPane.YES_NO_OPTION);
					if(op == 0) {
					}*/
					//parent.steamCMDConsole.append("Removing old '" + mod + "'\n");
					FileUtils.deleteDirectory(new File(server.getConfig().getParameter("DIRECTORY_SERVER").toString() + File.separator + mod + File.separator));
					//parent.steamCMDConsole.append("Copying " + mod + " - " + parent.df.format((parent.getFileFolderSize(modWorkshopFile) / 1024f) / 1024f) + "MB...\n");
					FileUtils.copyDirectory(new File(server.getConfig().getParameter("DIRECTORY_WORKSHOP").toString() + File.separator + mod + File.separator), new File(server.getConfig().getParameter("DIRECTORY_SERVER").toString() + File.separator + mod + File.separator));
					//parent.steamCMDConsole.append("Finished " + mod + "!\n"); 
				} else {
					//parent.steamCMDConsole.append("Copying " + mod + " - " + parent.df.format((parent.getFileFolderSize(modWorkshopFile) / 1024f) / 1024f) + "MB...\n");
					FileUtils.copyDirectory(new File(server.getConfig().getParameter("DIRECTORY_WORKSHOP").toString() + File.separator + mod + File.separator), new File(server.getConfig().getParameter("DIRECTORY_SERVER").toString() + File.separator + mod + File.separator));
					//parent.steamCMDConsole.append("Finished " + mod + "!\n"); 
				}
			} catch (IOException e1) {}
		}
	}
	
	
	
	public A3WorkshopWindow(final A3ServerManager parent, BohemiaServer server) {
		this.parent = parent;
		this.server = server;
		try {
			setTitle("A3ServerLauncher.JAVA - WORKSHOP");
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 596, 290);
			contentPane = new JPanel();
			contentPane.setBorder(null);
			setContentPane(contentPane);
			contentPane.setLayout(null);
			btnSaveVehicle = new JButton("Update Mods");
			btnSaveVehicle.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new Thread(new CopyFilesThread()).start();
				}
			});
			btnSaveVehicle.setBounds(378, 229, 100, 23);
			contentPane.add(btnSaveVehicle);
			
			JButton btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
			btnCancel.setBounds(11, 229, 100, 23);
			contentPane.add(btnCancel);
			
			JButton btnDeleteNote = new JButton("Delete Mod");
			btnDeleteNote.setEnabled(false);
			btnDeleteNote.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
			btnDeleteNote.setBounds(284, 229, 90, 23);
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
						if(!WORKSHOP_SELECTED.contains(mod)) {
							WORKSHOP_SELECTED.add(list.getModel().getElementAt(list.getSelectedIndex()).toString());
						}
					} else {
						if(WORKSHOP_SELECTED.contains(mod)) {
							WORKSHOP_SELECTED.remove(list.getModel().getElementAt(list.getSelectedIndex()).toString());
						}
					}
	    			updateModel();
	    			/*config.modsEnabled = MODS_LIST;
	    			config.save();
					*/
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
		    			if(!WORKSHOP_SELECTED.contains(mod)) {
		    				WORKSHOP_SELECTED.add(list.getModel().getElementAt(i).toString());
			    		}
		    		}
	    			updateModel();
				}
			});
			btnAll.setBounds(537, 3, 43, 23);
			contentPane.add(btnAll);
			
			JButton btnRefreshMods = new JButton("Refresh Mods");
			btnRefreshMods.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					//parent.reloadModDir(parent.CACHE_WORKSHOP, parent.WORKSHOP_MODS_DIR);
					refreshModList();
				}
			});
			btnRefreshMods.setBounds(480, 229, 100, 23);
			contentPane.add(btnRefreshMods);
			
			JButton btnNone_1 = new JButton("None");
			btnNone_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					for(int i = 0; i < list.getModel().getSize(); i++) {
		    			String mod = list.getModel().getElementAt(i).toString();
		    			if(WORKSHOP_SELECTED.contains(mod)) {
		    				WORKSHOP_SELECTED.remove(list.getModel().getElementAt(i).toString());
			    		}
		    		}
	    			updateModel();
				}
			});
			btnNone_1.setBounds(480, 3, 57, 23);
			contentPane.add(btnNone_1);
			
			JLabel lblNewLabel = new JLabel("Workshop Mods");
			lblNewLabel.setBounds(10, 8, 76, 14);
			contentPane.add(lblNewLabel);
			
			textField = new JTextField();
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
			textField.setBounds(278, 4, 200, 21);
			contentPane.add(textField);
			textField.setColumns(10);
			
			
			setLocationRelativeTo(null);
			setResizable(false);
			/*
			if(server.getConfig().getParameter("DIRECTORY_WORKSHOP").toString().length() > 0) {
				//parent.reloadModDir(parent.CACHE_WORKSHOP, parent.WORKSHOP_MODS_DIR);
				refreshModList();
			}*/
		} catch(Exception e) {
			e.printStackTrace();
		}
	
	}

	
	public void showWindow() {
		System.out.print(server.getConfig().getParameter("DIRECTORY_WORKSHOP").toString());
		refreshModList();
		setVisible(true);
	}
	
	
	private void updateModel() {
		for(int i = 0; i < getModel().size(); i++) {
			if(WORKSHOP_SELECTED.contains(getModel().getElementAt(i).toString())) {
				getModel().getElementAt(i).setSelected(true);
			} else {
				getModel().getElementAt(i).setSelected(false);
			}
		}
		list.repaint();
	}
	
	private void refreshModList(String filter) {
		getModel().clear();
		File[] fileList = new File(server.getConfig().getParameter("DIRECTORY_WORKSHOP").toString()).listFiles();
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
	/*private void refreshModList(String filter) {
		getModel().clear();
		for(ArmaMod mod: parent.CACHE_WORKSHOP) {
			if(mod.folderName.toLowerCase().contains(filter.toLowerCase())) {
				getModel().addElement(mod);
			}
		}
		
		
		list.setModel(getModel());
		list.repaint();
		//.setText("Mods (" + getModel().size() + " total | " + modList.size() + " enabled)");
	}*/
	private void refreshModList() {
		refreshModList("");
	}
	
	private DefaultListModel<ArmaMod> getModel() {
		return (DefaultListModel<ArmaMod>) list.getModel();
	}
}
