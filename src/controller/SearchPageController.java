package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import db.CollectedInfoManager;
import javafx.animation.TranslateTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.BigCategory;
import model.Category;
import model.CollectedInfo;
import model.Product;
import task.BigCategoryTask;
import task.CategoryTask;
import task.ProductTask;
import utility.IOHandler;

public class SearchPageController implements Initializable {

    @FXML
    private Button SearchBtn;
    @FXML
    private TextField searchField;
    @FXML
    private Button testBtn;
    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, ImageView> ImageColumn;

    @FXML
    private TableColumn<Data, String> ProductNameColumn;

    @FXML
    private TableColumn<Data, String> PriceColumn;
    
    @FXML
    private Button goToMainBtn;

    @FXML
    void OnGoToMainBtnClicked(ActionEvent event) {    	
    		 try {
    	            //메인페이지로 이동하기
    	            Stage primaryStage = (Stage) goToMainBtn.getScene().getWindow();
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
//    	       	    ArrayList<BigCategory> bigCategoryList= new ArrayList<BigCategory>();
//    	       	    bigCategoryList.add(new BigCategory("1", "의류"));
//    	       	    bigCategoryList.add(new BigCategory("2", "식품"));
//    	       	    bigCategoryList.add(new BigCategory("3", "건강"));       	    
    	       	    
    	       	    
    	       	    CategoryTask b = new CategoryTask();
//    	      	    ArrayList<Category> categoryList = b.findByBigCategory(bigCategory);
//    	      	    ArrayList<Category> categoryList = new ArrayList<Category>();
//    	      	    categoryList.add(new Category("1", "남성상의", "1"));
//    	      	    categoryList.add(new Category("2", "여상상의", "1"));
//    	      	    categoryList.add(new Category("3", "남성하의", "1"));
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
    	       	   
    	       	   
    	       	    
    	       	    
//    	       	    MenuItem menuItem1 = new MenuItem("A-1");
//    	       	    MenuItem menuItem2 = new MenuItem("A-2");
//    	       	 	MenuItem menuItem3 = new MenuItem("A-3");
//    	       	 	MenuButton menuBtn1= new MenuButton("카테고리 A", null,menuItem1,menuItem2,menuItem3);       	 
//    	       	 	
//    	       	 	MenuItem menuItem4 = new MenuItem("B-1");
//    	    	    MenuItem menuItem5 = new MenuItem("B-2");
//    	    	 	MenuItem menuItem6 = new MenuItem("B-3");
//    	    	 	MenuButton menuBtn2= new MenuButton("카테고리 B", null,menuItem4,menuItem5,menuItem6);
//    	       	 	menu.getChildren().addAll(menuBtn1,menuBtn2);

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

    @FXML
    void OnSearchBtnClicked(ActionEvent event) {
    	IOHandler.getInstance().showAlert("테스트");
    }       
    
    //상품이 클릭되었을 때
    @FXML
    void OnProductClicked(MouseEvent event) {
    	if(event.getClickCount()>1)
    	{
    		System.out.println(table.getSelectionModel().getSelectedItem().getName()+" clicked!");    		
    		
    		moveToProductPage();
    	}
    }

   // 임의 항목 추가 테스트 버튼
    @FXML
    void test(ActionEvent event) {
//    	IOHandler.getInstance().showAlert("테스트");
//    	
//    		
//    	 ObservableList<Data> myList = FXCollections.observableArrayList();
//    	 
//    	
//    	 myList.add(new Data(new SimpleStringProperty("사진"),new SimpleStringProperty("고기"),new SimpleStringProperty("1000")));
//    	 myList.add(new Data(new SimpleStringProperty("사진"),new SimpleStringProperty("나물"),new SimpleStringProperty("2000")));
//    	 myList.add(new Data(new SimpleStringProperty("사진"),new SimpleStringProperty("생선"),new SimpleStringProperty("3000")));
//    	 
//    	 ImageColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
//         ProductNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
//         PriceColumn.setCellValueFactory(cellData -> cellData.getValue().genderProperty());    	
//      
//    	table.setItems(myList);
    }
    
    private void moveToProductPage()
    {
    	 try {           
             Stage primaryStage = (Stage) table.getScene().getWindow();
             Parent root = FXMLLoader.load(getClass().getResource("/page/ProductPage.fxml"));
             Scene scene = new Scene(root);
             primaryStage.setScene(scene);
             primaryStage.show();

         } catch (Exception e) {
         	String errorMsg = "LoginPageController.moveToProductPager\n" + e.getMessage();
         	e.printStackTrace();
         	IOHandler.getInstance().showAlert(errorMsg);
         	IOHandler.getInstance().log(errorMsg);
         }
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
    
	public void transferProduct(String s)
    {		
    	searchField.setText(s);    	
    	try {
    		// TODO Auto-generated method stub
    		searchField.requestFocus();
    		ProductTask pTask = new ProductTask();
        	ArrayList<Product> productList = pTask.searchByProductName(s);
        	
        	//밑에 부분이 init 부분으로 가면 왜 안되는지 아직 파악못함
        	CollectedInfoManager cManager = new CollectedInfoManager();
        	ObservableList<Data> myList = FXCollections.observableArrayList();
        	
        	for(int i=0;i<productList.size();i++) {
        		String pName= productList.get(i).getName();
        		ArrayList<CollectedInfo> p = cManager.findByProductName(pName);
        		Double price =p.get(0).getPrice();
        		
        		 myList.add(new Data(new SimpleStringProperty("사진"),new SimpleStringProperty(productList.get(i).getName()),new SimpleStringProperty(price.toString())));
        		 
        	}     	
        	     	
             ProductNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
             PriceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());    	
          
        	table.setItems(myList);
    		}
    		catch(Exception e)
    		{
    			IOHandler.getInstance().log("상품목록 초기화"+ e);
    		}
    }
	
    
}


// 임의로 쓰는 데이터 객체, 테이블뷰에 쓰려면 StringProperty나 IntProperty 형식으로 써야함
class Data{
	private StringProperty image;
    private StringProperty name;
    private StringProperty price;
 
    public Data(StringProperty image, StringProperty name, StringProperty price) {
        this.name = name;
        this.image=image;
        this.price=price;     
    }
 
    public StringProperty nameProperty() {
        return name;
    }   
    public StringProperty priceProperty() {
        return price;
    }
    public StringProperty getName() {
		return name;
	}
   
 
}

