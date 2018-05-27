package io.creams.test.api.web.roomController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class roomController_Steps<SONArray> extends roomController_Hooks {

	private roomController_Flows roomController_Flows;

	public roomController_Steps() {
		super();
		roomController_Flows = new roomController_Flows();
	}

	@Test
	// 查询所有的楼盘和房源
	public void Rooms_11() {
		List<Map<String, Object>> buildingAndRoomList = given().when().get("/buildings/rooms").then().assertThat()
				.statusCode(200).extract().path("");
		Map<String, Object> roomsMap = buildingAndRoomList.get(0);
		List<Map<String, Object>> rooms = (List<Map<String, Object>>) roomsMap.get("rooms");
		Map<String, Object> buildingAndRoomItemsMaps = rooms.get(new Random().nextInt(rooms.size()));
		JSONObject buildingsAndRoomJson = new JSONObject(buildingAndRoomItemsMaps);
		assertThat(buildingsAndRoomJson.toString(),
				matchesJsonSchemaInClasspath("jsonResources/schema/web/roomController/rooms_get.json"));
	}

	@Test
	// 新建房源
	public void Rooms_2() throws IOException {
		roomController_Hooks.roomController_Info.setBuidingHasRooms(roomController_Flows.getBuildingsIdHasRooms());
		roomController_Hooks.roomController_Info.setfloorName(roomController_Flows.getfloorName());
		String floorName = roomController_Hooks.roomController_Info.getfloorName();
		JsonObject createRooms = roomController_Flows
				.putFloorNameParamToJson("jsonResources/web/roomController/createRoom.json", floorName);
		roomController_Hooks.roomController_Info.setPortfolioID(Integer
				.parseInt(given().pathParam("buildingId", roomController_Hooks.roomController_Info.getBuidingHasRooms())
						.body(createRooms.toString()).when().post("/buildings/{buildingId}/rooms").then().assertThat()
						.statusCode(201).extract().body().asString()));
	}

	@Deprecated
	// 查询楼盘下房源的所有标签
	public void Rooms_21() throws IOException {
		List<Object> tags = given().pathParam("ids", roomController_Hooks.roomController_Info.getBuidingHasRooms())
				.when().get("/buildings/{ids}/rooms/tags").then().assertThat().statusCode(200).extract().path("");
		assertThat(tags.contains("string"), is(true));

	}

	@Test
	// 查询房源的详细信息
	public void Rooms_3() throws IOException {
		Map<String, Object> roomDetailMap = given()
				.pathParam("id", roomController_Hooks.roomController_Info.getRoomId()).when()
				.get("/buildings/rooms/{id}").then().assertThat().statusCode(200).extract().path("");
		JSONObject roomDetailMapJson = new JSONObject(roomDetailMap);
		assertThat(roomDetailMapJson.toString(),
				matchesJsonSchemaInClasspath("jsonResources/schema/web/roomController/rooms_detail.json"));

	}

	@Deprecated
	// 查询房源的详细信息
	public void Rooms_31() throws IOException {
		given().when().get("/buildings/rooms/fitments").then().assertThat().statusCode(200);

	}

	@Test
	// 修改房源
	public void Rooms_4() throws IOException {
		JsonObject modifyRoomsBody = roomController_Flows
				.getRoomsJson("jsonResources/web/roomController/modifyRoom.json");
		given().pathParam("buildingId", roomController_Hooks.roomController_Info.getBuidingHasRooms())
				.pathParam("roomId", roomController_Hooks.roomController_Info.getRoomId())
				.body(modifyRoomsBody.toString()).when().put("/buildings/{buildingId}/rooms/{roomId}").then()
				.assertThat().statusCode(201).extract().body().asString();
	}

	@Test
	// 房源详情，带操作日志
	public void Rooms_5() throws IOException {
		Map<String, Object> roomDetailAndLogMap = given()
				.pathParam("buildingId", roomController_Hooks.roomController_Info.getBuidingHasRooms())
				.pathParam("id", roomController_Hooks.roomController_Info.getRoomId()).when()
				.get("/buildings/{buildingId}/editable/rooms/{id}").then().assertThat().statusCode(200).extract()
				.path("");
		JSONObject roomDetailAndLogMapJson = new JSONObject(roomDetailAndLogMap);
		assertThat(roomDetailAndLogMapJson.toString(),
				matchesJsonSchemaInClasspath("jsonResources/schema/web/roomController/rooms_log.json"));

	}

	@Test
	// 根据可招商状态查询房源
	public void Rooms_6() {
		List<Map<String, Object>> roomItemsList = given()
				.pathParam("buildingIds", roomController_Hooks.roomController_Info.getBuidingHasRooms()).when()
				.get("/buildings/{buildingIds}/rooms/marketing").then().assertThat().statusCode(200).extract()
				.path("items");
		Map<String, Object> AbleHireRoomMap = roomItemsList.get(new Random().nextInt(roomItemsList.size()));
		if (roomItemsList.size() == 0) {

		} else {
			JSONObject AbleHireRoomJson = new JSONObject(AbleHireRoomMap);
			assertThat(AbleHireRoomJson.toString(),
					matchesJsonSchemaInClasspath("jsonResources/schema/web/roomController/rooms_ableHire.json"));
		}
	}

	@Deprecated
	// 查询房源推广信息
	public void Rooms_7() throws IOException {
		given().pathParam("id", roomController_Hooks.roomController_Info.getRoomId()).when()
				.get("/buildings/rooms/{id}/promotions").then().assertThat().statusCode(200);
	}

	@Deprecated
	// queryRoomLogsByRoomId
	public void Rooms_8() throws IOException {
		Map<String, Object> roomLogsMap = given().pathParam("id", roomController_Hooks.roomController_Info.getRoomId())
				.when().get("/buildings/rooms/{id}/logs").then().assertThat().statusCode(200).extract().path("");
		List<Map<String, Object>> items = (List<Map<String, Object>>) roomLogsMap.get("items");
		if (items.size() == 0) {
		} else {
			JSONObject roomLogsJson = new JSONObject(roomLogsMap);
			assertThat(roomLogsJson.toString(),
					matchesJsonSchemaInClasspath("jsonResources/schema/web/roomController/rooms_editLog.json"));
		}
	}

	@Test
	// 删除房源
	public void Rooms_90() throws IOException {
		given().pathParam("buildingId", roomController_Hooks.roomController_Info.getBuidingHasRooms())
				.pathParam("roomId", roomController_Hooks.roomController_Info.getRoomId()).when()
				.delete("/buildings/{buildingId}/rooms/{roomId}").then().assertThat().statusCode(204);

	}

	@Test
	// 修改房源顺序
	public void Rooms_91() throws IOException {
		Gson gson = new Gson();
		roomController_Hooks.roomController_Info.setBuidingHasRooms(roomController_Flows.getBuildingsIdHasRooms());
		roomController_Hooks.roomController_Info.setFloorIdHasRooms(roomController_Flows.getFloorIdHasRooms());
		given().pathParam("floorId", roomController_Flows.getFloorIdHasRooms())
				.body(gson.toJson(roomController_Flows.placeNum())).when().put("/buildings/floor/{floorId}/room/queue")
				.then().assertThat().statusCode(200);

	}

}
