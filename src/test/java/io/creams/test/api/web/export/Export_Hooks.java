package io.creams.test.api.web.export;

import io.creams.test.api.HooksApi;
import io.creams.test.api.UserAccount;
import io.creams.test.api.data.CollectionsSlave;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

;

public class Export_Hooks extends HooksApi {
	static Export_Info export_Info;
	static UserAccount userAccount;
	static CollectionsSlave collectionsSlave;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		HooksApi.driver.createNewDriverInstance();
		userAccount = new UserAccount();
		driver.switchToAppUrlWithAuthentication(userAccount.getAccessToken());
		export_Info = new Export_Info("ApiTesting");
		collectionsSlave = new CollectionsSlave();
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
