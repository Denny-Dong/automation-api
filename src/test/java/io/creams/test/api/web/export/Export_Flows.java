package io.creams.test.api.web.export;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class Export_Flows {

	public List<Integer> getBuildingIdList() {
		@SuppressWarnings("unchecked")
		Map<String, Object> userMap = (Map<String, Object>) Export_Hooks.userAccount.getUserInfoMap().get("user");
		@SuppressWarnings("unchecked")
		List<Integer> buildingIdList = (List<Integer>) userMap.get("buildingIdList");
		return buildingIdList;
	}

	public List<Integer> getRegionList() {
		List<Map<String, Object>> regionlist = given().when().get("/marketing/channel/contacts/regions").then()
				.assertThat().statusCode(200).extract().path("");
		List<Integer> regionIds = new ArrayList<Integer>();
		for (Map<String, Object> regionMap : regionlist) {
			regionIds.add((Integer) (regionMap.get("blockId")));
		}
		return regionIds;
	}

}
