package io.creams.test.api.web.buildings;

import io.creams.test.api.data.CollectionsSlave;
import org.hamcrest.CoreMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertThat;

public class Buildings_Flows {
	protected Logger logger = LoggerFactory.getLogger(getClass().getName());

	public Map<String, Object> getBuildingFloorInfoMap(List<Map<String, Object>> buildingFloorList) {
		Map<String, Object> floorInfoMap = new HashMap<>();
		for (Map<String, Object> map : buildingFloorList) {
			if (map.get("floor").equals("ApiTesingFloor")) {
				floorInfoMap = new HashMap<>(map);
			}
		}
		return floorInfoMap;
	}

	public List<Map<String, Object>> getBuildingFloorList(Integer buildingId) {
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> buildingFloorList = given().pathParam("buildingId", buildingId).when()
				.get("/buildings/{buildingId}/floors").then().assertThat().statusCode(200).extract().body()
				.as(List.class);
		return buildingFloorList;
	}

	public List<Integer> getBuildingIdListRandom(Integer buildingIdNumber) {
		List<Map<String, Object>> itemsArray = this.getBuildingsItemsList();
		List<Integer> buildingID = new ArrayList<Integer>();
		for (Map<String, Object> map : itemsArray) {
			buildingID.add((Integer) map.get("id"));
		}
		Collections.shuffle(buildingID);
		List<Integer> buildingSet = buildingID.subList(0, buildingIdNumber);
		return buildingSet;
	}

	public Integer getBuildingIdRandom() {
		List<Integer> buildingIdList = this.getBuildingsIdList();
		return buildingIdList.get(new Random().nextInt(buildingIdList.size()));
	}

	public List<Integer> getBuildingsIdList() {
		List<Map<String, Object>> buildingsItemsList = this.getBuildingsItemsList();
		List<Integer> buildingsIdList = new ArrayList<>();
		for (Map<String, Object> map : buildingsItemsList) {
			buildingsIdList.add((Integer) map.get("id"));
		}
		return buildingsIdList;

	}

	public List<Integer> getBuildingsIdList(String buildingName) {
		List<Map<String, Object>> buildingsItemsList = this.getBuildingsItemsList();
		List<Integer> buildingsList = new ArrayList<>();
		for (Map<String, Object> map : buildingsItemsList) {
			if (map.get("name").equals(buildingName)) {
				buildingsList.add((Integer) map.get("id"));
			}
		}
		return buildingsList;
	}

	public List<Map<String, Object>> getBuildingsItemsList() {
		List<Map<String, Object>> buildingsItemsList = given().when().get("/buildings").then().assertThat()
				.statusCode(200).extract().path("items");
		return buildingsItemsList;
	}

	public Integer getFloorId(Integer roomCount, Integer buildingId) {
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> floorList = given().pathParam("buildingId", buildingId).when()
				.get("/buildings/{buildingId}/floors").then().assertThat().statusCode(200).extract().body()
				.as(List.class);
		Integer floorId = 0;
		for (Map<String, Object> map : floorList) {
			if ((Boolean) map.get("active") && (Integer) map.get("roomCount") >= roomCount) {
				floorId = (Integer) map.get("id");
				break;
			} else {
				continue;
			}
		}
		return floorId;
	}

	public List<Map<String, Object>> getPermitPriceInfoList(PermitPrice_Info permitPrice_Info) {
		List<Map<String, Object>> permit_priceInfoList = given()
				.pathParam("customerId", permitPrice_Info.getCustomerId())
				.queryParam("buildingIds", permitPrice_Info.getBuildingId()).when().get("/permit-price/{customerId}")
				.then().assertThat().statusCode(200)
				.body(matchesJsonSchemaInClasspath("jsonResources/schema/web/buildings/permit_price_get.json"))
				.extract().body().path("items");
		return permit_priceInfoList;
	}

	public List<Integer> getPortfolioBuildingIdList(String portfolioName) {
		@SuppressWarnings("unchecked")
		List<Integer> actualBuildingIdList = (List<Integer>) this.getPortfolioInfoMap(portfolioName)
				.get("BuildingIdList");
		Collections.sort(actualBuildingIdList);
		return actualBuildingIdList;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getPortfolioInfoMap(String nameOfPortfolio) {
		Map<String, Object> portfolioInfoMap = new HashMap<>();
		List<Integer> BuildingIDList = new ArrayList<>();
		List<String> portfolioNameList = new ArrayList<>();
		Integer portfolioId = null;
		List<Map<String, Object>> items = given().when().post("/buildings/portfolios/list").then().statusCode(200)
				.extract().body().jsonPath().get("items");
		List<Map<String, Object>> buildingsListMap = null;
		for (Map<String, Object> map : items) {
			if (map.get("name").equals(nameOfPortfolio)) {
				buildingsListMap = (List<Map<String, Object>>) map.get("buildings");
				for (Map<String, Object> buildingInfo : buildingsListMap) {
					BuildingIDList.add((Integer) buildingInfo.get("id"));
				}
				portfolioId = (Integer) map.get("id");
			}
			portfolioNameList.add((String) map.get("name"));
		}
		portfolioInfoMap.put("BuildingIdList", BuildingIDList);
		portfolioInfoMap.put("PortfolioId", portfolioId);
		portfolioInfoMap.put("portfolioNameList", portfolioNameList);
		return portfolioInfoMap;
	}

	public List<Integer> getRoomIdList(Integer floorId) {
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> roomList = given().pathParam("floorId", floorId).when()
				.get("/buildings/floors/{floorId}").then().assertThat().statusCode(200).extract().body().as(List.class);
		Map<String, Object> roomInfoMap = roomList.get(new CollectionsSlave().getListIndexRandom(roomList.size()));
		Integer roomId = (Integer) roomInfoMap.get("id");
		List<Integer> roomIdList = new ArrayList<>();
		roomIdList.add(roomId);
		return roomIdList;
	}

	public List<Integer> getSalesmanIdList(Integer buildingId, Integer floorId, List<Integer> roomIdList) {
		Map<String, Object> roomBody = new HashMap<>();
		roomBody.put("buildingId", buildingId);
		roomBody.put("roomsId", roomIdList);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> salsemanList = given().body(roomBody).when().post("/permit-price/users").then()
				.assertThat().statusCode(200).extract().body().as(List.class);
		Map<String, Object> salsemanInfoMap = salsemanList
				.get(new CollectionsSlave().getListIndexRandom(salsemanList.size()));
		List<Integer> salesmanIdList = new ArrayList<>();
		salesmanIdList.add((Integer) salsemanInfoMap.get("id"));
		return salesmanIdList;
	}

	public Map<String, Object> initPortfolioInfoMap(List<Integer> BuildingIDList, String portfolioName) {
		Map<String, Object> buildingsPortfoliosPostBody = new HashMap<String, Object>();
		buildingsPortfoliosPostBody.put("buildingIds", BuildingIDList);
		buildingsPortfoliosPostBody.put("name", portfolioName);
		return buildingsPortfoliosPostBody;
	}

	@SuppressWarnings("unchecked")
	public void verifyPermitPriceBody(List<Map<String, Object>> permit_priceInfoList,
			PermitPrice_Info permitPrice_InfoObject) {
		assertThat(permit_priceInfoList.size(), CoreMatchers.is(1));
		List<Map<String, Object>> actualPermitList = new ArrayList<>();
		for (Map<String, Object> map : permit_priceInfoList) {
			Map<String, Object> permitItem = new HashMap<>();
			permitItem.put("buildingId", map.get("buildingId"));
			permitItem.put("roomIdList", Buildings_Hooks.collectionsSlave
					.getListMapKeyValueList((List<Map<String, Object>>) map.get("rooms"), "id"));
			permitItem.put("price", map.get("price"));
			permitItem.put("priceUnit", map.get("priceUnit"));
			permitItem.put("usersIdList", Buildings_Hooks.collectionsSlave
					.getListMapKeyValueList((List<Map<String, Object>>) map.get("users"), "id"));
			actualPermitList.add(permitItem);
		}
		assertThat(actualPermitList.toString(),
				CoreMatchers.containsString(permitPrice_InfoObject.getPermitPriceInfoMap().toString()));
	}

}
