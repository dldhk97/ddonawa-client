package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Account;
import task.AccountTask;
import utility.IOHandler;

public class RegisterPageController {

    @FXML
    private TextField IdField;

    @FXML
    private PasswordField PwdField;

    @FXML
    private Button RegisterBtn;

    @FXML
    private Button BackBtn;

    @FXML
    void OnBackBtnClicked(ActionEvent event) {
    	moveToLoginPage();
    }

    @FXML
    void OnRegisterBtnClicked(ActionEvent event) {
    	tryRegister();
    }
    
    private void tryRegister() {
        try {
        	String inputUserId = IdField.getText();
            String inputUserPw = PwdField.getText();
            
        	// 아이디 혹은 비밀번호가 비어있으면 빠꾸
        	if(inputUserId.isEmpty()) {
        		IOHandler.getInstance().showAlert("아이디가 비어있습니다.");
        		IdField.requestFocus();
        		return;
        	}
        	else if(inputUserPw.isEmpty()) {
        		IOHandler.getInstance().showAlert("비밀번호가 비어있습니다.");
        		PwdField.requestFocus();
        		return;
        	}
        	
        	AccountTask a = new AccountTask();
        	Account inputAccount = new Account(IdField.getText(), PwdField.getText());
        	boolean isRegisterSucceed = a.register(inputAccount);      	
                 
            
            // 서버로부터 결과 나오면 처리
            if(isRegisterSucceed) {
            	IOHandler.getInstance().showAlert("회원가입 성공");
            	// 사용자 정보를 서버로부터 받아와서 저장해야 할 듯? 사용자명이라던가(지금은 없지만 있으면 좋을듯?), 찜 목록이라던가             	
                moveToLoginPage();
            }
            else {
            	IOHandler.getInstance().showAlert("회원가입 실패");            	
            }
            
        }
        catch(Exception e) {
        	// 알 수 없는 예외 터지면 알림 띄우고, 로그에 남김
        	String errorMsg = "LoginPageController.tryLogin\n" + e.getMessage();
        	IOHandler.getInstance().showAlert(errorMsg);
        	IOHandler.getInstance().log(errorMsg);
        }
    }
    
    //로그인 화면으로 이동하는 메소드
    private void moveToLoginPage() {
        try {           
            Stage primaryStage = (Stage) BackBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/page/LoginPage.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("또나와 로그인");
            primaryStage.show();

        } catch (Exception e) {
        	String errorMsg = "LoginPageController.moveToLogin\n" + e.getMessage();
        	e.printStackTrace();
        	IOHandler.getInstance().showAlert(errorMsg);
        	IOHandler.getInstance().log(errorMsg);
        }
    }
}
