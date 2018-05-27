package io.creams.test.api.web.finance;

import org.json.JSONObject;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertThat;

public class Finance_Steps extends Finance_Hooks {
	private Finance_Flows finance_Flows;

	public Finance_Steps() {
		super();
		finance_Flows = new Finance_Flows();
	}

	@Test
	//查询银行账户列表
	public void finance() {
		List<Map<String, Object>> financeItemsList = given().params("page", 1, "size", 10).when()
				.get("/account").then().assertThat().statusCode(200).extract().path("items");
		Map<String, Object> financeItemsMaps = financeItemsList
				.get(new Random().nextInt(financeItemsList.size()));
		int accountId = Integer.parseInt(financeItemsMaps.get("id").toString());
		Finance_Hooks.finance_Info.setAccountId(Integer.parseInt(financeItemsMaps.get("id").toString()));
		JSONObject financeItemsJson = new JSONObject(financeItemsMaps);
		assertThat(financeItemsJson.toString(),
				matchesJsonSchemaInClasspath("jsonResources/schema/web/finance/account_get_items.json"));

	}
	
	//添加银行账户
	
	@Test
	//查询商品码
	public void commodity() {
		List<Map<String,Object>> commodityList=given().when().get("/invoices/commodity").then().assertThat().statusCode(200).extract().path("");
		Map<String,Object> commodityMap = commodityList.get(new Random().nextInt(commodityList.size()));
		JSONObject commodityJson = new JSONObject(commodityMap);
		assertThat(commodityJson.toString(),
				matchesJsonSchemaInClasspath("jsonResources/schema/web/finance/commodity_get_items.json"));
				
	}

	
	
}
