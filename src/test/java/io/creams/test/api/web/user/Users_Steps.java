package io.creams.test.api.web.user;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertThat;

public class Users_Steps extends Users_Hooks {

	private Users_Flows users_Flows;

	public Users_Steps() {
		super();
		users_Flows = new Users_Flows();
	}

	@Test
	// 查询消息列表
	public void users_messages() {
		given().params("page", 1, "limit", 20).when().get("/users/messages").then().assertThat().statusCode(200)
				.body(matchesJsonSchemaInClasspath("jsonResources/schema/web/users/users_messages_get.json"));
	}

	@Test
	// 查询查询用户是否申请过使用
	public void users_application() {
		@SuppressWarnings("unchecked")
		Map<String, Object> userMap = (Map<String, Object>) userAccount.getUserInfoMap().get("user");
		String responseBody = given().params("tel", userMap.get("userTel")).when().get("/users/application").then()
				.assertThat().statusCode(200).extract().body().asString();
		assertThat(responseBody, CoreMatchers.notNullValue());

	}

	@Test
	// 获取用户下的所有权限
	public void users_authorities_list_get() {
		String responseBody = given().when().get("/users/authorities/list").then().assertThat().statusCode(200)
				.body(matchesJsonSchemaInClasspath("jsonResources/schema/web/users/users_authorities_list_get.json"))
				.extract().path("dataScopeAccessLevel[0].dataScope");
		assertThat(responseBody, CoreMatchers.equalTo("CONTRACT"));
	}

	@Test
	// 查询用户列表
	public void users_customer() {
		@SuppressWarnings("unchecked")
		Map<String, Object> userMap = (Map<String, Object>) userAccount.getUserInfoMap().get("user");
		given().queryParam("userId", userMap.get("userId")).when().get("/users/members").then().assertThat()
				.statusCode(200);
	}

	@Test
	// 查询用户customer自定义logos
	public void users_customers_logos() {
		@SuppressWarnings("unchecked")
		Map<String, Object> userMap = (Map<String, Object>) userAccount.getUserInfoMap().get("user");
		given().pathParam("id", userMap.get("customerId")).when().get("/users/customers/{id}/logos").then().assertThat()
				.statusCode(200);
	}

	@Test
	// 查询用户列表
	public void users_members() {
		given().when().get("/users/members").then().statusCode(200);

	}

}
