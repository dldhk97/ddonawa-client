package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import model.Account;

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
	
	private final static String SERVER_IP = NetworkInfo.SERVER_IP.toString();
	private final static int PORT = Integer.parseInt(NetworkInfo.SERVER_PORT.toString());
	private final static int TIMEOUT = Integer.parseInt(NetworkInfo.SERVER_TIMEOUT.toString());
	
	
	
	public LoginResult tryLogin(Account account) {
		Socket clientSocket = createSocket();
		if(clientSocket == null) {
			return LoginResult.ERROR;
		}
		
		try {
			Protocol sendProtocol = new Protocol(ProtocolType.LOGIN, Direction.TO_SERVER, account);
			
			sendData(clientSocket, sendProtocol);
			Protocol receieved = getData(clientSocket);
			
			LoginResult result = (LoginResult) receieved.getObject();
			
			clientSocket.close();
			if(result != null) {
				return result;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// 미워도 다시 한번 닫기
		try {
			if(clientSocket != null) {
				clientSocket.close();
			}			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return LoginResult.UNKNOWN;
	}
	
	// ----------------------------------- 프로토콜 ------------------------------//
	
	private Protocol getData(Socket s){
		try {
			
			ObjectInputStream os = new ObjectInputStream(s.getInputStream());
			
			Protocol p = (Protocol) os.readObject();
			
			return p;
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private boolean sendData(Socket s, Protocol p) {
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(s.getOutputStream());
			objectOutputStream.writeObject(p);
			objectOutputStream.flush();
			
			return true;
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private Socket createSocket() {
		try {
			Socket socket = new Socket(SERVER_IP, PORT);
			socket.setSoTimeout(TIMEOUT);
			return socket;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
