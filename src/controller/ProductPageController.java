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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import model.Account;
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
	    
    private Product currentProduct = null; 
    private Favorite currentFavorite = null;
    
    // 해당 상품의 수집정보 목록(최신순)
    ArrayList<CollectedInfo> collectedInfoList = null;
    XYChart.Series<String, Double> series;
    
    @FXML
    void TargetPressed(KeyEvent event) {
    	if(event.getCode()==KeyCode.ENTER)
    	{
    		zzimRegister();
    	}
    }
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	
	}
    
    @FXML
    void OnZzimRegisterBtnClicked(ActionEvent event) {
    	if(currentFavorite == null) {
    		zzimRegister();
    		setupFavoriteView();
    	}
    	else {
    		boolean isDeleteFavorite = IOHandler.getInstance().showDialog("이미 찜 정보가 등록되어 있습니다!", "찜 정보를 삭제한 후 새로 등록하시겠습니까?");
    		if(isDeleteFavorite) {
    			deleteFavorite();
    			setupFavoriteView();
    			
    		}
    	}
    	
    }
    
    private void deleteFavorite() {
    	// 사용자가 선택한 상품정보를 서버에게 넘겨주고, 수집정보를 가져온다.
    	try {
    		Protocol received = NetworkManager.getInstance().connect(ProtocolType.EVENT, EventType.DELETE_FAVORITE, (Object)currentFavorite);
	    	Response response = received.getResponse();
	    	ResponseType type = response.getResponseType();       	
	    	
	    	// 응답 결과에 따라 알아서 처리하셈.
	    	switch(type) {
	        	case SUCCEED:    
	        		IOHandler.getInstance().showAlert(response.getMessage());
	        		break;
	    		default:
	    			IOHandler.getInstance().showAlert("찜 삭제에 실패하였습니다!");
	    			break;
	    	}
    	}
    	catch (Exception e) {
    		e.printStackTrace();
		}
		
    }

    
    @FXML
    void OnZzimBtnClicked(ActionEvent event) {
    	zzimBar.setVisible(!zzimBar.isVisible());
    	
    }
    
    // 서버로부터 찜 정보 가져와서 현재 페이지이 상품에 해당되는 찜 정보 가져옴.
    private Favorite getFavoriteInfo(){
    	// 일단 해당 상품의 수집정보 목록이 있어야 함.
    	if(collectedInfoList == null) {
			return null;
		}
    	try {
    		// 사용자 정보 획득
    		Account userAccount = UserAccount.getInstance().getAccount();
        	
    		// 서버로 사용자 정보 보내서 찜 목록 가져옴
    		Protocol received = NetworkManager.getInstance().connect(ProtocolType.EVENT, EventType.GET_FAVORITE, (Object)userAccount);
        	Response response = received.getResponse();
        	ResponseType type = response.getResponseType();
        	
        	ArrayList<Favorite> favoriteList = null;
        	
        	switch(type) {
        	case SUCCEED:
        		favoriteList = (ArrayList<Favorite>) received.getObject();
        		break;
    		default:
    			IOHandler.getInstance().showAlert("찜 정보를 가져오는데 실패했습니다!");
    			break;
        	}
        	
        	if(favoriteList != null) {
        		// 서버로부터 받아온 찜목록 탐색
        		for(Favorite f : favoriteList) {
        			// 찜의 상품명과 현 페이지의 상품명이 같으면 찜 정보 반환
        			if(f.getProductName().equals(currentProduct.getName())) {
        				return f;
        			}
        		}
        	}
        	
    	}
    	catch (Exception e) {
    		e.printStackTrace();
		}
    	
    	
    	return null;
    }
    
    public void DataTransfer(Product product)
    {
    	currentProduct = product;
    	try {
    		// 사용자가 선택한 상품정보를 서버에게 넘겨주고, 수집정보를 가져온다.
    		Protocol received = NetworkManager.getInstance().connect(ProtocolType.EVENT, EventType.GET_PRODUCT_DETAIL, (Object)currentProduct);
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
	    	String productName = currentProduct.getName();
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
			setupFavoriteView();
    	}
    	catch(Exception e) {
    		IOHandler.getInstance().log("ProductPageController.DataTransfer : " +e);
    	}
    	
    }
    
    private void setupFavoriteView() {
    	// 서버로부터 찜 정보 가져옴. 찜 정보가 있다면 세팅.
    	try {
    		currentFavorite = getFavoriteInfo();
    		
    		if(currentFavorite == null) {
        		targetPrice.setEditable(true);
        		targetPrice.setText("");
        		return;
        	}
        	
        	String targetPriceStr = String.valueOf(currentFavorite.getTargetPrice());
        	targetPrice.setText(targetPriceStr);
        	targetPrice.setEditable(false);
    		
    	}
    	catch (Exception e) {
    		e.printStackTrace();
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
    
    public void zzimRegister()
    {
    	String uId = UserAccount.getInstance().getAccount().getId();
    	String productName = pName.getText();
    	double target = Double.MAX_VALUE;
    	try {
    		target = Double.parseDouble(targetPrice.getText());
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		IOHandler.getInstance().showAlert("올바르지 않은 목표 가격입니다!");
    		return;
		}
    	
    	Favorite favorite = new Favorite(uId, productName, target);
    	
    	if(target>= Double.parseDouble(pPrice.getText()))
    	{
    		IOHandler.getInstance().showAlert("목표 가격은 현재가보다 낮아야 합니다.");
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
	            		currentFavorite = favorite;
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
}
