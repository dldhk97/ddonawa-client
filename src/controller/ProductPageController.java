package controller;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.TranslateTransition;
import javafx.beans.binding.ObjectExpression;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.CollectedInfo;
import model.Product;
import network.EventType;
import network.NetworkManager;
import network.Protocol;
import network.ProtocolType;
import network.Response;
import network.ResponseType;
import utility.IOHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;


public class ProductPageController extends SidebarController implements Initializable {	
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
	    private LineChart<String, Double> lineChart;

	    @FXML
	    private CategoryAxis categoryAxisBottom;

	    @FXML
	    private NumberAxis numberAxisLeft;

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

	        	    // 대분류 버튼과 그 예하의 아이템들 추가
	           	    ArrayList<MenuButton> menuButtons = getMenuButtonList();
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
	    
    Popup popup;
    TextArea textArea;
    
    ArrayList<CollectedInfo> collectedInfoList = null;
    XYChart.Series<String, Double> series;
    
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
	    
//    	setupChart();
//    	setupToolTip();
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
    public void DataTransfer(Product p)
    {
    	try {
    		// 사용자가 선택한 상품정보를 서버에게 넘겨주고, 수집정보를 가져온다.
    		Protocol received = NetworkManager.getInstance().connect(ProtocolType.EVENT, EventType.GET_PRODUCT_DETAIL, (Object)p);
        	Response response = received.getResponse();
        	ResponseType type = response.getResponseType();
        	
        	switch(type) {
        	case SUCCEED:
        		collectedInfoList = (ArrayList<CollectedInfo>) received.getObject();
        		break;
        	case FAILED:
        		break;
    		default:
    			break;
        	}
        	
	    	if(collectedInfoList == null || collectedInfoList.size() < 1)
	    	{
	    		System.out.println("선택한 상품을 검색에서 못받아옴");
	    	}    	
	    	
	    	// 썸네일 구하기
	    	CollectedInfo recentInfo = collectedInfoList.get(0);
	    	String thumb = recentInfo.getThumbnail();
	    	if(thumb != null && thumb.length() > 0)
	    	{
	    		Image img = new Image(recentInfo.getThumbnail());
    			Image.setImage(img);
	    	}
	    	
	    	// 표시할 정보 명시 및 최저가 구하기
	    	String productName = p.getName();
	    	Double productPrice = recentInfo.getPrice();
	    	Double min = getMinPrice(collectedInfoList);
	    	
			pName.setText(productName);
			pPrice.setText(productPrice.toString());
			pMinPrice.setText(min.toString());
			
			Image.setOnMouseClicked(event->{
				try {
					Desktop.getDesktop().browse(new URI(recentInfo.getUrl()));
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				} 
			});
			
			setupChart();
    	}
    	catch(Exception e) {
    		IOHandler.getInstance().log("ProductPageController.DataTransfer : " +e);
    	}    	
    	
    }
    
    public Double getMinPrice(ArrayList<CollectedInfo> list) {
    	double min = Double.MAX_VALUE;
    	for(CollectedInfo c : list) {
    		if(c.getPrice() < min) {
    			min = c.getPrice();
    		}
    	}
    	return min;
    }
    
    private void setupChart() {
    	if(collectedInfoList == null) {
    		return;
    	}
    	
    	try {
    		lineChart.getData().add(new Series<>(createData(collectedInfoList)));
    	    
//    	    
//    	    list.add(series);
//    	    
//    	    lineChart.setData(list);
    	    lineChart.setCursor(Cursor.CROSSHAIR);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
		}
	}
    
    private static ObservableList<XYChart.Data<String, Double>> createData(ArrayList<CollectedInfo> collectedInfoList) {
    	ObservableList<XYChart.Data<String, Double>> list = FXCollections.observableArrayList();
    	
    	int size = collectedInfoList.size();
    	for(int i = size - 1 ; i >= 0 ; i--) {
    		CollectedInfo ci = collectedInfoList.get(i);
        	XYChart.Data<String, Double> data = new XYChart.Data<>(ci.getCollectedDate().toString(), ci.getPrice());
            data.setNode(createDataNode(data.YValueProperty()));
            list.add(data);
        }
        return list;
    }
    
    private static Node createDataNode(ObjectExpression<Double> value) {
        Label label = new Label();
        label.textProperty().bind(value.asString("%,.2f"));

        Pane pane = new Pane(label);
        pane.setShape(new Circle(6.0));
        pane.setScaleShape(false);

        label.translateYProperty().bind(label.heightProperty().divide(500.0));

        return pane;
    }
    
}
