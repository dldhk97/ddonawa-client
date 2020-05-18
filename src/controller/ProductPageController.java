package controller;

import java.awt.Desktop;
import java.net.URI;
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
    
    
    //일단 이미지 클릭시 링크로 기본웹브라우저 통해서 접근
    //URI 오류는 보통 링크를 틀렸을때 많이 나옴 , http:// 붙였는지 등등 확인 필요
    @FXML
    void OnProductImageClicked(MouseEvent event)  {
    	try {     		
    		Desktop.getDesktop().browse(new URI("http://prod.danawa.com/info/?pcode=9491865&keyword=%EA%B3%A0%EA%B8%B0&cate=1622520")); 
    	} catch (Exception e) {
    		e.printStackTrace(); 
    	}

    }    
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	
    	
    	// 맨 처음 초기화 하면서 차트, 이름, 가격, URL, 썸네일 등 초기화 해서 화면 보여줘야함 
    	
		// 이미지 설정하는 부분
		String s = "http://img.danawa.com/prod_img/500000/562/196/img/5196562_1.jpg?";
		Image img = new Image(s);
		Image.setImage(img); 
	    
	    
	}
}
