package io.creams.test.api.web.businessCommon;

import io.creams.test.api.HooksApi;
import io.creams.test.api.UserAccount;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class BusinessCommon_Hooks extends HooksApi{
	static Tags_Info tags_Info_create;
	static Tags_Info tags_Info_modify;
	
	static Category_info category_Info;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		HooksApi.driver.createNewDriverInstance();
		UserAccount userAccount = new UserAccount();
		driver.switchToAppUrlWithAuthentication(userAccount.getAccessToken());
		tags_Info_create = new Tags_Info("ApiTesting","ROOM");
		tags_Info_modify = new Tags_Info("ApiTestingModify","ROOM");
		category_Info = new Category_info("BILL","ApiTesting");
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
