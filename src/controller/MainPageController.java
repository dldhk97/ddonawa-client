package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import utility.IOHandler;

public class MainPageController {

    @FXML
    private Button testBtn1;

    @FXML
    private Button testBtn2;

    @FXML
    private Button searchBtn;

    @FXML
    void OnClikedSearchBtn(ActionEvent event) {
    	//검색 수행
    	IOHandler.getInstance().showAlert("검색버튼 클릭");
    	moveToSearchPage();
    }

    @FXML
    void On_testBtn1_Clicked(ActionEvent event) {

    }

    @FXML
    void On_testBtn2_Clicked(ActionEvent event) {

    }

    //메인 화면으로 이동하는 메소드
    private void moveToSearchPage() {
        try {
            //검색페이지로 이동하기
            Stage primaryStage = (Stage) searchBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/page/SearchPage.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("또나와 검색결과");
            primaryStage.show();

        } catch (Exception e) {
        	String errorMsg = "LoginPageController.moveToSearchPage\n" + e.getMessage();
        	IOHandler.getInstance().showAlert(errorMsg);
        	IOHandler.getInstance().log(errorMsg);
        }
    }
}

