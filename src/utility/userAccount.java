package utility;

public class userAccount {
	private static userAccount uInstance;
	private static String userID;
	
	public static userAccount getInstance()
	{
		if(uInstance==null)
		{
			uInstance = new userAccount();
		}
		return uInstance;
	}
	
	private userAccount() 
	{
		
	}
	
	public void setID(String s)
	{
		userID=s;
	}
	
	public String getID()
	{
		return userID;
	}
}

