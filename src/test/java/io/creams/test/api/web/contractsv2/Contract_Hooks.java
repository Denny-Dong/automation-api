package io.creams.test.api.web.contractsv2;

import io.creams.test.api.data.CollectionsSlave;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import io.creams.test.api.HooksApi;
import io.creams.test.api.UserAccount;

public class Contract_Hooks extends HooksApi{
	static UserAccount userAccount;
	static ContractData contractData;
	static CollectionsSlave collectionsSlave;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		HooksApi.driver.createNewDriverInstance();
		userAccount = new UserAccount();
		driver.switchToAppUrlWithAuthentication(userAccount.getAccessToken());
		contractData = new ContractData("租赁合同接口自动化测试");
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
