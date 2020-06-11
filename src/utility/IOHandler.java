package utility;

import java.util.Date;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class IOHandler {
	
	// 싱글톤 패턴
	private static IOHandler _instance;
	
	// IOHandler 사용 시 IOHandler.getInstance().메소드명 으로 사용하면 됨.
	public static IOHandler getInstance()
	{
		if(_instance == null)
			_instance = new IOHandler();
		return _instance;
	}
	
	//경고창 띄우는 메소드
	public void showAlert(String msg)
	{
		showAlert("Information", null, msg);
	}
	
	//경고창 띄우는 메소드
	public void showAlert(String title, String msg)
	{
		showAlert(title, null, msg);
	}
	
	//경고창 띄우는 메소드
	public void showAlert(String title, String header, String msg)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(msg);
		alert.showAndWait();
	}

	// 콘솔 or 텍스트 파일에 로깅
	public void log(String s) {
		System.out.println("[" + new Date() + ".Server]" + s);
	}
}
