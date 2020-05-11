package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import utility.IOHandler;

public class LoginPageController {

	@FXML
    private TextField idField;

    @FXML
    private PasswordField pwField;
	
    @FXML
    private Button loginBtn;

    // ���̵� �ʵ� ���� ��
    @FXML
    void On_idFiled_Typed(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER || event.getCharacter().equals("\r")) {
    		pwField.requestFocus();		// �н����� �ʵ�� ��Ŀ��
        }
    }
    
    // �н����� �ʵ� ���� ��
    @FXML
    void On_pwField_Typed(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER || event.getCharacter().equals("\r")) {
            tryLogin();					// �α��� �õ�
        }
    }

    @FXML
    void On_loginBtn_Clicked(ActionEvent event) {
    	tryLogin();
    }

    
    private void tryLogin() {
    	String inputUserId = idField.getText();
        String inputUserPw = pwField.getText();
        
        try {
        	// ���� ���� �����ؼ� ���̵� ��� üũ
        	// boolean isLoginSucceed = NetworkManager.tryLogin(inputUserId, inputUserPw);		// �밭 �̷�������...
        	
            boolean isLoginSucceed = true;
            if(isLoginSucceed) {
            	IOHandler.getInstance().showAlert("�α��� ����");
            	// ����� ������ �����κ��� �޾ƿͼ� �����ؾ� �� ��? ����ڸ��̶����(������ ������ ������ ������?), �� ����̶���� 
                moveToMain();
            }
            else {
            	IOHandler.getInstance().showAlert("�α��ο� �����߽��ϴ�. �ٽ� �õ����ּ���.");
            	idField.requestFocus();															// ���̵� �ʵ�� ��Ŀ��
            }
            
        }
        catch(Exception e) {
        	String errorMsg = "LoginPageController.tryLogin\n" + e.getMessage();
        	IOHandler.getInstance().showAlert(errorMsg);
        	IOHandler.getInstance().log(errorMsg);
        }
    }
    
    //���� ȭ������ �̵�
    private void moveToMain() {
        try {
            //������������ �̵��ϱ�
            Stage primaryStage = (Stage) loginBtn.getScene().getWindow(); 			// �⺻ �������� ��������
            Parent root = FXMLLoader.load(getClass().getResource("/page/MainPage.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    

}