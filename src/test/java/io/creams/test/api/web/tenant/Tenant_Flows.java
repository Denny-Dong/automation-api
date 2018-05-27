package io.creams.test.api.web.tenant;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.creams.test.api.HooksApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class Tenant_Flows {

	protected Logger logger = LoggerFactory.getLogger(getClass().getName());

	public JsonObject getTenantJson(String filepath) throws IOException {
		return HooksApi.driver.jsonObjectProducer(filepath);
	}

	public JsonObject putTenantJson(String filepath) throws IOException {
		return HooksApi.driver.jsonObjectProducer(filepath);
	}

	public JsonObject putTagParamToJson(String filepath, int tag) throws IOException {
		JsonObject tenantPostBody = this.getTenantJson(filepath);
		JsonArray tagList = new JsonArray();
		tagList.add(tag);
		tenantPostBody.add("tags", tagList);
		return tenantPostBody;
	}

	public int getTagId() {
		List<Map<String, Object>> tagItemsList = given().queryParam("tagType", "TENANT").when().get("/tags").then()
				.assertThat().statusCode(200).extract().path("");
		Map<String, Object> tagItemsMaps = tagItemsList.get(new Random().nextInt(tagItemsList.size()));
		int tagId = Integer.parseInt(tagItemsMaps.get("id").toString());
		return tagId;
	}
}
