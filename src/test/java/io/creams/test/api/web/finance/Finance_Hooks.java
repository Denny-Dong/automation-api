package io.creams.test.api.web.finance;

import io.creams.test.api.HooksApi;
import io.creams.test.api.UserAccount;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class Finance_Hooks extends HooksApi {
	static UserAccount userAccount;
	static Finance_Info finance_Info;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		HooksApi.driver.createNewDriverInstance();
		userAccount = new UserAccount();
		driver.switchToAppUrlWithAuthentication(userAccount.getAccessToken());
		finance_Info = new Finance_Info();

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
