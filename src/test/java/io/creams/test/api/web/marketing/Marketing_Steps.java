package io.creams.test.api.web.marketing;

import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class Marketing_Steps extends Marketing_Hooks {
	private Marketing_Flows marketing_Flows;
	
	public Marketing_Steps() {
		super();
		marketing_Flows = new Marketing_Flows();
	}

	@Test
	// 查询渠道联系人列表
	public void Marketing_Channel_Contact_b() {
		List<String> telList = given().params("page", 1, "size", 20, "sort", "createdDate,desc").when()
				.get("/marketing/channel/contacts").then().assertThat().statusCode(200)
				.body(matchesJsonSchemaInClasspath(
						"jsonResources/schema/web/marketing/marketing_channel_contacts_list.json"))
				.extract().path("items.tel");
		Boolean isContains = false;
		for (String tel : telList) {
			System.out.println(Marketing_Hooks.channel_Contract_Info.getTel());
			if (tel != null && tel.equals(Marketing_Hooks.channel_Contract_Info.getTel().toString())) {
				isContains = true;
				break;
			}
		}
		Assert.assertTrue(isContains);
	}

	@Test
	// 新建渠道联系人
	public void Marketing_Channel_Contact_a() throws IOException {
		JsonObject channelContactInfoPostBody = marketing_Flows
				.getChannelContactJson("jsonResources/web/marketing/marketing_create_channel_contact.json");
		Map<String, Object> resultMap  = given().body(channelContactInfoPostBody.toString()).when().post("/marketing/channel/contacts")
				.then().assertThat().statusCode(201).extract().jsonPath().get();
		Marketing_Hooks.channel_Contract_Info.setId(Integer.parseInt(resultMap.get("id").toString()));
		Marketing_Hooks.channel_Contract_Info.setTel(resultMap.get("tel").toString());
		System.out.println(channel_Contract_Info.getId());
		
	}
	
	@Test
	//查询渠道联系人详情
	public void Marketing_Channel_Contact_c(){
		given().pathParam("id", Marketing_Hooks.channel_Contract_Info.getId()).
		when().get("/marketing/channel/contact/{id}").
		then().assertThat().statusCode(200).body(matchesJsonSchemaInClasspath("jsonResources/schema/web/marketing/marketing_channel_contacts_info.json"));
	}

	@Test
	// 批量删除渠道联系人
	public void Marketing_Channel_Contact_d() {		
		given().pathParam("contactIds", Marketing_Hooks.channel_Contract_Info.getId()).
		when().delete("/marketing/channel/contacts/{contactIds}").
		then().assertThat().statusCode(200);
	}

	@Test
	// 获取各类经纪人的数量
	public void Marketing_Channel_Contact_Count() {
		given().when().get("/marketing/channel/contacts/count").then().assertThat().statusCode(200)
				.body(matchesJsonSchemaInClasspath(
						"jsonResources/schema/web/marketing/marketing_channel_contact_count.json"));
	}
	
	@Test
	//查询所有渠道联系人的region信息
	public void Marketing_Channel_Contact_region(){
		given().
		when().get("/marketing/channel/contacts/regions").
		then().assertThat().statusCode(200).body(matchesJsonSchemaInClasspath("jsonResources/schema/web/marketing/marketing_channel_contact_region.json"));
		}
	
	@Test
	//新建租客联系人
	public void Marketing_Tenant_Contact_a() throws IOException{
		JsonObject TenantContactInfoPostBody = marketing_Flows.getChannelContactJson("jsonResources/web/marketing/marketing_create_tenant_contact.json");
		Map<String,Object> CreateTenantContactMap = given().body(TenantContactInfoPostBody.toString()).when().post("/marketing/tenants/contacts").
				then().assertThat().statusCode(201).extract().jsonPath().get();
		Marketing_Hooks.channel_Contract_Info.setName(CreateTenantContactMap.get("name").toString());
	}
	
	@Test
	//查询租客联系人列表
	public void Marketing_Tenant_Contact_b(){ 
		List<Map<String,Object>> tenantContactList = given().params("page", 1, "size", 20).
		when().get("/marketing/tenants/contacts").
		then().assertThat().statusCode(200).extract().path("items");
		Map<String,Object> tenantContactInfoMaps = tenantContactList.get(new Random().nextInt(tenantContactList.size()));
		JSONObject tenantContactJson = new JSONObject(tenantContactInfoMaps);
		Assert.assertThat(tenantContactJson.toString(), matchesJsonSchemaInClasspath("jsonResources/schema/web/marketing/marketing_tenant_contact_list.json"));
	}
}
