package io.creams.test.api.web.property;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static io.restassured.RestAssured.given;

public class Property_Flows {

	protected Logger logger = LoggerFactory.getLogger(getClass().getName());
	
	// 获取所有楼宇列表
	@Test
	public List<Integer> getBuilding_List() {
		List<Map<String, Object>> buildingItemsList = given().when().get("/buildings").then().assertThat()
				.statusCode(200).extract().path("items");
		List<Integer> buildingList = new ArrayList<>();
		for (Map<String, Object> building : buildingItemsList) {
			buildingList.add((Integer) building.get("id"));
		}
		System.out.println(buildingList);
		return buildingList;
	}

	// 循环楼宇，获取每个楼宇下的所有房源列表,然后保存房源个数不少于2个的楼宇列表
	public List<Integer> getBuilding_Select_List() {
		List<Integer> buildingList = this.getBuilding_List();
		List<Integer> buildingSelectList = new ArrayList<>();
		List<List<Integer>> roomList = new ArrayList<>();

		for (Integer i = 0; i < buildingList.size(); i++) {
			List<Map<String, Object>> RoomItemsList = given().pathParam("buildingIds", buildingList.get(i))
					.params("roomType", "ALL", "page", 1, "pageSize", 30, "asc", "false").when()
					.get("/buildings/{buildingIds}/rooms/marketing").then().assertThat().statusCode(200).extract()
					.path("items");
			List<Integer> roomId = new ArrayList<>();
			for (Map<String, Object> room : RoomItemsList) {
				roomId.add((Integer) room.get("id"));
			}
			System.out.println(roomId);
			roomList.add(roomId);
			System.out.println(roomList);
			if (roomId.size() >= 2) {
				buildingSelectList.add(buildingList.get(i));
			}
			System.out.println(buildingSelectList);
		}
		return buildingSelectList;
	}

	// 从楼宇列表中随机取1个楼宇
	public Integer getBuildingIdRandom() {
		List<Integer> buildingIdList = this.getBuilding_Select_List();
		Integer buildingIdRandom = buildingIdList.get(new Random().nextInt(buildingIdList.size()));
		System.out.println(buildingIdRandom);
		return buildingIdRandom;

	}

	// 获取某个楼宇下的所有房源
	public List<Integer> getRoomIdList() {
		Integer buildingId = this.getBuildingIdRandom();
		List<Map<String, Object>> RoomItemsList = given().pathParam("buildingIds", buildingId)
				.params("roomType", "ALL", "page", 1, "pageSize", 30, "asc", "false").when()
				.get("/buildings/{buildingIds}/rooms/marketing").then().assertThat().statusCode(200).extract()
				.path("items");
		List<Integer> RoomIdList = new ArrayList<>();
		for (Map<String, Object> map : RoomItemsList) {
			RoomIdList.add((Integer) map.get("id"));
		}
		System.out.println(RoomIdList);
		return RoomIdList;
	}

	// 从所有房源中随机取房源
	public List<Integer> getRoomIdRandom(Integer roomCountNumber) {
		List<Integer> roomList = this.getRoomIdList();
		Collections.shuffle(roomList);
		List<Integer> room = roomList.subList(0, roomCountNumber);
		return room;
	}
	
	//新建水电表传参
	public Map<String,Object> createElectricityMeterInfoMap(String billOther,String name,String presentReading,double price,Integer rate,List<Integer> roomIds,String type){
		Map<String,Object> ElectricityMeterPostBody = new HashMap<String,Object>();
		ElectricityMeterPostBody.put("billOther", billOther);
		ElectricityMeterPostBody.put("name", name);
		ElectricityMeterPostBody.put("presentReading", presentReading);
		ElectricityMeterPostBody.put("price", price);
		ElectricityMeterPostBody.put("rate", rate);
		ElectricityMeterPostBody.put("roomIds", roomIds);
		ElectricityMeterPostBody.put("type", type);
		return ElectricityMeterPostBody;
	}
}





