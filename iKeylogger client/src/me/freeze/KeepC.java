package me.freeze;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;

public class KeepC extends Thread{
	
	private Socket socket;

	public void run() {
		while(true) {
			try {
				Thread.sleep(5000);
				socket = new Socket(Main.IP_ADDRESS, Main.PORT);
				PrintWriter p = new PrintWriter(socket.getOutputStream(), true);
				JSONObject jo = new JSONObject();
				jo.put("type", "connection");
				p.println(jo);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
