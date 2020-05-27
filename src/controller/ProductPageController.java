package controller;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;


public class ProductPageController implements Initializable {	
	 @FXML
	    private ImageView Image;

	    @FXML
	    private Button zzimBtn;

	    @FXML
	    private HBox zzimBar;

	    @FXML
	    private TextField targetPrice;

	    @FXML
	    private Button zzimRegister;
    Popup popup;
    TextArea textArea;
    
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
    	popup = new Popup();
    	textArea = new TextArea("팝업");
    	
    	// 맨 처음 초기화 하면서 차트, 이름, 가격, URL, 썸네일 등 초기화 해서 화면 보여줘야함 
    	
		// 이미지 설정하는 부분
		String s = "http://img.danawa.com/prod_img/500000/562/196/img/5196562_1.jpg?";
		Image img = new Image(s);
		Image.setImage(img); 	    
	    
	}
    
    @FXML
    void OnZzimRegisterBtnClicked(ActionEvent event) {
    	
    }

    
    @FXML
    void OnZzimBtnClicked(ActionEvent event) {
//    	if(popup.isShowing()) {
//    		popup.hide();
//    	}
//    	else {
//    		final Window window = zzimBtn.getScene().getWindow();
//    		popup.setWidth(100);
//    		popup.setHeight(300);
//    		
//    		final double x = window.getX()+zzimBtn.localToScene(0,0).getX() + zzimBtn.getScene().getX();
//    		final double y = window.getY()+zzimBtn.localToScene(0,0).getY() + zzimBtn.getScene().getHeight();
//    		
//    		popup.getContent().clear();
//    		popup.getContent().addAll(textArea);
//    		popup.show(window);
//    	}
    	zzimBar.setVisible(true);
    }
}
