package controller;

import java.util.ArrayList;

import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import model.BigCategory;
import model.Category;
import network.EventType;
import network.NetworkManager;
import network.Protocol;
import network.ProtocolType;
import network.Response;
import network.ResponseType;
import utility.IOHandler;

// 사이드바가 있는 컨트롤러는 Extends 해서 쓰세용
public class SidebarController {
	
	// 메뉴버튼 리스트를 가져옴. 안에는 메뉴아이템도 다 들어 있음.
	protected ArrayList<MenuButton> getMenuButtonList(){
		
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
						IOHandler.getInstance().showAlert(categoryName);
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
	
}
