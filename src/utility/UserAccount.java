package utility;

import model.Account;

public class UserAccount {
	private static UserAccount uInstance;
	private static Account account;
	
	public static UserAccount getInstance()
	{
		if(uInstance==null)
		{
			uInstance = new UserAccount();
		}
		return uInstance;
	}
	
	private UserAccount() 
	{
		
	}


	public static Account getAccount() {
		return account;
	}

	public static void setAccount(Account account) {
		UserAccount.account = account;
	}

}

