package io.creams.test.api.web.analysis;

import io.creams.test.api.HooksApi;
import io.creams.test.api.UserAccount;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class Analysis_Hooks extends HooksApi{
	static UserAccount userAccount;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		HooksApi.driver.createNewDriverInstance();
		UserAccount userAccount = new UserAccount();
		driver.switchToAppUrlWithAuthentication(userAccount.getAccessToken());
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
