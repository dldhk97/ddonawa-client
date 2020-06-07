package network;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import model.Account;
import task.LoginResult;

public class NetworkManager {
	// 싱글톤 패턴
	private static NetworkManager _instance;
	
	// 다른데서 생성 못하게 생성자를 private로 함. 
	private NetworkManager() {}
	
	// 이 클래스에 접근할 때는 getInstance로만 접근할 것.
	public static NetworkManager getInstance() {
		if(_instance == null)
			_instance = new NetworkManager();
		return _instance;
	}
	
	public LoginResult tryLogin(Account account) {
		
		int port = Integer.parseInt(NetworkInfo.SERVER_PORT.toString());
		int timeout = Integer.parseInt(NetworkInfo.SERVER_TIMEOUT.toString());
		try {
			Socket socket = new Socket(NetworkInfo.SERVER_IP.toString(), port);
			socket.setSoTimeout(timeout);
			
			InputStream data = socket.getInputStream();
			
			byte[] receieved = new byte[100];
			data.read(receieved);
			
			socket.close();
			
			System.out.println(new String(receieved));
			
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return LoginResult.UNKNOWN;
	}
}
