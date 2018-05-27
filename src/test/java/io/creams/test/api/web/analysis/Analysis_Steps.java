package io.creams.test.api.web.analysis;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class Analysis_Steps extends Analysis_Hooks{
	private Analysis_Flows analysis_Flows;

	public Analysis_Steps() {
		super();
		analysis_Flows = new Analysis_Flows();
	}
	
	//@Test
	//每月退租面积、企业数
	public void analysis_tenants_checkout() {
		Integer id = analysis_Flows.getBuildingsId();
		given().pathParam("buildingIds",id).when().get("/analysis/buildings/{buildingIds}/charts/tenants/checkout").
		then().assertThat().statusCode(200).body(matchesJsonSchemaInClasspath("jsonResources/schema/web/analysis/checkout_get_items.json"));
	}
	
	@Test
	//在租合来租金月单价走势图
	public void analysis_payments_priceChart(){
		Integer buildingId = analysis_Flows.getBuildingsId();
		long id  = Long.valueOf(buildingId).longValue();
		given().pathParam("buildingId", id)
		       .queryParam("startDate", "2017-01-01")
		       .queryParam("endDate", "2018-12-30").
		  when().get("/analysis/buildings/{buildingId}/contracts/payments/price-chart").
		then().assertThat().statusCode(200).body(matchesJsonSchemaInClasspath("jsonResources/schema/web/analysis/price_get_items.json"));
	}
	
	@Test
	//图表:退租原因占比
	public void analaysis_checkout_reasons() {
		String buildingId = analysis_Flows.getBuildingsId().toString();
		given().pathParam("buildingIds", buildingId).when().get("/analysis/v2/buildings/{buildingIds}/charts/tenants/checkout/reasons").
		then().assertThat().statusCode(200);
	}
	
	@Test
	//图表：入驻企业行业（数量、面积）
	public void analysis_tenants_industries() {
		String buildingId = analysis_Flows.getBuildingsId().toString();
		given().pathParam("buildingIds", buildingId).
		when().get("/analysis/v2/buildings/{buildingIds}/charts/tenants/industries").then().assertThat().statusCode(200).body(matchesJsonSchemaInClasspath("jsonResources/schema/web/analysis/industries_get_items.json"));
	}
	
	@Test
	//图表: 退租行业企业数占比
	public void analysis_industry_termination_count() {
		String buildingId = analysis_Flows.getBuildingsId().toString();
		given().pathParam("buildingIds", buildingId).
		when().get("/analysis/v2/buildings/{buildingIds}/industry-termination-count").
		then().assertThat().statusCode(200);
	}
	
	@Test
	//每月成交面积/企业数明细查询
	public void analysis_list_contracts() {
		String buildingId = analysis_Flows.getBuildingsId().toString();
		given().pathParam("buildingIds", buildingId)
		       .queryParam("startDate", "2017-01-01")
		       .queryParam("endDate", "2018-12-30").
		when().get("/analysis/v2/buildings/{buildingIds}/list/contracts").
		then().assertThat().statusCode(200);
	}
	
	@Ignore
	//入驻企业数行业导出EXCEL
	public void analysis_tenantsNum_industries() {
		String buildingId = analysis_Flows.getBuildingsId().toString();
		given().pathParam("buildingIds", buildingId).
		when().get("/analysis/v2/buildings/{buildingIds}/list/tenantsNum/industries").
		then().assertThat().statusCode(200);
	}
	
	@Ignore
	//入驻企业行业面积占比导出EXCEL
	public void analysis_TenantsRoomArea_industries() {
		//String buildingId = analysis_Flows.getBuildingsId().toString();
		given().pathParam("buildingIds", 3675).
		when().get("/analysis/v2/buildings/{buildingIds}/list/tenantsRoomArea/industries").
		then().assertThat().statusCode(200);
	}
	
	//@Test
	//每月成交面积/企业数
	public void analysis_charts_contracts() {
		String buildingId = analysis_Flows.getBuildingsId().toString();
		given().pathParam("ids", buildingId).
		when().get("/analysis/v2/buildings/{ids}/charts/contracts").
		then().assertThat().statusCode(200).body(matchesJsonSchemaInClasspath("jsonResources/schema/web/analysis/contracts_get_items.json"));
	}
	
	@Test
	//图表:出租率空置率
	public void analysis_charts_rooms() {
		String buildingId = analysis_Flows.getBuildingsId().toString();
		given().pathParam("ids", buildingId).
		when().get("/analysis/v2/buildings/{ids}/charts/rooms").
		then().assertThat().statusCode(200).body(matchesJsonSchemaInClasspath("jsonResources/schema/web/analysis/rooms_get_items.json"));
	}
	
	@Ignore
	//成交面积列表EXCEL
	public void analysis_excel_contractWithMakeList() {
		long buildingId = Long.parseLong(analysis_Flows.getBuildingsId().toString());
		given().queryParam("buildingIds", buildingId).
		       queryParam("startDate", "2017-01-01").
		       queryParam("endDate", "2018-12-30").
		when().get("/analysis/v2/excel/contractWithMakeList").
		then().assertThat().statusCode(200);
	}
	
	@Ignore
	//退租企业数行业占比EXCEL
	public void analysis_excel_industry() {
		long buildingId = Long.parseLong(analysis_Flows.getBuildingsId().toString());
		given().queryParam("buildingIds", buildingId).
		when().get("/analysis/v2/excel/industry-termination-contracts").
		then().assertThat().statusCode(200);
	}
	
	//@Test
	//退租原因分析EXCEL
	public void analysis_excel_checkout() {
		long buildingId = Long.parseLong(analysis_Flows.getBuildingsId().toString());
		System.out.println(buildingId);
		given().queryParam("buildingIds", buildingId).
		when().get("/analysis/{buildingIds}/excel/tenants/checkout").
		then().assertThat().statusCode(200);
	}
	
	@Test
	//楼盘顶部数据
	public void analysis_statistics() {
		String buildingId = analysis_Flows.getBuildingsId().toString();		
		List<Map<String,Object>> statisticsList = given().pathParam("buildingIds", buildingId).
				when().get("/statistics/buildings/{buildingIds}").
				then().assertThat().statusCode(200).extract().path("statistics");
		Map<String,Object> statistiscMap = statisticsList.get(new Random().nextInt(statisticsList.size()));
		JSONObject statisticsJson = new JSONObject(statistiscMap);
		Assert.assertThat(statisticsJson.toString(), matchesJsonSchemaInClasspath("jsonResources/schema/web/analysis/statistics_get_items.json"));
	}
}
