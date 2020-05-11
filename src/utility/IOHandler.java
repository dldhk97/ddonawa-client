package utility;

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
	public void showAlert(String s)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(s);
		alert.showAndWait();
	}

	// 콘솔 or 텍스트 파일에 로깅
	public void log(String s) {
		// [날짜+시간+사용자명]+오류명 이런식으로 로깅하게 할 예정임
		System.out.println(s);
	}
}
