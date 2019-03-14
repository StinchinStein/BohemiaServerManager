package com.raffertynh.server;

import javax.swing.JPanel;

import org.json.simple.JSONArray;

import com.raffertynh.a3server.A3ServerManager;
import com.raffertynh.a3server.Configuration;

public class BohemiaServer extends JPanel implements Runnable {

	public boolean SERVER_RUNNING = false;
	private Thread thread;
	public Process serverProcess;

	public String execString;
	public A3ServerManager parent;
	private Configuration cfg;
	
	public JSONArray MODS_LIST = new JSONArray();
	public String MODS_PARAM = "";
	
	public BohemiaServer(A3ServerManager parent, String sName) {
		this.parent = parent;
		cfg = new Configuration(sName);
	}
	
	public void startServer(String execString) {
		this.execString = execString;
		thread = new Thread(this);
		thread.start();
	}
	public void run() {
		System.out.println("No server specified, not starting anything");
		try {
			thread.join();
		} catch (InterruptedException e) {}
	}
	
	public Configuration getConfig() {
		return cfg;
	}
	public void stopServer() {
		serverProcess.destroy();
	}
	
	public void updateModParameter() {
		String tMd = "-mod=\"";

		for(Object s : MODS_LIST) {
			tMd += s.toString() + ";";
		}
		MODS_PARAM = tMd + "\"";
		System.out.println("MOD PARAMETER: " + MODS_PARAM);	
	}
	
}
