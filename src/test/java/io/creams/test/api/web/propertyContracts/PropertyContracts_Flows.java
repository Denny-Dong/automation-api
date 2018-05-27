package io.creams.test.api.web.propertyContracts;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.creams.test.api.HooksApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static io.restassured.RestAssured.given;

public class PropertyContracts_Flows {
	protected Logger logger = LoggerFactory.getLogger(getClass().getName());

	public JsonObject getPropertyContractParaJson(String filepath) throws IOException {
		return HooksApi.driver.jsonObjectProducer(filepath);
	}

	public JsonObject putRoomParamToJson(String filepath, int room) throws IOException {
		JsonObject createPropertyContractBody = this.getPropertyContractParaJson(filepath);
		JsonArray roomsList = new JsonArray();
		roomsList.add(room);
		createPropertyContractBody.add("roomIds", roomsList);
		return createPropertyContractBody;
	}

	public JsonObject putRoomAndTenantParamToJson(String filepath, int room, int tenantId, String tenantName)
			throws IOException {
		JsonObject createPropertyContractBody = this.getPropertyContractParaJson(filepath);
		JsonArray roomsList = new JsonArray();
		roomsList.add(room);
		createPropertyContractBody.add("roomIds", roomsList);
		createPropertyContractBody.addProperty("tenantId", String.valueOf(tenantId));
		createPropertyContractBody.addProperty("tenantName", tenantName);
		return createPropertyContractBody;
	}

	public JsonObject putTenantNameToJson(String filepath, String tenantName) throws IOException {
		JsonObject createPropertyContractBody = this.getPropertyContractParaJson(filepath);
		// createPropertyContractBody.addProperty("tenant", tenantName);
		return createPropertyContractBody;
	}

	public List<Integer> getBuildingIdList() {
		@SuppressWarnings("unchecked")
		Map<String, Object> userMap = (Map<String, Object>) PropertyContracts_Hooks.userAccount.getUserInfoMap()
				.get("user");
		@SuppressWarnings("unchecked")
		List<Integer> buildingIdList = (List<Integer>) userMap.get("buildingIdList");
		return buildingIdList;
	}

	public int getUserId() {
		@SuppressWarnings("unchecked")
		Map<String, Object> userMap = (Map<String, Object>) PropertyContracts_Hooks.userAccount.getUserInfoMap()
				.get("user");
		int userId = (int) userMap.get("userId");
		return userId;
	}

	public int getRooms(Integer buildingId) throws IOException {
		List<Map<String, Object>> propertyContractItemsList = given().pathParam("buildingids", buildingId.toString())
				.when().get("/buildings/{buildingids}/rooms/marketing").then().assertThat().statusCode(200).extract()
				.path("items");

		if (propertyContractItemsList.size() == 0) {
			return this.createRooms(buildingId);
		} else {
			Map<String, Object> propertyContractItemsMaps = propertyContractItemsList
					.get(new Random().nextInt(propertyContractItemsList.size()));

			int roomId = Integer.parseInt(propertyContractItemsMaps.get("id").toString());

			return roomId;
		}
	}

	public void instances(String status, String comment) {
		given().when().pathParam("id", this.waitForMyApproval()).queryParams("status", status, "comment", comment)
				.put("/oa/instances/{id}").then().assertThat().statusCode(200);
	}

	public Map<String, Object> invalidInfoMap(String action, String memo) {
		Map<String, Object> invalidInfoPostBody = new HashMap<String, Object>();
		invalidInfoPostBody.put("action", action);
		invalidInfoPostBody.put("memo", memo);
		return invalidInfoPostBody;
	}

	public Map<String, Object> initiateApprovalInfoMap(int userId, int objectId, int buildingId, String subDomainType,
			String domainType, String description) {
		List<Map<String, Object>> userIdList = new ArrayList<Map<String, Object>>();
		Map<String, Object> userInfoMap = new HashMap<String, Object>();
		userInfoMap.put("queue", 1);
		userInfoMap.put("userId", userId);
		userIdList.add(userInfoMap);
		Map<String, Object> initiateApprovalInfoPostBody = new HashMap<String, Object>();
		initiateApprovalInfoPostBody.put("objectId", objectId);
		initiateApprovalInfoPostBody.put("buildingId", buildingId);
		initiateApprovalInfoPostBody.put("subDomainType", subDomainType);
		initiateApprovalInfoPostBody.put("domainType", domainType);
		initiateApprovalInfoPostBody.put("description", description);
		initiateApprovalInfoPostBody.put("approvers", userIdList);
		return initiateApprovalInfoPostBody;
	}

	public void initiateApproval(int userId, int objectid, int buildingid, String subDomainType, String domainType,
			String description) {
		Map<String, Object> initiateApprovalInfoPostBody = this.initiateApprovalInfoMap(userId, objectid, buildingid,
				subDomainType, domainType, description);
		given().when().body(initiateApprovalInfoPostBody).post("/oa/start").then().assertThat().statusCode(200);
	}

	public int waitForMyApproval() {
		List<Map<String, Object>> myApprovalList = given().when().get("/oa/instances/todo").then().assertThat()
				.statusCode(200).extract().path("items");
		Map<String, Object> myApprovalMaps = myApprovalList.get(0);
		return Integer.parseInt(myApprovalMaps.get("id").toString());
	}

	public int createRooms(int buildingId) throws IOException {
		JsonObject createRooms = this.getPropertyContractParaJson("jsonResources/web/roomController/createRoom.json");
		return Integer.parseInt(given().pathParam("buildingId", buildingId).body(createRooms.toString()).when()
				.post("/buildings/{buildingId}/rooms").then().assertThat().statusCode(201).extract().body().asString());
	}

	public Map<String, Object> getTenant() {
		List<Map<String, Object>> tenantItemsList = given().when().get("/tenants").then().assertThat().statusCode(200)
				.extract().path("items");
		Map<String, Object> tenantItemsMaps = tenantItemsList.get(new Random().nextInt(tenantItemsList.size()));
		return tenantItemsMaps;
	}

}
