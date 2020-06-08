package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Product;
import utility.IOHandler;

public class MainPageController implements Initializable {
	
    @FXML
    private Button searchBtn;
    
    @FXML
    private TextField searchField;
    
    @FXML
    void OnSearchPressed(KeyEvent event) {
    	if(event.getCode()==KeyCode.ENTER)
    	{
    		moveToSearchPage();
    	}
    }
    @FXML
    void OnClikedSearchBtn(ActionEvent event) {   	
    	moveToSearchPage();
    }

    @FXML
    void On_testBtn1_Clicked(ActionEvent event) {

    }

    @FXML
    void On_testBtn2_Clicked(ActionEvent event) {

    }
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
//		// 머품목 갯수 받아와서 버튼 만드러줌
//    	//ArrayList<BigCategory> aList
//    	int tempNum=3;
//    	
//    	ArrayList<Button> btnList= new ArrayList<Button>();
//    	
//    	for(int i=0; i<tempNum;i++)
//    	{
//    		btnList.add(new Button());     	    		
//    	}
//    	
//    	hbox1.getChildren().addAll(btnList);
//    	btnList.get(0).setText("카테고리1");   	
//    	btnList.get(1).setText("카테고리2");
//    	btnList.get(2).setText("카테고리3");
//    	hbox1.setMargin(btnList.get(0), new Insets(10));  
//    	hbox1.setMargin(btnList.get(1), new Insets(20));
//    	//btnList.get(0).setOnMouseDragOver(event -> {System.out.println("테스트");});
//    	//Hover 이벤트 주는거
//    	    
//    	btnList.get(0).addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
//    	      @Override
//    	      public void handle(MouseEvent event) {
//    	        System.out.println("카테고리1 Hover");
//    	      }
//    	    });

    		
//    	 Pane root = new Pane();
//    	 root.setPrefSize(400, 300);    
//    	 VBox menu = new VBox();
//    	 
//    	    menu.setId("menu");
//    	    menu.prefHeightProperty().bind(root.heightProperty());
//    	    menu.setPrefWidth(200);
//
//    	    menu.getChildren().addAll(new Button("Something"), new Button("Something else"), new Button("Something different"));
//
//    	   // menu.getStylesheets().add(getClass().getResource("/application/menustyle.css").toExternalForm());
//    	    menu.setTranslateX(-190);
//    	    TranslateTransition menuTranslation = new TranslateTransition(Duration.millis(500), menu);
//
//    	    menuTranslation.setFromX(-190);
//    	    menuTranslation.setToX(0);
//
//    	    menu.setOnMouseEntered(evt -> {
//    	        menuTranslation.setRate(1);
//    	        menuTranslation.play();
//    	    });
//    	    menu.setOnMouseExited(evt -> {
//    	        menuTranslation.setRate(-1);
//    	        menuTranslation.play();
//    	    });
//    	    
//    	    root.getChildren().addAll(menu);
//    	    Scene scene = new Scene(root);    	    
//    	    
//    	    stage.setScene(scene);
//    	    stage.show();    	
    	searchBtn.setOnAction(event->{
    		IOHandler.getInstance().showAlert("검색버튼 클릭");
        	
        	String s = searchField.getText();
        	System.out.println("입력값 :" +s);
        	moveToSearchPage();
    	});
	}
    

    //검색 화면으로 이동하는 메소드
    private void moveToSearchPage() {
       try {
    	    String searchWord = searchField.getText();
    	    
    	    // 검색어 없으면 검색 안함
    	    int wordLength = searchWord.length();
    	    if(searchWord == null || wordLength < 1) {
    	    	return;
    	    }
    	    else if(wordLength < 2) {
    	    	// 한 글자만 검색하면 검색결과 너무 많음. 2글자 이상 검색해라.
    	    	IOHandler.getInstance().showAlert("검색어는 2자리 이상 입력해주십시오.");
    	    	return;
    	    }
    	    
            //검색페이지로 이동하기
            Stage primaryStage = (Stage) searchBtn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/page/SearchPage.fxml"));
            Parent root =  loader.load();        
            Scene scene = new Scene(root);
            
            SearchPageController sController = loader.getController();
            boolean canIMove = sController.transferProduct(searchWord);
            
           if(canIMove) {
        	   primaryStage.setScene(scene);
               primaryStage.setTitle("또나와 검색결과");
               primaryStage.show();
           }

        } catch (Exception e) {
        	String errorMsg = "MainPageController.moveToSearchPage\n"+ e.getMessage();
        	IOHandler.getInstance().showAlert(errorMsg);
        	IOHandler.getInstance().log(errorMsg);
        }
    }

	
}

