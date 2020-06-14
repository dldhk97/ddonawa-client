package controller;

import java.util.ArrayList;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.BigCategory;
import model.Category;
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

// 사이드바가 있는 컨트롤러는 Extends 해서 쓰세용
public class SidebarController {
	
	public void addSideBar(Parent parent) {
    	try {
    		
            HBox sideBarRoot = new HBox();
       	 	sideBarRoot.setPrefSize(600, 400);
       	 	VBox sideBarBox = new VBox();       	 	 	 	
       	 	sideBarRoot.setAlignment(Pos.CENTER);
       	    sideBarBox.setId("menu");       	    
       	    sideBarBox.setPrefWidth(100);
       	    
       	    ArrayList<MenuButton> menuButtons = getMenuButtonList();
    	    for(MenuButton mb : menuButtons) {
    	    	sideBarBox.getChildren().add(mb);
    	    }
    	    
    	    sideBarBox.getStylesheets().add(getClass().getResource("/application/menustyle.css").toExternalForm());
       	   // sideBarBox.setTranslateX(-90);
       	    TranslateTransition menuTranslation = new TranslateTransition(Duration.millis(500), sideBarBox);       	    
//       	    menuTranslation.setFromX(-90);
//       	    menuTranslation.setToX(0);
       	    
       	    sideBarBox.setOnMouseEntered(evt -> {
       	        menuTranslation.setRate(1);
       	        menuTranslation.play();
       	    });
       	    sideBarBox.setOnMouseExited(evt -> {
       	        menuTranslation.setRate(-1);
       	        menuTranslation.play();
       	    });
       	           	    
       	    sideBarRoot.widthProperty().addListener(evt->{       	    	
       	    	sideBarBox.setTranslateX(-90-sideBarBox.getLayoutX());
       	    	menuTranslation.setFromX(-90-sideBarBox.getLayoutX());
        	    menuTranslation.setToX(-sideBarBox.getLayoutX());
       	    	
       	    	
       	    });
       	    
       	 
       	    
       	    
       	    sideBarRoot.getChildren().addAll(sideBarBox,parent);
       	    Scene scene = new Scene(sideBarRoot);
       	    Stage stage = new Stage();
       	
       	    stage.setScene(scene);
       	    stage.setTitle("또나와 메인화면");
       	    stage.show();
    	}
    	catch (Exception e) {
    		e.printStackTrace();
		}
    	
    }
	
	// 메뉴버튼 리스트를 가져옴. 안에는 메뉴아이템도 다 들어 있음.
	private ArrayList<MenuButton> getMenuButtonList(){
		
		ArrayList<MenuButton> result = new ArrayList<MenuButton>();
		
		try {
			ArrayList<BigCategory> bigCategoryList = getBigCategoryList();
	    	
	    	if(bigCategoryList == null || bigCategoryList.size() < 1) {
	    		IOHandler.getInstance().log("빅 카테고리 비어있거나 null임");
	    		return null;
	    	}
			
			for(BigCategory bc : bigCategoryList) {
				String bigCategoryName = bc.getName();
				MenuButton menuButton = new MenuButton(bigCategoryName);
				
				ArrayList<Category> categoryList = getCategoryList(bc);
				
				for(Category c : categoryList) {
					String categoryName = c.getName();
					MenuItem menuItem = new MenuItem(categoryName);
					menuItem.setOnAction(event->{
						// 카테고리 클릭하면 그걸로 검색해서 서버에서 결과 받아옴.
						ArrayList<Tuple<Product, CollectedInfo>> received = doSearch(c);
//						moveToSearchPage(received);
						searchEventListener.onEvent(received);
					});
					
					menuButton.getItems().add(menuItem);
				}
				result.add(menuButton);
			}
			return result;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
    	
		
		return null;
		
	}
	
	// 빅 카테고리 가진거 다 내놓으라고 함
	private ArrayList<BigCategory> getBigCategoryList() throws Exception{
		Protocol received = NetworkManager.getInstance().connect(ProtocolType.EVENT, EventType.GET_BIG_CATEGORY, null);
		Response response = received.getResponse();
    	ResponseType type = response.getResponseType();
    	
    	ArrayList<BigCategory> bigCategoryList = null;
    	switch (type) {
			case SUCCEED:
				bigCategoryList = (ArrayList<BigCategory>) received.getObject();
				break;
			case FAILED:
				IOHandler.getInstance().log("빅 카테고리 받아오는데 실패함");
				break;
			default:
				IOHandler.getInstance().log("빅 카테고리 받아오다 에러 발생");
				break;
		}
    	return bigCategoryList;
	}
	
	// 빅 카테고리 서버한테 넘겨주고 그 하위 카테고리 리스트 가져옴
	private ArrayList<Category> getCategoryList(BigCategory bigCategory) throws Exception{
		Protocol received = NetworkManager.getInstance().connect(ProtocolType.EVENT, EventType.GET_CATEGORY, bigCategory);
		Response response = received.getResponse();
    	ResponseType type = response.getResponseType();
    	
    	ArrayList<Category> categoryList = null;
    	switch (type) {
			case SUCCEED:
				categoryList = (ArrayList<Category>) received.getObject();
				break;
			case FAILED:
				IOHandler.getInstance().log("카테고리 받아오는데 실패함");
				break;
			default:
				IOHandler.getInstance().log("카테고리 받아오다 에러 발생");
				break;
		}
    	return categoryList;
	}
	
	// 서버 연결해서 상품명으로 검색하고, 결과 받아온다.
    private ArrayList<Tuple<Product, CollectedInfo>> doSearch(final Category category) {
    	try {
    		// 카테고리 없으면 검색 안함
    	    if(category == null) {
    	    	return null;
    	    }
    		
    		// 검색 시 서버로부터 상품정보 - 최신수집정보가 쌍(Tuple)으로 이루어진 결과 배열을 받음.
    		Protocol received = NetworkManager.getInstance().connect(ProtocolType.EVENT, EventType.SEARCH_BY_CATEGORY, (Object)category);
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
    
  //검색 화면으로 이동하는 메소드
    private void moveToSearchPage(final ArrayList<Tuple<Product, CollectedInfo>> received) {
       try {
            //검색페이지로 이동하기
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/page/SearchPage.fxml"));
            Parent root =  loader.load();        
            Scene scene = new Scene(root);
            
            SearchPageController sController = loader.getController();
            boolean canIMove = sController.setTableView(received);
            
           if(canIMove) {
        	   primaryStage.setScene(scene);
               primaryStage.setTitle("또나와 검색결과");
               primaryStage.show();
           }

        } catch (Exception e) {
        	String errorMsg = "SidebarController.moveToSearchPage\n"+ e.getMessage();
        	IOHandler.getInstance().showAlert(errorMsg);
        	IOHandler.getInstance().log(errorMsg);
        }
    }
    
    private ISearchEventListener searchEventListener;
    
    // 데이터 전달을 위한 리스너 설정
    public void setOnEventListener(ISearchEventListener listener){
        searchEventListener = listener;
    }

    // 데이터 전달을 위한 인터페이스
    public interface ISearchEventListener {
        void onEvent(ArrayList<Tuple<Product, CollectedInfo>> received);
    }
	
}
