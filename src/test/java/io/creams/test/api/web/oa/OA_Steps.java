package io.creams.test.api.web.oa;

import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertThat;

public class OA_Steps extends OA_Hooks {
	private OA_Flows oa_Flows;

	public OA_Steps() {
		super();
		oa_Flows = new OA_Flows();
	}

	@Test
	// 获取流程实例审批历史
	public void oa_history_instances_id() {
		given().pathParam("id", 37).when().get("/oa/history/instances/{id}").then().assertThat().statusCode(200)
				.body(matchesJsonSchemaInClasspath("jsonResources/schema/web/OA/oa_history_instances_id.json"));
	}

	@Test
	// 获取对象审批历史
	public void oa_history_object() {
		given().params("objectId", 0, "domainType", "CONTRACT", "subDomainType", "NEW_APPROVAL").when()
				.get("/oa/history/object").then().assertThat().statusCode(200)
				.body(matchesJsonSchemaInClasspath("jsonResources/schema/web/OA/oa_history_object.json"));
	}

	@Test
	// 我已审批
	public void oa_instances_done() {
		List<Map<String, Object>> oaInstancesDoneItemsList = given()
				.queryParams("runtimeStatus", "FINISHED", "taskStatus", "APPROVED").when().get("/oa/instances/done")
				.then().assertThat().statusCode(200).extract().path("items");
		Map<String, Object> oaInstancesDoneItemsMaps = oaInstancesDoneItemsList
				.get(new Random().nextInt(oaInstancesDoneItemsList.size()));
		JSONObject oaInstancesDoneItemsJson = new JSONObject(oaInstancesDoneItemsMaps);
		assertThat(oaInstancesDoneItemsJson.toString(),
				matchesJsonSchemaInClasspath("jsonResources/schema/web/OA/oa_instances_done.json"));

	}

	@Test
	// 我发起的
	public void oa_instances_a() {
		List<Map<String, Object>> oaInstancesStartedItemsList = given().queryParams("runtimeStatus", "RUNNING").when()
				.get("oa/instances/started").then().assertThat().statusCode(200).extract().path("items");
		Map<String, Object> oaInstancesStartedItemsMaps = oaInstancesStartedItemsList.get(0);
		OA_Hooks.oA_Info.setOaInstancesStartedItemsMaps(oaInstancesStartedItemsMaps);
		JSONObject oaInstancesStartedItemsJson = new JSONObject(oaInstancesStartedItemsMaps);
		assertThat(oaInstancesStartedItemsJson.toString(),
				matchesJsonSchemaInClasspath("jsonResources/schema/web/OA/oa_instances_started.json"));
	}

	@Test
	// 待我审批
	public void oa_instances_todo() {
		List<Map<String, Object>> oaInstancesToDoItemsList = given().queryParams("subDomainTypes", "NEW_APPROVAL")
				.when().get("oa/instances/todo").then().assertThat().statusCode(200).extract().path("items");
		Map<String, Object> oaInstancesToDoItemsMaps = oaInstancesToDoItemsList
				.get(new Random().nextInt(oaInstancesToDoItemsList.size()));
		JSONObject oaInstancesToDoItemsJson = new JSONObject(oaInstancesToDoItemsMaps);
		assertThat(oaInstancesToDoItemsJson.toString(),
				matchesJsonSchemaInClasspath("jsonResources/schema/web/OA/oa_instances_todo.json"));
	}

	@Test
	// 发起审批
	public void oa_start_a() throws IOException {
		String BuildingsIdString = oa_Flows.getBuildingIdList().get(1).toString();
		int roomId = oa_Flows.getRooms(BuildingsIdString);
		int userId = oa_Flows.getUserId();
		OA_Hooks.oA_Info.setBuildingID(Integer.parseInt(BuildingsIdString));
		OA_Hooks.oA_Info.setRoomID(roomId);
		OA_Hooks.oA_Info.setUserID(userId);
		OA_Hooks.oA_Info.setContractID(oa_Flows.V2_contracts(roomId, userId));
		Map<String, Object> oaStaretOfContractMap = oa_Flows.oaStartOfContractMap(userId,
				OA_Hooks.oA_Info.getBuildingID(), "楼宇名称/201/租客租客", "CONTRACT", OA_Hooks.oA_Info.getContractID(),
				"NEW_APPROVAL");
		given().when().body(oaStaretOfContractMap).post("/oa/start").then().assertThat().statusCode(200).extract()
				.body().asString();

	}

	@Test
	// 获取流程审核详情
	public void oa_start_b() {
		Map<String, Object> oaInstancesStartedItemsMaps = OA_Hooks.oA_Info.getOaInstancesStartedItemsMaps();
		int startId = Integer.parseInt(oaInstancesStartedItemsMaps.get("id").toString());
		given().pathParam("id", startId).when().get("/oa/instances/{id}").then().assertThat().statusCode(200);
		// .body(matchesJsonSchemaInClasspath("jsonResources/schema/web/OA/oa_instances_id_b.json"));
	}

	@Test
	// 审批流程
	public void oa_start_d() {
		Map<String, Object> oaInstancesStartedItemsMaps = OA_Hooks.oA_Info.getOaInstancesStartedItemsMaps();
		int startId = Integer.parseInt(oaInstancesStartedItemsMaps.get("id").toString());
		given().pathParam("id", startId).queryParam("status", "APPROVED").when().put("/oa/instances/{id}").then()
				.assertThat().statusCode(200);
	}

	@Test
	// 回收申请
	public void oa_start_e() {
		Map<String, Object> oaInstancesStartedItemsMaps = OA_Hooks.oA_Info.getOaInstancesStartedItemsMaps();
		int startId = Integer.parseInt(oaInstancesStartedItemsMaps.get("id").toString());
		given().pathParam("id", startId).queryParam("flag", "true").when().delete("/oa/instances/{id}").then()
				.assertThat().statusCode(200);
	}

	@Test
	// 获取流程定义详情
	public void oa_start_c() {
		given().queryParams("buildingId", OA_Hooks.oA_Info.getBuildingID(), "domainType", "CONTRACT", "subDomainType",
				"NEW_APPROVAL").when().get("/oa/settings").then().assertThat().statusCode(200);

	}

	// 添加流程定义
	public void oa_start_f() {
		int userId = oa_Flows.getUserId();
		Map<String, Object> oaStaretOfContractMap = oa_Flows.oaSettingsMap(userId, OA_Hooks.oA_Info.getBuildingID(),
				"CONTRACT", "NEW_APPROVAL");
		given().body(oaStaretOfContractMap).when().post("/oa/settings").then().assertThat().statusCode(200);
	}

	// 修改流程定义
	public void oa_Settings_id() {
		int userId = oa_Flows.getUserId();
		Map<String, Object> oaStaretOfContractMap = oa_Flows.oaSettingsMap(userId, OA_Hooks.oA_Info.getBuildingID(),
				"CONTRACT", "NEW_APPROVAL");
		// given().pathParam().body(oaStaretOfContractMap).when().put("/oa/settings").then().assertThat()
		// .statusCode(200);

	}

	@Test
	// 工作流待办数量
	public void oa_statistics() {
		given().when().get("/oa/statistics").then().assertThat().statusCode(200)
				.body(matchesJsonSchemaInClasspath("jsonResources/schema/web/OA/oa_statistics.json"));
	}
}
