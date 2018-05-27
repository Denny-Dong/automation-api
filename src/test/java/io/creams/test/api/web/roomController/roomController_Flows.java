package io.creams.test.api.web.roomController;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.creams.test.api.HooksApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static io.restassured.RestAssured.given;

public class roomController_Flows {
	protected Logger logger = LoggerFactory.getLogger(getClass().getName());

	public JsonObject getRoomsJson(String filepath) throws IOException {
		return HooksApi.driver.jsonObjectProducer(filepath);
	}

	public Map<String, Object> roomPlaceInfoMap(int roomId, int queue) {
		Map<String, Object> roomIdInfo = new HashMap<String, Object>();
		roomIdInfo.put("roomId", roomId);
		roomIdInfo.put("queue", queue);
		return roomIdInfo;

	}

	// 动态获取楼宇id
	public Integer getBuildingsId() {
		List<Map<String, Object>> buildingsItemsList = given().when().get("/buildings").then().assertThat()
				.statusCode(200).extract().path("items");
		Integer buildingId = (Integer) buildingsItemsList.get(new Random().nextInt(buildingsItemsList.size()))
				.get("id");
		return buildingId;
	}

	// 获取 buildingsItemsList
	public List<Map<String, Object>> getBuildingsItemsList() {
		List<Map<String, Object>> buildingsItemsList = given().when().get("/buildings").then().assertThat()
				.statusCode(200).extract().path("items");
		return buildingsItemsList;
	}

	// 随机获取有房源的buildingId
	public Integer getBuildingsIdHasRooms() {
		List<Map<String, Object>> buildingsItemsList = this.getBuildingsItemsList();
		List<Integer> buildingsIdList = new ArrayList<>();
		Integer buildingHasRoomsId = null;
		for (Map<String, Object> map : buildingsItemsList) {
			if (((Float) map.get("buildingAreaSize") >= 500)) {
				buildingsIdList.add((Integer) map.get("id"));
				buildingHasRoomsId = buildingsIdList.get(new Random().nextInt(buildingsIdList.size()));
				break;
			}
		}
		return buildingHasRoomsId;
	}

	// 查找至少两个房源的楼层
	public Integer getFloorIdHasRooms() {

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> buildingFloorList = given()
				.pathParam("buildingId", roomController_Hooks.roomController_Info.getBuidingHasRooms()).when()
				.get("/buildings/{buildingId}/floors").then().assertThat().statusCode(200).extract().body()
				.as(List.class);
		List<Integer> floorList = new ArrayList<>();
		Integer floorId = null;
		for (Map<String, Object> map : buildingFloorList) {
			if ((Integer) map.get("roomCount") >= 2) {
				floorList.add((Integer) map.get("id"));
				floorId = floorList.get(new Random().nextInt(floorList.size()));
				break;
			}
		}
		return floorId;

	}

	// 获取指定楼层下的房源id
	@SuppressWarnings("unchecked")
	public List<Integer> roomIdList() {
		List<Map<String, Object>> roomList = given()
				.pathParam("floorId", roomController_Hooks.roomController_Info.getFloorIdHasRooms()).when()
				.get("/buildings/floors/{floorId}").then().assertThat().statusCode(200).extract().body().as(List.class);
		List<Integer> roomIdslist = new ArrayList<>();
		for (Map<String, Object> map : roomList) {
			roomIdslist.add((Integer) map.get("id"));
		}
		Collections.shuffle(roomIdslist);
		return roomIdslist;
	}

	// 获取指定楼宇下的楼层名，并且随便选一个
	public String getfloorName() {
		List<Map<String, Object>> floorList = given()
				.pathParam("buildingId", roomController_Hooks.roomController_Info.getBuidingHasRooms()).when()
				.get("/buildings/{buildingId}/floors").then().assertThat().statusCode(200).extract().body()
				.as(List.class);
		List<String> floorsList = new ArrayList<>();
		String floorName = null;
		for (Map<String, Object> map : floorList) {
			floorsList.add((String) map.get("floor"));
			floorName = floorsList.get(new Random().nextInt(floorsList.size()));
			break;
		}
		return floorName;

	}

	// 传floorName
	public JsonObject putFloorNameParamToJson(String filepath, String floorName) throws IOException {
		JsonObject createRoomBody = this.getRoomsJson(filepath);
		JsonArray createRooms = new JsonArray();
		createRooms.add(floorName);
		createRoomBody.addProperty("floorName", floorName);
		return createRoomBody;
	}

	// 封装List数据
	public Object placeNum() {
		ArrayList<Object> roomPlaceList = new ArrayList<>();
		List<Integer> roomId = this.roomIdList();
		Integer quenum = 0;
		for (Integer roomid : roomId) {
			roomPlaceList.add(roomPlaceInfoMap(roomid, quenum++));
		}

		return roomPlaceList;

	}

}
