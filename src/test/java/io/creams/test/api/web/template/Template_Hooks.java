package io.creams.test.api.web.template;

import io.creams.test.api.HooksApi;
import io.creams.test.api.UserAccount;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class Template_Hooks extends HooksApi {
	static Template_Info template_Info;
	static UserAccount userAccount;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		HooksApi.driver.createNewDriverInstance();
		userAccount = new UserAccount();
		driver.switchToAppUrlWithAuthentication(userAccount.getAccessToken());
		template_Info = new Template_Info("ApiTesting");
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
