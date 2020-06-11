package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;



import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Account;
import model.CollectedInfo;
import model.Favorite;
import model.Product;
import model.Tuple;
import network.EventType;
import network.NetworkManager;
import network.Protocol;
import network.ProtocolType;
import network.Response;
import network.ResponseType;
import utility.IOHandler;
import utility.UserAccount;

public class zzimPageControll {
    @FXML
    private TableView<Data> zListTable;
    
    @FXML
    private TableColumn<Data, String> pNameColumn;
    
    @FXML
    private TableColumn<Data, String> targetColumn;

    @FXML
    private Button zzimDeleteBtn;
    
    @FXML
    private Button backBtn;
    
    @FXML
    void productClick(MouseEvent event) {    			  	
    		if(event.getClickCount()>1)
    		{
    			Data a =zListTable.getSelectionModel().getSelectedItem();
    			if(a!=null)
    			{
    				String productName=a.getFavorite().getProductName();
    				Product target = new Product(productName, ""); 	
    	    		System.out.println(target.getName() + " clicked!");  
    	    		moveToProductPage(target);
    			}
    			
    		} 	       	   	
    }
    
    @FXML
    void deleteBtnClick(ActionEvent event) {
    	
    	try {
    		Data a =zListTable.getSelectionModel().getSelectedItem();
    		if(a!=null)
    		{
				Protocol received = NetworkManager.getInstance().connect(ProtocolType.EVENT, EventType.DELETE_FAVORITE, (Object)a.getFavorite());
		    	Response response = received.getResponse();
		    	ResponseType type = response.getResponseType();       	
		    	ArrayList<Favorite> receievedList = null;
		    	// 응답 결과에 따라 알아서 처리하셈.
		    	switch(type) {
		        	case SUCCEED:    
		        		IOHandler.getInstance().showAlert(response.getMessage());
		        		refresh();
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
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	
    }

    @FXML
    void backBtnClick(ActionEvent event) {
    	try {
   	    	// 메인페이지 엶.
            FXMLLoader.load(getClass().getResource("/page/MainPage.fxml"));
            
			// 기존 페이지 종료
   	    	Stage nowStage = (Stage) backBtn.getScene().getWindow();
   	    	nowStage.close();
	
	        } catch (Exception e) {
	        	String errorMsg = "LoginPageController.moveToMain\n" + e.getMessage();
	        	IOHandler.getInstance().showAlert(errorMsg);
	        	IOHandler.getInstance().log(errorMsg);
	        }    	 
    }	
    
    

 
    
    private void moveToProductPage(Product p)
    {
    	 try {           
             Stage primaryStage = new Stage();
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/page/ProductPage.fxml"));
             Parent root = loader.load();
             Scene scene = new Scene(root);
             
             ProductPageController pController = loader.getController();
             pController.DataTransfer(p);
             
             primaryStage.setScene(scene);
             primaryStage.setTitle(p.getName());
             primaryStage.show();

         } catch (Exception e) {
         	String errorMsg = "SearchPageController.moveToProductPager\n" + e.getMessage();
         	e.printStackTrace();
         	IOHandler.getInstance().showAlert(errorMsg);
         	IOHandler.getInstance().log(errorMsg);
         }
    }
  
 

	public void initialize(Stage stage) {	
		refresh();
		
//    	stage.setOnCloseRequest(evt->{    		
//    	});
	}
	
	   class Data{	
		   private final Favorite favorite;
		   
		   private final StringProperty name;
		   private final StringProperty target; 
		 
		    public Data(Favorite favorite) {
		    	this.favorite = favorite;		
		    	name= new SimpleStringProperty(favorite.getProductName());
		    	target = new SimpleStringProperty(String.valueOf(favorite.getTargetPrice()));
		    }
		    
		    public Favorite getFavorite() {
		    	return favorite;
		    }
		    
		    public StringProperty nameProperty() {
		        return name;
		    }   
		    public StringProperty targetProperty() {
				return target;
			}
		    
		    
	   }
	   
	   public void refresh()
	   {

			try {
				Account account = UserAccount.getInstance().getAccount();
				Protocol received = NetworkManager.getInstance().connect(ProtocolType.EVENT, EventType.GET_FAVORITE, (Object)account);
		    	Response response = received.getResponse();
		    	ResponseType type = response.getResponseType();       	
		    	ArrayList<Favorite> receievedList = null;
		    	// 응답 결과에 따라 알아서 처리하셈.
		    	switch(type) {
		        	case SUCCEED:    
		        		receievedList = (ArrayList<Favorite>)received.getObject();
		        		break;
		        	case FAILED:
		        		break;
		        	case SERVER_NOT_RESPONSE:
		        		break;
		        	case ERROR:
		        		break;
		    		default:
		    			IOHandler.getInstance().showAlert(response.getMessage());
		    			break;
		    	}
				ObservableList<Data> myList = FXCollections.observableArrayList();	    	
			
				if(receievedList!=null)
				{
			    	for(Favorite f : receievedList) {
			    		Data newData = new Data(f);
			    		
			    		myList.add(newData);
			    	}
				}
		    	     	
		    	pNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		        targetColumn.setCellValueFactory(cellData -> cellData.getValue().targetProperty());    	
		      
		    	zListTable.setItems(myList);
			}catch(Exception e) {
				e.printStackTrace();
			}
	    	
	   }
}


