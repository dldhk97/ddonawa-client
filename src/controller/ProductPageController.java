package controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import db.CollectedInfoManager;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import model.BigCategory;
import model.Category;
import model.CollectedInfo;
import model.Product;
import task.BigCategoryTask;
import task.CategoryTask;
import utility.IOHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;


public class ProductPageController implements Initializable {	
		@FXML
	    private ImageView Image;
		
	 	@FXML
	 	private TextField pName;

	    @FXML
	    private TextField pPrice;

	    @FXML
	    private TextField pMinPrice;

	    @FXML
	    private Button zzimBtn;

	    @FXML
	    private HBox zzimBar;

	    @FXML
	    private TextField targetPrice;

	    @FXML
	    private Button zzimRegister;
	    
	    @FXML
	    private Button goToMainBtn;

	    @FXML
	    void OnGoToMainBtnClicked(ActionEvent event) {
	    	 try {
	             //메인페이지로 이동하기
	             Stage primaryStage = (Stage) zzimBtn.getScene().getWindow();
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
//	        	    ArrayList<BigCategory> bigCategoryList= new ArrayList<BigCategory>();
//	        	    bigCategoryList.add(new BigCategory("1", "의류"));
//	        	    bigCategoryList.add(new BigCategory("2", "식품"));
//	        	    bigCategoryList.add(new BigCategory("3", "건강"));       	    
	        	    
	        	    
	        	    CategoryTask b = new CategoryTask();
//	       	    ArrayList<Category> categoryList = b.findByBigCategory(bigCategory);
//	       	    ArrayList<Category> categoryList = new ArrayList<Category>();
//	       	    categoryList.add(new Category("1", "남성상의", "1"));
//	       	    categoryList.add(new Category("2", "여상상의", "1"));
//	       	    categoryList.add(new Category("3", "남성하의", "1"));
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
	        	   
	        	   
	        	    
	        	    
//	        	    MenuItem menuItem1 = new MenuItem("A-1");
//	        	    MenuItem menuItem2 = new MenuItem("A-2");
//	        	 	MenuItem menuItem3 = new MenuItem("A-3");
//	        	 	MenuButton menuBtn1= new MenuButton("카테고리 A", null,menuItem1,menuItem2,menuItem3);       	 
//	        	 	
//	        	 	MenuItem menuItem4 = new MenuItem("B-1");
//	     	    MenuItem menuItem5 = new MenuItem("B-2");
//	     	 	MenuItem menuItem6 = new MenuItem("B-3");
//	     	 	MenuButton menuBtn2= new MenuButton("카테고리 B", null,menuItem4,menuItem5,menuItem6);
//	        	 	menu.getChildren().addAll(menuBtn1,menuBtn2);

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
    Popup popup;
    TextArea textArea;
    
    //일단 이미지 클릭시 링크로 기본웹브라우저 통해서 접근
    //URI 오류는 보통 링크를 틀렸을때 많이 나옴 , http:// 붙였는지 등등 확인 필요
    @FXML
    void OnProductImageClicked(MouseEvent event)  {
    

    }    
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	popup = new Popup();
    	textArea = new TextArea("팝업");
    	
    	// 맨 처음 초기화 하면서 차트, 이름, 가격, URL, 썸네일 등 초기화 해서 화면 보여줘야함 
    	
		// 이미지 설정하는 부분
//		String s = "http://img.danawa.com/prod_img/500000/562/196/img/5196562_1.jpg?";
//		Image img = new Image(s);
//		Image.setImage(img); 	    
	    
	}
    
    @FXML
    void OnZzimRegisterBtnClicked(ActionEvent event) {
    	
    }

    
    @FXML
    void OnZzimBtnClicked(ActionEvent event) {
//    	if(popup.isShowing()) {
//    		popup.hide();
//    	}
//    	else {
//    		final Window window = zzimBtn.getScene().getWindow();
//    		popup.setWidth(100);
//    		popup.setHeight(300);
//    		
//    		final double x = window.getX()+zzimBtn.localToScene(0,0).getX() + zzimBtn.getScene().getX();
//    		final double y = window.getY()+zzimBtn.localToScene(0,0).getY() + zzimBtn.getScene().getHeight();
//    		
//    		popup.getContent().clear();
//    		popup.getContent().addAll(textArea);
//    		popup.show(window);
//    	}
    	zzimBar.setVisible(true);
    }
    public void DataTransfer(String s)
    {
    	try {
    	CollectedInfoManager cManager = new CollectedInfoManager();
    	ArrayList<CollectedInfo> p = cManager.findByProductName(s);
    	if(p.size()<1)
    	{
    		System.out.println("선택한 상품을 검색에서 못받아옴");
    	}    	
    	System.out.println("테스트1");
    	Double min = Double.MAX_VALUE;
    	for(int i=0;i<p.size();i++)
    	{
    		System.out.println(p.get(i));
    		if(p.get(i).getPrice() < min)
    		{
    			min = p.get(i).getPrice();
    		}
    	}
    	String thum = p.get(0).getThumbnail();
    	if(thum!=null)
    	{
    		if(thum.length()>0)
    		{
    			Image img = new Image(p.get(0).getThumbnail());
    			Image.setImage(img); 	
    		}
    	}
    	
    	
//    	if(p.get(0).getThumbnail().length()>0)
//    	{
//			Image img = new Image(p.get(0).getThumbnail());
//			Image.setImage(img); 	 
//    	}
		pName.setText(p.get(0).getProductName());
		Double price =p.get(0).getPrice();
		pPrice.setText(price.toString());
		pMinPrice.setText(min.toString());
		
		Image.setOnMouseClicked(event->{
			try {
				Desktop.getDesktop().browse(new URI(p.get(0).getUrl()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		});
    	}catch(Exception e) {
    		IOHandler.getInstance().log("ProductPageController.DataTransfer : " +e);
    	}    	
    	
    }
    
}
