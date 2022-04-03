package me.freeze;

import java.net.InetAddress;

public class Connection extends Thread{

	InetAddress CONNECTION;
	int timeout = 15;
	int ctime = 0;
	public Connection(InetAddress CONNECTION) {
		this.CONNECTION = CONNECTION;
	}
	
	public void run() {
		while(Main.CONNECTION_STATUS.get(CONNECTION)) {
			try {
				Main.CONNECTION_TIMEOUT.put(CONNECTION, Main.CONNECTION_TIMEOUT.get(CONNECTION)+1);
//				System.out.println(Main.CONNECTION_TIMEOUT.get(CONNECTION)); DEBUG
				if(Main.CONNECTION_TIMEOUT.get(CONNECTION) > timeout) {
					Main.CONNECTION_STATUS.put(CONNECTION, false);
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Main.CONNECTION_STATUS.put(CONNECTION, false);
		Main.CONNECTION_TIMEOUT.remove(CONNECTION);
	}
	
}
