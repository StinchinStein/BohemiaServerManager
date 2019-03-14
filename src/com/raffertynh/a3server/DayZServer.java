package com.raffertynh.a3server;

import com.raffertynh.server.BohemiaServer;

public class DayZServer extends BohemiaServer {
	
	public DayZServer(A3ServerManager parent) {
		super(parent, "DayZ");

		getConfig().setDefaultParameter("DIRECTORY_SERVER", "");
		getConfig().setDefaultParameter("DIRECTORY_WORKSHOP", "");
		getConfig().setDefaultParameter("MODS_PARAMETER", "");
		
		getConfig().finish();
	}
	
	public void run() {
		try {
			SERVER_RUNNING = true;
    		parent.onServerStarted(ServerType.DAY_Z_SERVER);
			serverProcess = Runtime.getRuntime().exec(getConfig().getParameter("DIRECTORY_SERVER") + "\\DayZServer_x64.exe " + execString); //INSTALL_DIR + "\\arma3server_x64.exe -name=A3Server -world=altis -config=CONFIG_server.cfg " + MODS_PARAM
			while(serverProcess.isAlive()) {}
    		SERVER_RUNNING = false;
    		parent.onServerStopped(ServerType.DAY_Z_SERVER);
		} catch(Exception e) {
    		SERVER_RUNNING = false;
    		parent.onServerStopped(ServerType.DAY_Z_SERVER);
		}
	}

}
