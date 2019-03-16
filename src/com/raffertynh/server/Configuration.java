package com.raffertynh.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JFileChooser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Configuration {

	public static String ROOT_FOLDER = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();


	public JSONObject SAVE_VALUES = new JSONObject();
	
	private File CFG_FILE;
	private File CFG_LOC;
	public Configuration(String saveLoc) {
		System.out.println("Operating System: " + System.getProperty("os.name"));
		//root folder for saving the configuration file
		if(System.getProperty("os.name").toLowerCase().contains("linux")) {
			//linux: /home/<user>/config.json
			ROOT_FOLDER = System.getProperty("user.home");
			CFG_FILE = new File(ROOT_FOLDER + "/config.json");
			CFG_LOC = new File(ROOT_FOLDER + "/");
		} else {
			//windows: Documents/BJ Pandora/config.json
			CFG_FILE = new File(ROOT_FOLDER + "\\StinchinStein\\" + saveLoc + "\\config.json");
			CFG_LOC = new File(ROOT_FOLDER + "\\StinchinStein\\" + saveLoc + "\\");
		}
		
		if(!CFG_LOC.exists()) CFG_LOC.mkdirs(); 
		//default settings
		
		
		//load if file exists, else? Save default to file.
	}

	public void finish() {
		
		if(CFG_FILE.exists()) {
			load(); //load file
		} else {
			save(); //save all values to default in new file.
		}
		updateConfig();
	}
	public void setParameter(String key, Object value) {
		SAVE_VALUES.put(key, value);
	}
	public void setDefaultParameter(String key, String defaultValue) {
		if(!SAVE_VALUES.containsKey(key)) {
			SAVE_VALUES.put(key, defaultValue);	
		}
	}
	public Object getParameter(String key) {
		try {
			return SAVE_VALUES.get(key);
		} catch(Exception e) {
			return "-1";
		}
		
	}
	
	public void removeParameter(String key) {
		SAVE_VALUES.remove(key);
	}
	
	public void updateConfig() {
		//completely unused as far as I know...
		
		//DEPRECATED
		//UNDO DEPRECATION, THIS IS USED
		
		//saves to existing file, but sets values to the default if they don't exist
		//pretty much for when we add new values in future updates, it won't overwrite user settings.
		try {
			FileReader configurationFile = new FileReader(CFG_FILE);
			JSONObject obj = (JSONObject) new JSONParser().parse(configurationFile);
			configurationFile.close();

			for(Iterator iterator = SAVE_VALUES.keySet().iterator(); iterator.hasNext();) {
			    String key = (String) iterator.next();
			    obj.put(key, SAVE_VALUES.get(key));
			}
			
			FileOutputStream writer = new FileOutputStream(CFG_FILE);
			writer.write(obj.toJSONString().getBytes());
			writer.close();
			System.out.println("[Configuration] Saved Existing File!");
		} catch(Exception e) {}
	}
	
	public void save() {
		try {
			FileOutputStream writer = new FileOutputStream(CFG_FILE);
			JSONObject obj = new JSONObject();

			for(Iterator iterator = SAVE_VALUES.keySet().iterator(); iterator.hasNext();) {
			    String key = (String) iterator.next();
			    obj.put(key, SAVE_VALUES.get(key));
			}
			
			writer.write(obj.toJSONString().getBytes());
			writer.close();
			System.out.println("[Configuration] Saved New File!");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	//load file values to this class for use in the program
	public void load() {
		 System.out.println("[Configuration] Loading...");
		
		try {
			FileReader configurationFile = new FileReader(CFG_FILE);
		    JSONObject obj = (JSONObject) new JSONParser().parse(configurationFile);

			for(Iterator iterator = obj.keySet().iterator(); iterator.hasNext();) {
			    String key = (String) iterator.next();
			    SAVE_VALUES.put(key, obj.get(key));
			}
		    System.out.println("[Configuration] Loaded!");
		} catch(Exception e) {
		    System.out.println("[Configuration] Failed!");
		    e.printStackTrace();
		}
	}

	
	
	//from an old game my friend and I worked on
	
	/*public void apply(PandoraAPI pandora) {
		//apply configuration to game values
		pandora.user = WIDTH;
		pandora.HEIGHT = HEIGHT;
		pandora.display = fullscreenDisplay;
	}*/
}
