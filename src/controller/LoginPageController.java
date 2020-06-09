package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Account;
import model.CollectedInfo;
import model.Product;
import model.Tuple;
import network.NetworkManager;
import network.Protocol;
import network.ProtocolType;
import network.Response;
import network.ResponseType;
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
       	    
       	    // 대분류 버튼과 그 예하의 아이템들 추가
       	    SidebarController sc = new SidebarController();
       	    sc.setOnEventListener(new SearchEventListener(this));
       	    ArrayList<MenuButton> menuButtons = sc.getMenuButtonList();
       	    for(MenuButton mb : menuButtons) {
       	    	menu.getChildren().add(mb);
       	    }

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

    
    ArrayList<Tuple<Product, CollectedInfo>> received;
    //검색 화면으로 이동하는 메소드
    private void moveToSearchPage(final ArrayList<Tuple<Product, CollectedInfo>> received) {
       try {
    	   
    	   if(received == null || received.size() < 1) {
    		   return;
    	   }
    	   
            //검색페이지로 이동하기
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/page/SearchPage.fxml"));
            Parent root =  loader.load();        
            Scene scene = new Scene(root);
            
            SearchPageController sController = loader.getController();
            boolean canIMove = sController.transferProduct(received);
            
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
    
   
    
    // 데이터 수신을 위한 리스너
    private class SearchEventListener implements SidebarController.ISearchEventListener {
    	LoginPageController lpc;
    	
    	public SearchEventListener(LoginPageController lpc) {
    		this.lpc = lpc; 
    	}

		@Override
		public void onEvent(ArrayList<Tuple<Product, CollectedInfo>> received) {
			lpc.moveToSearchPage(received);
		}

        
    }

	
    
   
}
