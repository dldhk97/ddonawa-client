package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    	//IOHandler.getInstance().showAlert("검색버튼 클릭");
    	//검색 수행
    }

    @FXML
    void On_testBtn1_Clicked(ActionEvent event) {

    }

    @FXML
    void On_testBtn2_Clicked(ActionEvent event) {

    }

}
