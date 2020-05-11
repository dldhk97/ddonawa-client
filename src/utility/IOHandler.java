package utility;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class IOHandler {
	
	// �̱��� ����
	private static IOHandler _instance;
	
	// IOHandler ��� �� IOHandler.getInstance().�޼ҵ�� ���� ����ϸ� ��.
	public static IOHandler getInstance()
	{
		if(_instance == null)
			_instance = new IOHandler();
		return _instance;
	}
	
	//���â ���� �޼ҵ�
	public void showAlert(String s)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(s);
		alert.showAndWait();
	}

	// �ܼ� or �ؽ�Ʈ ���Ͽ� �α�
	public void log(String s) {
		// [��¥+�ð�+����ڸ�]+������ �̷������� �α��ϰ� �� ������
		System.out.println(s);
	}
}
