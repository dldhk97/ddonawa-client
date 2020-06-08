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
import utility.IOHandler;

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
	
	// 서버에게 ProtocolType으로, 특정 객체를 보냄. 요청만 한다면 null로 보냄.
	// 로그인의 경우 타입을 Login으로, Object를 Account로 보냄.
	// 결과는 Protocol로 반환되고 Protocol에는 Type, Response(반응 타입, 메시지), Object가 있음.
	// type은 요청했던 게 무엇인지, 반응은 성공/실패/오류/모름 과 메시지를 담고 있고, Object는 배열이 될수도, 객체가 될 수도, null이 될 수도 있음.
	// 반응은 무조건 담겨져 나오기 때문에, 반응에서 SUCCEED 인지 체크하고, Object를 알맞게 형변환해서 사용하면 됨.
	public Protocol connect(ProtocolType type, Object object) throws Exception{
		return connect(type, null, object);
	}
	
	public Protocol connect(ProtocolType type, EventType eventType, Object object) throws Exception {
		if(type == ProtocolType.EVENT && eventType == null) {
			IOHandler.getInstance().log("프로토콜이 이벤트인데, 이벤트 타입이 없습니다!");
			throw new Exception();
		}	
		
		// 소켓 생성함.
		Socket clientSocket = null;
		try {
			clientSocket = createSocket();			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		if(clientSocket == null) {
			return new Protocol(type, Direction.TO_CLIENT, eventType, new Response(ResponseType.SERVER_NOT_RESPONSE, "서버 응답 없음"), null);
		}
		
		try {
			// 전달할 프로토콜 생성 및 전송
			Protocol sendProtocol = new Protocol(type, Direction.TO_SERVER, eventType, null, object);
			sendData(clientSocket, sendProtocol);
			
			// 데이터 수신
			Protocol receieved = getData(clientSocket);
			
			// 소켓 종료
			clientSocket.close();
			
			// 프로토콜 반환
			if(receieved != null) {
				return receieved;
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
		return null;
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
