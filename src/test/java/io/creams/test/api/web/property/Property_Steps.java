package io.creams.test.api.web.property;

import org.junit.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class Property_Steps extends Property_Hooks{

	private Property_Flows property_Flows;
	
	public Property_Steps(){
		super();
		property_Flows = new Property_Flows();
	}
	
	
	@Test
	//新建水电表
	public void Property_Electricity_Meter_a(){
		Property_Hooks.property_Info.setRoomId(property_Flows.getRoomIdRandom(2)); 
		System.out.println(Property_Hooks.property_Info.getRoomId());
		Map<String,Object> electricityMeterPostBody = property_Flows.createElectricityMeterInfoMap("APITestBillOther", "APITestName", "100", 5.00, 1, Property_Hooks.property_Info.getRoomId(), "ELECTRICITY");
		given().body(electricityMeterPostBody).when().post("/property/meter").then().assertThat().statusCode(201);
	}
	
}


