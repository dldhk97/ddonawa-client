package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;


public class ProductPageController implements Initializable {

    @FXML
    private ImageView Image;

    @FXML
    void OnProductImageClicked(MouseEvent event) {

    }    
    
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		// 이미지 설정하는 부분
		String s = "http://img.danawa.com/prod_img/500000/562/196/img/5196562_1.jpg?";
		Image img = new Image(s);
		Image.setImage(img);
	}
}
