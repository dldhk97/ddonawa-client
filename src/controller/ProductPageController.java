package controller;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import utility.IOHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
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
	    
	    @FXML
	    private Button goToMainBtn;

	    @FXML
	    void OnGoToMainBtnClicked(ActionEvent event) {
	    	 try {
	             //메인페이지로 이동하기
	             Stage primaryStage = (Stage) Image.getScene().getWindow();
	             Parent root = FXMLLoader.load(getClass().getResource("/page/MainPage.fxml"));
	             //Scene scene = new Scene(root);    	    
	            	
	             Pane root1 = new Pane();
	        	 	root1.setPrefSize(600, 400);    
	        	 	VBox menu = new VBox();
	        	 
	        	    menu.setId("menu");
	        	    menu.prefHeightProperty().bind(root1.heightProperty());
	        	    menu.setPrefWidth(100);

	        	   // 나중에 동적으로 추가해줘야 할듯
	        	    MenuItem menuItem1 = new MenuItem("A-1");
	        	    MenuItem menuItem2 = new MenuItem("A-2");
	        	 	MenuItem menuItem3 = new MenuItem("A-3");
	        	 	MenuButton menuBtn1= new MenuButton("카테고리 A", null,menuItem1,menuItem2,menuItem3);
	        	 	
	        	 	MenuItem menuItem4 = new MenuItem("B-1");
	     	    MenuItem menuItem5 = new MenuItem("B-2");
	     	 	MenuItem menuItem6 = new MenuItem("B-3");
	     	 	MenuButton menuBtn2= new MenuButton("카테고리 B", null,menuItem4,menuItem5,menuItem6);
	        	 	menu.getChildren().addAll(menuBtn1,menuBtn2);

	        	    menu.getStylesheets().add(getClass().getResource("/application/menustyle.css").toExternalForm());
	        	    menu.setTranslateX(-90);
	        	    TranslateTransition menuTranslation = new TranslateTransition(Duration.millis(500), menu);

	        	    menuTranslation.setFromX(-90);
	        	    menuTranslation.setToX(0);

	        	    menu.setOnMouseEntered(evt -> {
	        	        menuTranslation.setRate(1);
	        	        menuTranslation.play();
	        	    });
	        	    menu.setOnMouseExited(evt -> {
	        	        menuTranslation.setRate(-1);
	        	        menuTranslation.play();
	        	    });
	        	    
	        	    root1.getChildren().addAll(root,menu);
	        	    Scene scene = new Scene(root1);
	        	    
	        	    
	             primaryStage.setScene(scene);
	             primaryStage.setTitle("또나와 메인화면");
	             primaryStage.show();

	         } catch (Exception e) {
	         	String errorMsg = "LoginPageController.moveToMain\n" + e.getMessage();
	         	IOHandler.getInstance().showAlert(errorMsg);
	         	IOHandler.getInstance().log(errorMsg);
	         }
	    }
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
