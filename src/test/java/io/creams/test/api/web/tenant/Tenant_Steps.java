package io.creams.test.api.web.tenant;

import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertThat;

public class Tenant_Steps extends Tenant_Hooks {

	private Tenant_Flows tenant_Flows;

	public Tenant_Steps() {
		super();
		tenant_Flows = new Tenant_Flows();
	}

	@Test
	// 查询租客列表
	public void tenant_b() {
		List<Map<String, Object>> tenantItemsList = given().param("name", Tenant_Hooks.tenant_Info.getName()).when()
				.get("/tenants").then().assertThat().statusCode(200).extract().path("items");
		Map<String, Object> tenantItemsMaps = tenantItemsList.get(new Random().nextInt(tenantItemsList.size()));
		JSONObject tenantItemsJson = new JSONObject(tenantItemsMaps);
		assertThat(tenantItemsJson.toString(),
				matchesJsonSchemaInClasspath("jsonResources/schema/web/tenant/tenant_get_items.json"));
	}

	@Test
	// 新建租客
	public void tenant_a() throws IOException {
		int tagId = tenant_Flows.getTagId();
		JsonObject tenantPostBody = tenant_Flows.putTagParamToJson("jsonResources/web/tenant/createTenant.json", tagId);
		Map<String, Object> resultMap = (given().body(tenantPostBody.toString()).when().post("/tenants").then()
				.assertThat().statusCode(201).extract().jsonPath().get());
		Tenant_Hooks.tenant_Info.setId(Integer.parseInt(resultMap.get("id").toString()));
		Tenant_Hooks.tenant_Info.setName(resultMap.get("name").toString());

	}

	@Test
	// 查看租客详情
	public void tenant_c() {
		given().pathParam("ids", Tenant_Hooks.tenant_Info.getId()).when().get("/tenants/{ids}").then().assertThat()
				.statusCode(200).body(matchesJsonSchemaInClasspath("jsonResources/schema/web/tenant/tenant_info.json"));
	}

	@Test
	// 修改租客
	public void tenant_e() throws IOException {
		int tag = tenant_Flows.getTagId();
		JsonObject uptadeTenantBody = tenant_Flows.putTagParamToJson("jsonResources/web/tenant/createTenant.json", tag);
		given().pathParam("id", Tenant_Hooks.tenant_Info.getId()).body(uptadeTenantBody.toString()).when()
				.put("/tenants/{id}").then().assertThat().statusCode(202);
	}

	@Test
	// 删除租客
	public void tenant_f() {
		given().pathParam("id", Tenant_Hooks.tenant_Info.getId()).when().delete("/tenants/{id}").then().assertThat()
				.statusCode(204);
	}

	@Test
	// 查询租客开票信息
	public void tenant_d() {
		given().queryParam("tenantIds", Tenant_Hooks.tenant_Info.getId()).when().get("/tenants/invoiceInfos").then()
				.assertThat().statusCode(200)
				.body(matchesJsonSchemaInClasspath("jsonResources/schema/web/tenant/tenantInvoiceInfos.json"));
	}

	@Test
	// 租客统计查询
	public void tenantStatistics() {
		given().when().get("/tenants/statistics").then().assertThat().statusCode(200)
				.body(matchesJsonSchemaInClasspath("jsonResources/schema/web/tenant/tenantStatistics.json"));
	}

}
