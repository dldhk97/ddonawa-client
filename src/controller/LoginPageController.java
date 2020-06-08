package controller;

import java.util.ArrayList;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Account;
import model.BigCategory;
import model.Category;
import network.NetworkManager;
import network.Protocol;
import network.ProtocolType;
import network.Response;
import network.ResponseType;
import task.BigCategoryTask;
import task.CategoryTask;
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
        	
        	Account account = new Account(idField.getText(), pwField.getText());
        	
        	// 대충 서버 연결해서 아이디 비번 체크하고 결과받음
        	Protocol received = NetworkManager.getInstance().connect(ProtocolType.LOGIN, (Object)account);		// 대강 이런식으로...
        	Response response = received.getResponse();
        	ResponseType type = response.getResponseType();
        	
        	switch(type) {
        	case SUCCEED:
        		IOHandler.getInstance().showAlert("로그인에 성공했습니다");
        		 moveToMain();
        		break;
        	case FAILED:
        		IOHandler.getInstance().showAlert(response.getMessage());
        		idField.requestFocus();
        		break;
        	default:
        		IOHandler.getInstance().showAlert(response.getMessage());
        		break;
        	}
        	           
        	//사용자 정보 저장해야할 듯
        	
        	
        	//moveToMain();
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
            //Scene scene = new Scene(root);    	    
           	
            //Pane root1 = new Pane();            
            HBox root1 = new HBox();
       	 	root1.setPrefSize(600, 400);         	 	
       	 	VBox menu = new VBox();       	 	 	 	
       	 	root1.setAlignment(Pos.CENTER);
       	    menu.setId("menu");       	    
       	    menu.setPrefWidth(100);

       	   // 나중에 동적으로 추가해줘야 할듯       	   
       	    BigCategoryTask a= new BigCategoryTask();
       	    ArrayList<BigCategory> bigCategoryList= a.getAllBigCategory();
//       	    ArrayList<BigCategory> bigCategoryList= new ArrayList<BigCategory>();
//       	    bigCategoryList.add(new BigCategory("1", "의류"));
//       	    bigCategoryList.add(new BigCategory("2", "식품"));
//       	    bigCategoryList.add(new BigCategory("3", "건강"));       	    
       	    
       	    
       	    CategoryTask b = new CategoryTask();
//      	    ArrayList<Category> categoryList = b.findByBigCategory(bigCategory);
//      	    ArrayList<Category> categoryList = new ArrayList<Category>();
//      	    categoryList.add(new Category("1", "남성상의", "1"));
//      	    categoryList.add(new Category("2", "여상상의", "1"));
//      	    categoryList.add(new Category("3", "남성하의", "1"));
       	    MenuButton menuBtn = new MenuButton();
       	    menuBtn.setId("menuBtn");
       	   
    	    
       	    for(int i=0;i<bigCategoryList.size();i++)
       	    {
       	    	menuBtn = new MenuButton(bigCategoryList.get(i).getName());           	    	
       	    	ArrayList<Category> categoryList = b.findByBigCategory(bigCategoryList.get(i));
       	    	for(int j=0;j<categoryList.size();j++)
       	    	{
       	    		MenuItem mitem = new MenuItem(categoryList.get(j).getName());
       	    		mitem.setOnAction( evt -> {
       	    			//카테고리 클릭했을 때 액션       	    			
       	    			IOHandler.getInstance().showAlert(mitem.getText());
       	    		});
       	    		menuBtn.getItems().addAll(mitem);       	    	
       	    	}
       	    	menu.getChildren().add(menuBtn);
       	    }
       	   
       	   
       	    
       	    
//       	    MenuItem menuItem1 = new MenuItem("A-1");
//       	    MenuItem menuItem2 = new MenuItem("A-2");
//       	 	MenuItem menuItem3 = new MenuItem("A-3");
//       	 	MenuButton menuBtn1= new MenuButton("카테고리 A", null,menuItem1,menuItem2,menuItem3);       	 
//       	 	
//       	 	MenuItem menuItem4 = new MenuItem("B-1");
//    	    MenuItem menuItem5 = new MenuItem("B-2");
//    	 	MenuItem menuItem6 = new MenuItem("B-3");
//    	 	MenuButton menuBtn2= new MenuButton("카테고리 B", null,menuItem4,menuItem5,menuItem6);
//       	 	menu.getChildren().addAll(menuBtn1,menuBtn2);

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
       	    
       	    root1.getChildren().addAll(menu,root);
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
