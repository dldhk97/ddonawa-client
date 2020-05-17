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
    
    
    
    //이미지 클릭시 이벤트 설정
    
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	
    	
    	// 맨 처음 초기화 하면서 차트, 이름, 가격, URL, 썸네일 등 초기화 해서 화면 보여줘야함 
    	
		// 이미지 설정하는 부분
		String s = "http://img.danawa.com/prod_img/500000/562/196/img/5196562_1.jpg?";
		Image img = new Image(s);
		Image.setImage(img); 
	    
	    
	}
}
