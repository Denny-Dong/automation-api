package io.creams.test.api.web.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class Analysis_Flows {
	protected Logger logger = LoggerFactory.getLogger(getClass().getName());
	
	public Integer getBuildingsId() {
		List<Map<String, Object>> buildingsItemsList = given().when().get("/buildings").then().assertThat()
				.statusCode(200).extract().path("items");
		Integer buildingId = (Integer)buildingsItemsList.get(new Random().nextInt(buildingsItemsList.size())).get("id");
		return buildingId;
	}
}
