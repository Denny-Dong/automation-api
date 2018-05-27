package io.creams.test.api.web.oa;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.creams.test.api.HooksApi;

import java.io.IOException;
import java.util.*;

import static io.restassured.RestAssured.given;

public class OA_Flows {

	public JsonObject getcontractParaJson(String filepath) throws IOException {
		return HooksApi.driver.jsonObjectProducer(filepath);
	}

	public List<Object> getBuildingIdList() {
		@SuppressWarnings("unchecked")
		Map<String, Object> userMap = (Map<String, Object>) OA_Hooks.userAccount.getUserInfoMap().get("user");
		@SuppressWarnings("unchecked")
		List<Object> buildingIdList = (List<Object>) userMap.get("buildingIdList");
		return buildingIdList;
	}

	public int getRooms(String buildingId) {
		List<Map<String, Object>> contractItemsList = given().pathParam("buildingids", buildingId).when()
				.get("/buildings/{buildingids}/rooms/marketing").then().assertThat().statusCode(200).extract()
				.path("items");

		Map<String, Object> contractItemsMaps = contractItemsList.get(new Random().nextInt(contractItemsList.size()));

		int roomId = Integer.parseInt(contractItemsMaps.get("id").toString());

		return roomId;
	}

	public int getUserId() {
		@SuppressWarnings("unchecked")
		Map<String, Object> userMap = (Map<String, Object>) OA_Hooks.userAccount.getUserInfoMap().get("user");
		int userId = (int) userMap.get("userId");
		return userId;
	}

	//
	public JsonObject getContractParaJson(String filepath) throws IOException {
		return HooksApi.driver.jsonObjectProducer(filepath);
	}

	// 改变roomId
	public JsonObject putRoomParamToJson(String filepath, int room, int followId) throws IOException {
		JsonObject createContractBody = this.getContractParaJson(filepath);
		JsonArray roomsList = new JsonArray();
		roomsList.add(room);
		createContractBody.add("roomIds", roomsList);
		createContractBody.addProperty("followId", Integer.toString(followId));
		return createContractBody;
	}

	public int V2_contracts(int roomId, int followId) throws IOException {
		JsonObject createContractBody = putRoomParamToJson("jsonResources/web/OA/oa_create_contract.json", roomId,
				followId);
		return Integer.parseInt(given().body(createContractBody.toString()).when().post("/v2/contracts").then()
				.assertThat().statusCode(201).extract().body().asString());
	}

	// 封装参数
	public Map<String, Object> oaStartOfContractMap(int userId, int buildingId, String description, String domainType,
			int objectId, String subDomainType) {
		List<Map<String, Object>> userIdList = new ArrayList<Map<String, Object>>();
		Map<String, Object> userInfoMap = new HashMap<String, Object>();
		userInfoMap.put("queue", 1);
		userInfoMap.put("userId", userId);
		userIdList.add(userInfoMap);
		Map<String, Object> oaStartOfContractBody = new HashMap<String, Object>();
		oaStartOfContractBody.put("objectId", objectId);
		oaStartOfContractBody.put("buildingId", buildingId);
		oaStartOfContractBody.put("subDomainType", subDomainType);
		oaStartOfContractBody.put("domainType", domainType);
		oaStartOfContractBody.put("description", description);
		oaStartOfContractBody.put("approvers", userIdList);
		return oaStartOfContractBody;
	}

	public Map<String, Object> oaSettingsMap(int userId, int buildingId, String domainType, String subDomainType) {
		List<Map<String, Object>> userIdList = new ArrayList<Map<String, Object>>();
		Map<String, Object> userInfoMap = new HashMap<String, Object>();
		userInfoMap.put("queue", 1);
		userInfoMap.put("userId", userId);
		userIdList.add(userInfoMap);
		Map<String, Object> oaSettingsBody = new HashMap<String, Object>();
		oaSettingsBody.put("buildingId", buildingId);
		oaSettingsBody.put("subDomainType", subDomainType);
		oaSettingsBody.put("domainType", domainType);
		oaSettingsBody.put("approvers", userIdList);
		return oaSettingsBody;
	}

}
