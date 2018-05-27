package io.creams.test.api.web.businessCommon;

import org.hamcrest.CoreMatchers;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BusinessCommon_Steps extends BusinessCommon_Hooks{
	private BusinessCommon_Flows businessCommon_Flows;

	public BusinessCommon_Steps() {
		super();
		businessCommon_Flows = new BusinessCommon_Flows();
	}
	
	@Test
	//添加标签
	public void commons_tags_a() {
		Map<String, Object> tagsPostBody = businessCommon_Flows.TagsInfoMap(BusinessCommon_Hooks.tags_Info_create.getName(),BusinessCommon_Hooks.tags_Info_create.getTypeEnum());
		given().body(tagsPostBody).when().post("/tags").then().assertThat().statusCode(201);
	}
	
	@Test
	//标签列表
	public void commons_tags_b() {
		List<Map<String, Object>> tagsListItems = given().param("tagType","ROOM").when().get("/tags").
				then().assertThat().statusCode(200).body(matchesJsonSchemaInClasspath("jsonResources/schema/web/businessCommon/tags_get_items.json")).extract().path("");
		Map<String, Object> tagMap= tagsListItems.get(tagsListItems.size()-1);
		Assert.assertThat((String)(tagMap.get("name")), CoreMatchers.containsString(BusinessCommon_Hooks.tags_Info_create.getName()));
		
	}
	
	@Test
	//修改标签 
	public void commons_tags_c() {
		Map<String, Object> tagsPostBody = businessCommon_Flows.TagsInfoMap(BusinessCommon_Hooks.tags_Info_modify.getName(),BusinessCommon_Hooks.tags_Info_modify.getTypeEnum());
		given().pathParam("id", businessCommon_Flows.getTagId(BusinessCommon_Hooks.tags_Info_create.getName())).body(tagsPostBody).
		when().put("/tags/{id}").then().assertThat().statusCode(200);
	}
	
	@Test
	//删除标签
	public void commons_tags_d() {
		//获取id
		given().pathParam("id", businessCommon_Flows.getTagId(BusinessCommon_Hooks.tags_Info_modify.getName())).when().delete("/tags/{id}").then().assertThat().statusCode(200);
	}
	
	@Test
	//创建自定义类型
	public void commons_category_b() {
		Map<String,Object> categoryPostBody = businessCommon_Flows.CategoryInfoMap(BusinessCommon_Hooks.category_Info.getCategoryEnum(), BusinessCommon_Hooks.category_Info.getName());
		given().body(categoryPostBody).when().post("/commons/custom-category").
		then().assertThat().statusCode(200);
	}
	
	@Test
	//查询自定义类型
	public void commons_category_c() {
		List<Map<String,Object>> categoryListItems  = given().queryParam("categoryEnum", "BILL").when().get("/commons/custom-category").
				then().assertThat().statusCode(200).extract().path("");
		//过滤id=null的map
		categoryListItems = businessCommon_Flows.filterList(categoryListItems);
		Map<String,Object> categoryMap = categoryListItems.get(categoryListItems.size()-1);
		Assert.assertThat((String)(categoryMap.get("name")), CoreMatchers.containsString(BusinessCommon_Hooks.category_Info.getName()));
		JSONObject catagoryMapJson = new JSONObject(categoryMap);
		Assert.assertThat(catagoryMapJson.toString(), matchesJsonSchemaInClasspath("jsonResources/schema/web/businessCommon/category_get_items.json"));
	}
	
	@Test
	//删除自定义类型
	public void commons_category_d() {
		given().pathParam("id", businessCommon_Flows.getCategoryId(BusinessCommon_Hooks.category_Info.getName())).when().delete("/commons/custom-category/{id}").
		then().assertThat().statusCode(200);
	}
}
