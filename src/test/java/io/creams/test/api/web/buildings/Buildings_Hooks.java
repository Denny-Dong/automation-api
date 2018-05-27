package io.creams.test.api.web.buildings;

import io.creams.test.api.HooksApi;
import io.creams.test.api.UserAccount;
import io.creams.test.api.data.CollectionsSlave;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class Buildings_Hooks extends HooksApi {
	protected static Floor_Info floor_Info;
	static Portfolio_Info portfolio_Info;
	static CollectionsSlave collectionsSlave;
	static UserAccount userAccount;
	static PermitPrice_Info permitPrice_Info;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		HooksApi.driver.createNewDriverInstance();
		userAccount = new UserAccount();
		driver.switchToAppUrlWithAuthentication(userAccount.getAccessToken());
		portfolio_Info = new Portfolio_Info("ApiTesting");
		floor_Info = new Floor_Info();
		collectionsSlave = new CollectionsSlave();
		permitPrice_Info = new PermitPrice_Info(1, 18.88, "D");
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
