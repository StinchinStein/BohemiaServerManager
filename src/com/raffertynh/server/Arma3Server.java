package com.raffertynh.server;

import com.raffertynh.a3server.A3ServerManager;
import com.raffertynh.a3server.ServerType;

public class Arma3Server extends BohemiaServer {
	
	public Arma3Server(A3ServerManager parent) {
		super(parent, "Arma 3");

		getConfig().setDefaultParameter("DIRECTORY_SERVER", "");
		getConfig().setDefaultParameter("DIRECTORY_WORKSHOP", "");
		getConfig().setDefaultParameter("DIRECTORY_PROFILE", "");
		getConfig().setDefaultParameter("MODS_PARAMETER", "");
		
		getConfig().finish();
	}
	
	public void run() {
		try {
			SERVER_RUNNING = true;
    		parent.onServerStarted(ServerType.ARMA_3_SERVER);
			serverProcess = Runtime.getRuntime().exec(getConfig().getParameter("DIRECTORY_SERVER") + "\\arma3server_x64.exe " + execString); //INSTALL_DIR + "\\arma3server_x64.exe -name=A3Server -world=altis -config=CONFIG_server.cfg " + MODS_PARAM
			while(serverProcess.isAlive()) {}
    		SERVER_RUNNING = false;
    		parent.onServerStopped(ServerType.ARMA_3_SERVER);
		} catch(Exception e) {
    		SERVER_RUNNING = false;
    		parent.onServerStopped(ServerType.ARMA_3_SERVER);
		}
	}

}
