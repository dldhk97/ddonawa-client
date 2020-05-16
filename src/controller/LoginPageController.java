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
    @FXML
    private Button RegisterBtn;

    // 아이디 필드 엔터 시
    @FXML
    void On_idFiled_Typed(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER || event.getCharacter().equals("\r")) {
    		pwField.requestFocus();		// 패스워드 필드로 포커스
        }
    }
    
    // 패스워드 필드 엔터 시
    @FXML
    void On_pwField_Typed(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER || event.getCharacter().equals("\r")) {
            tryLogin();					// 로그인 시도
        }
    }

    // 로그인 버튼 클릭 시
    @FXML
    void On_loginBtn_Clicked(ActionEvent event) {
    	tryLogin();
    }
    
    // 회원가입 클릭 시
    @FXML
    void OnRegisterBtnClicked(ActionEvent event) {    	
    	moveToRegister();
    }

    
    private void tryLogin() {
        try {
        	String inputUserId = idField.getText();
            String inputUserPw = pwField.getText();
            
        	// 아이디 혹은 비밀번호가 비어있으면 빠꾸
        	if(inputUserId.isEmpty()) {
        		IOHandler.getInstance().showAlert("아이디가 비어있습니다.");
        		idField.requestFocus();
        		return;
        	}
        	else if(inputUserPw.isEmpty()) {
        		IOHandler.getInstance().showAlert("비밀번호가 비어있습니다.");
        		pwField.requestFocus();
        		return;
        	}
        	
        	// 대충 서버 연결해서 아이디 비번 체크
        	// boolean isLoginSucceed = NetworkManager.login(inputUserId, inputUserPw);		// 대강 이런식으로...
            boolean isLoginSucceed = true;
            
            
            // 서버로부터 결과 나오면 처리
            if(isLoginSucceed) {
            	IOHandler.getInstance().showAlert("로그인 성공");
            	// 사용자 정보를 서버로부터 받아와서 저장해야 할 듯? 사용자명이라던가(지금은 없지만 있으면 좋을듯?), 찜 목록이라던가             	
                moveToMain();
            }
            else {
            	IOHandler.getInstance().showAlert("로그인에 실패했습니다. 다시 시도해주세요.");
            	idField.requestFocus();															// 아이디 필드로 포커스
            }
            
        }
        catch(Exception e) {
        	// 알 수 없는 예외 터지면 알림 띄우고, 로그에 남김
        	String errorMsg = "LoginPageController.tryLogin\n" + e.getMessage();
        	IOHandler.getInstance().showAlert(errorMsg);
        	IOHandler.getInstance().log(errorMsg);
        }
    }
    
    //메인 화면으로 이동하는 메소드
    private void moveToMain() {
        try {
            //메인페이지로 이동하기
            Stage primaryStage = (Stage) loginBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/page/MainPage.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
        	String errorMsg = "LoginPageController.moveToMain\n" + e.getMessage();
        	IOHandler.getInstance().showAlert(errorMsg);
        	IOHandler.getInstance().log(errorMsg);
        }
    }
    
    //회원가입 화면으로 이동하는 메소드
    private void moveToRegister() {
        try {           
            Stage primaryStage = (Stage) RegisterBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/page/RegisterPage.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("또나와 회원가입");
            primaryStage.show();

        } catch (Exception e) {
        	String errorMsg = "LoginPageController.moveToRegister\n" + e.getMessage();
        	e.printStackTrace();
        	IOHandler.getInstance().showAlert(errorMsg);
        	IOHandler.getInstance().log(errorMsg);
        }
    }
    
}
