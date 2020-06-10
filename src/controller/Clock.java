package controller;

import java.util.ArrayList;
import java.util.Calendar;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Account;
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
import utility.UserAccount;

public class Clock extends Thread{
	//0-메인, 1-찜, 2-서치
	private static boolean isRun=false;
	private static boolean mCheck=false;
	private static boolean zCheck=false;
	private static boolean sCheck=false;

	public static boolean isRun() {
		return isRun;
	}

	public boolean ismCheck() {
		return mCheck;
	}

	public void setmCheck(boolean mCheck) {
		this.mCheck = mCheck;
	}

	public boolean iszCheck() {
		return zCheck;
	}

	public void setzCheck(boolean zCheck) {
		this.zCheck = zCheck;
	}

	public boolean issCheck() {
		return sCheck;
	}

	public void setsCheck(boolean sCheck) {
		this.sCheck = sCheck;
	}

	private static Clock cInstance;
	public static Clock getInstance()
	{
		if(cInstance==null)
		{
			return new Clock();
		}		
		return cInstance;
	}
	
	private Clock()
	{
		
	}
	
	Calendar lastAsk = Calendar.getInstance();
	private static final int ASK_PERIOD = 30000;		// 10초마다 한번 서버한테 물어봄
	private static final int DIE_CHECK_PERIOD = 10000;	// 10초마다 한번 죽어도되는지 물어봄
	private static int cnt = 0;
	
	@Override
	public void run() {
		isRun = true;
		cnt++;
		while(isRun)
		{
			System.out.println("ㅇㅋ 파싱함.");
			askToServer();
			lastAsk = Calendar.getInstance();
			
			try {
				while(true) {
	        		Thread.sleep(DIE_CHECK_PERIOD);
	        		System.out.println("저 죽으면 됩니까? 스레드개수 : " + cnt);
	        		
	        		requestClose();
	        		if(isRun == false) {
	        			System.out.println("응 죽어");
	        			break;
	        		}
	        		
	        		Calendar now = Calendar.getInstance();
	        		
	        		long diff = now.getTimeInMillis() - lastAsk.getTimeInMillis();
	        		if(diff > ASK_PERIOD) {
	        			System.out.println("1시간 지났으니 파싱이나 해라");
	        			break;
	        		}
	        	}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
	
	private void askToServer() {
		try {
			Account account = UserAccount.getInstance().getAccount();
			Protocol received = NetworkManager.getInstance().connect(ProtocolType.EVENT, EventType.REQUEST_FAVORITE_CHECK, (Object)account);
        	Response response = received.getResponse();
        	ResponseType type = response.getResponseType();
        	
        	ArrayList<CollectedInfo> receievedList = null;
        	// 응답 결과에 따라 알아서 처리하셈.
        	switch(type) {
            	case SUCCEED:
            		receievedList = (ArrayList<CollectedInfo>) received.getObject();
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
        	
        	if(receievedList != null) {
        		String msg="";
        		for(CollectedInfo c : receievedList)
        		{
        			msg += c.getProductName() + " 이(가) 목표가격 갱신! 현재가격은 "+c.getPrice() + " 입니다. \n";
        		}
        		notify(msg);
        	}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void notify(final String msg) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				IOHandler.getInstance().showAlert(msg);
			}
		});
	}
	
	public void requestClose()
	{	
		//모든 페이지가 닫혔을 때 아시겠나요?
		if(closeCheck())
		{
			System.out.println("ㅎㅇ");
			isRun=false;
		}
			
	}
	
	private boolean closeCheck() {
		boolean x = !(mCheck||zCheck||sCheck);
		return x;
		
	}
	
}


