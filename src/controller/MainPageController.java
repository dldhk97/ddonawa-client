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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.CollectedInfo;
import model.Product;
import model.Tuple;
import network.EventType;
import network.NetworkManager;
import network.Protocol;
import network.ProtocolType;
import network.Response;
import network.ResponseType;
import utility.IOHandler;

public class MainPageController implements Initializable {
	
    @FXML
    private Button searchBtn;
    
    @FXML
    private TextField searchField;
    
    // 키보드
    @FXML
    void OnSearchPressed(KeyEvent event) {
    	if(event.getCode()==KeyCode.ENTER)
    	{
    		onSearch();
    	}
    }
    
    // 버튼
    @FXML
    void OnClikedSearchBtn(ActionEvent event) {   	
//    	onSearch();
    }

    @FXML
    void On_testBtn1_Clicked(ActionEvent event) {

    }

    @FXML
    void On_testBtn2_Clicked(ActionEvent event) {

    }
    
    // 버튼
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	Parent parent = searchBtn.getParent().getParent();
    	SidebarController sc = new SidebarController();
    	sc.addSideBar(parent);
    	searchBtn.setOnAction(event->{
    		onSearch();
    	});
    	searchField.requestFocus();
	}
    
    private void onSearch() {
    	String searchWord = searchField.getText();
    	ArrayList<Tuple<Product, CollectedInfo>> received = doSearch(searchWord);
    	if(received != null) {
    		moveToSearchPage(received);
    	}
    }
    
    public void closeStage() {
    	Stage primaryStage = (Stage) searchField.getScene().getWindow();
    	primaryStage.close();
    }
    
    // --------------------------- 로직  ------------------------------------------ //

    //검색 화면으로 이동하는 메소드
    private void moveToSearchPage(final ArrayList<Tuple<Product, CollectedInfo>> received) {
       try {
            //검색페이지로 이동하기
            Stage primaryStage = (Stage) searchBtn.getScene().getWindow();
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

	// 서버 연결해서 상품명으로 검색하고, 결과 받아온다.
    private ArrayList<Tuple<Product, CollectedInfo>> doSearch(final String searchWord) {
    	try {
    		// 검색어 없으면 검색 안함
    	    int wordLength = searchWord.length();
    	    if(searchWord == null || wordLength < 1) {
    	    	return null;
    	    }
    	    else if(wordLength < 2) {
    	    	// 한 글자만 검색하면 검색결과 너무 많음. 2글자 이상 검색해라.
    	    	IOHandler.getInstance().showAlert("검색어는 2자리 이상 입력해주십시오.");
    	    	return null;
    	    }
    		
    		// 검색 시 서버로부터 상품정보 - 최신수집정보가 쌍(Tuple)으로 이루어진 결과 배열을 받음.
    		Protocol received = NetworkManager.getInstance().connect(ProtocolType.EVENT, EventType.SEARCH, (Object)searchWord);
        	Response response = received.getResponse();
        	ResponseType type = response.getResponseType();
        	
        	ArrayList<Tuple<Product, CollectedInfo>> receievedList = null;
        	
        	// 응답 결과에 따라 알아서 처리하셈.
        	switch(type) {
            	case SUCCEED:
            		receievedList = (ArrayList<Tuple<Product, CollectedInfo>>) received.getObject();
            		break;
            	case FAILED:
            		break;
            	case SERVER_NOT_RESPONSE:
            		break;
            	case ERROR:
            		break;
        		default:
        			break;
        	}
        	
        	// 서버에서 받아온 값이 있을 때만 처리
        	if(receievedList == null || receievedList.size() < 1) {
        		IOHandler.getInstance().showAlert("검색 결과가 없습니다.");
        		return null;
        	}
        	return receievedList;
    	}
    	catch (Exception e) {
    		e.printStackTrace();
		}
    	return null;
    }
    
 // 데이터 수신을 위한 리스너
    private class SearchEventListener implements SidebarController.ISearchEventListener {
    	MainPageController pageController;
    	
    	public SearchEventListener(MainPageController pageController) {
    		this.pageController = pageController; 
    	}

		@Override
		public void onEvent(ArrayList<Tuple<Product, CollectedInfo>> received) {
			pageController.moveToSearchPage(received);
			closeStage();
		}

        
    }

	
}

