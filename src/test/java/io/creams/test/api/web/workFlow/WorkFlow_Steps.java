package io.creams.test.api.web.workFlow;

import org.json.JSONObject;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertThat;

public class WorkFlow_Steps extends WorkFlow_Hooks {
	private WorkFlow_Flows workFlow_Flows;

	public WorkFlow_Steps() {
		super();
		workFlow_Flows = new WorkFlow_Flows();
	}

	@Test
	// 招商提醒列表
	public void workflows() {
		List<Map<String, Object>> workflowsItemsList = given().params("status", "ACTIVE", "page", 1, "size", 20).when()
				.get("/workflows/demands").then().assertThat().statusCode(200).extract().path("items");
		Map<String, Object> workflowsItemsMaps = workflowsItemsList
				.get(new Random().nextInt(workflowsItemsList.size()));
		JSONObject workflowsItemsJson = new JSONObject(workflowsItemsMaps);
		assertThat(workflowsItemsJson.toString(),
				matchesJsonSchemaInClasspath("jsonResources/schema/web/workflows/workflows_get_items.json"));

	}
}
