package io.creams.test.api.web.marketing;

import io.creams.test.api.HooksApi;
import io.creams.test.api.UserAccount;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class Marketing_Hooks extends HooksApi{
	static Channel_Contract_Info channel_Contract_Info;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		HooksApi.driver.createNewDriverInstance();
		UserAccount userAccount = new UserAccount();
		driver.switchToAppUrlWithAuthentication(userAccount.getAccessToken());
		channel_Contract_Info = new Channel_Contract_Info();
	}

	@AfterClass
	public static void tearDownAfterClass() {
	}

	@Before
	public void setUp() throws Throwable {
	}

	@After
	public void tearDown() {

	}
}
