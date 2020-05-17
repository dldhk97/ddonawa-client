package controller;

import java.util.ArrayList;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.stage.Stage;
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
    	
    		
    	 ObservableList<Data> myList = FXCollections.observableArrayList(new Data(new SimpleStringProperty("가"),new SimpleStringProperty("나"),new SimpleStringProperty("다")));
    	 
    	 for(int i=0;i<30;i++)
    	 {
    		 myList.add(new Data(new SimpleStringProperty("가"),new SimpleStringProperty("나"),new SimpleStringProperty("다")));
    	 }
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

