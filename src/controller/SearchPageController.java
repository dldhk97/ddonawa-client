package controller;

import java.util.ArrayList;

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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import utility.IOHandler;

public class SearchPageController {

    @FXML
    private Button SearchBtn;

    @FXML
    private Button testBtn;
    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, String> ImageColumn;

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
             Stage primaryStage = (Stage) SearchBtn.getScene().getWindow();
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

    @FXML
    void OnSearchBtnClicked(ActionEvent event) {
    	IOHandler.getInstance().showAlert("테스트");
    }       
    
    //상품이 클릭되었을 때
    @FXML
    void OnProductClicked(MouseEvent event) {
    	if(event.getClickCount()>1)
    	{
    		System.out.println(table.getSelectionModel().getSelectedItem().getName());    		
    		
    		moveToProductPage();
    	}
    }

    //임의 항목 추가 테스트 버튼
    @FXML
    void test(ActionEvent event) {
    	IOHandler.getInstance().showAlert("테스트");
    	
    		
    	 ObservableList<Data> myList = FXCollections.observableArrayList();
    	 
    	
    	 myList.add(new Data(new SimpleStringProperty("사진"),new SimpleStringProperty("고기"),new SimpleStringProperty("1000")));
    	 myList.add(new Data(new SimpleStringProperty("사진"),new SimpleStringProperty("나물"),new SimpleStringProperty("2000")));
    	 myList.add(new Data(new SimpleStringProperty("사진"),new SimpleStringProperty("생선"),new SimpleStringProperty("3000")));
    	 
    	 ImageColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
         ProductNameColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
         PriceColumn.setCellValueFactory(cellData -> cellData.getValue().genderProperty());    	
      
    	table.setItems(myList);
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
    
}


// 임의로 쓰는 데이터 객체, 테이블뷰에 쓰려면 StringProperty나 IntProperty 형식으로 써야함
class Data{
    private StringProperty name;
    private StringProperty address;
    private StringProperty gender;
 
    public Data(StringProperty name, StringProperty address, StringProperty gender) {
        this.name = name;
        this.address = address;
        this.gender = gender;       
    }
 
    public StringProperty nameProperty() {
        return name;
    }
    public StringProperty addressProperty() {
        return address;
    }
    public StringProperty genderProperty() {
        return gender;
    }
    public StringProperty getName()
    {
		return name;
    	
    }
 
}

