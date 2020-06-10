package controller;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.binding.ObjectExpression;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import model.CollectedInfo;
import model.Favorite;
import model.Product;
import network.EventType;
import network.NetworkManager;
import network.Protocol;
import network.ProtocolType;
import network.Response;
import network.ResponseType;
import utility.IOHandler;
import utility.UserAccount;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
	    private LineChart<String, Double> lineChart;

	    @FXML
	    private CategoryAxis categoryAxisBottom;

	    @FXML
	    private NumberAxis numberAxisLeft;
	    
    Popup popup;
    TextArea textArea;
    
    ArrayList<CollectedInfo> collectedInfoList = null;
    XYChart.Series<String, Double> series;
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	popup = new Popup();
    	textArea = new TextArea("팝업");
    	
	}
    
    @FXML
    void OnZzimRegisterBtnClicked(ActionEvent event) {
    	String uId = UserAccount.getInstance().getAccount().getId();
    	String productName = pName.getText();
    	Double target = Double.parseDouble(targetPrice.getText());
    	Favorite favorite = new Favorite(uId, productName, target);
    	
    	if(target>= Double.parseDouble(pPrice.getText()))
    	{
    		IOHandler.getInstance().showAlert("목표 가격을 다시 입력해주세요.");
    		targetPrice.requestFocus();
    	}
    			
    	else
    	{
	    	try {
	    		Protocol received = NetworkManager.getInstance().connect(ProtocolType.EVENT, EventType.ADD_FAVORITE, (Object)favorite);
	        	Response response = received.getResponse();
	        	ResponseType type = response.getResponseType();       	
	        	
	        	
	        	// 응답 결과에 따라 알아서 처리하셈.
	        	switch(type) {
	            	case SUCCEED:    
	            		
	            	case FAILED:
	            		
	            	case SERVER_NOT_RESPONSE:
	            	
	            	case ERROR:
	            		
	        		default:
	        			IOHandler.getInstance().showAlert(response.getMessage());
	        			break;
	        	}
	    	}catch(Exception e)
	    	{
	    		e.printStackTrace();
	    	}
    	}
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
					if(recentInfo.getUrl() == null) {
						IOHandler.getInstance().showAlert("해당 상품은 하이퍼링크가 존재하지 않습니다!");
						return;
					}
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
    	    lineChart.setCursor(Cursor.CROSSHAIR);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
		}
	}
    
    private static ObservableList<XYChart.Data<String, Double>> createData(ArrayList<CollectedInfo> collectedInfoList) {
    	ObservableList<XYChart.Data<String, Double>> list = FXCollections.observableArrayList();
    	
    	// 역순으로(날짜순으로)
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

//        pane.translateYProperty().bind(pane.heightProperty().divide(1.5));
        label.translateYProperty().bind(label.heightProperty().divide(1.5).subtract(-10));

        return pane;
    }
    
}
