package io.creams.test.api.web.propertyContracts;

import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

public class PropertyContracts_Steps extends PropertyContracts_Hooks {

	private PropertyContracts_Flows propertyContracts_Flows;

	public PropertyContracts_Steps() {
		super();
		propertyContracts_Flows = new PropertyContracts_Flows();
	}

	@Test
	// 新建物业合同
	public void propertyContract_a() throws IOException {
		List<Integer> buildingIdList = propertyContracts_Flows.getBuildingIdList();
		Integer buildingId = buildingIdList.get(new Random().nextInt(buildingIdList.size()));
		int roomId = propertyContracts_Flows.getRooms(buildingId);
		Map<String, Object> tenant = propertyContracts_Flows.getTenant();
		PropertyContracts_Hooks.propertyContract_Info.setTenantId((Integer) tenant.get("id"));
		PropertyContracts_Hooks.propertyContract_Info.setTenantName((String) tenant.get("name"));
		PropertyContracts_Hooks.propertyContract_Info.setBuildingID(buildingId);
		PropertyContracts_Hooks.propertyContract_Info.setRoomID(roomId);
		JsonObject createPropertyContractBody = propertyContracts_Flows.putRoomAndTenantParamToJson(
				"jsonResources/web/propertyContract/createPropertyContract.json", roomId,
				PropertyContracts_Hooks.propertyContract_Info.getTenantId(),
				PropertyContracts_Hooks.propertyContract_Info.getTenantName());
		PropertyContracts_Hooks.propertyContract_Info.setPortfolioID(Integer
				.parseInt(given().body(createPropertyContractBody.toString()).when().post("/property/v2/contracts")
						.then().assertThat().statusCode(201).extract().body().asString()));
		propertyContracts_Flows.initiateApproval(propertyContracts_Flows.getUserId(),
				PropertyContracts_Hooks.propertyContract_Info.getPropertyContractID(),
				PropertyContracts_Hooks.propertyContract_Info.getBuildingID(), "NEW_APPROVAL", "PROPERTY_CONTRACT",
				"自动化测试");
		propertyContracts_Flows.instances("APPROVED", "自动化测试");
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	@Test
	// 物业合同租金明细列表导出
	public void propertyContract_b() {
		String responseBody = given()
				.pathParam("contractId", PropertyContracts_Hooks.propertyContract_Info.getPropertyContractID()).when()
				.get("/export/property/v2/contracts/{contractId}").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 查询物业合同列表
	public void propertyContract_c() {
		List<Map<String, Object>> propertyContractItemsList = given()
				.param("buildingIds", PropertyContracts_Hooks.propertyContract_Info.getBuildingID()).when()
				.get("/property/v2/contracts").then().assertThat().statusCode(200).extract().path("items");
		Map<String, Object> propertyContractItemsMaps = propertyContractItemsList
				.get(new Random().nextInt(propertyContractItemsList.size()));
		JSONObject buildingsItemsJson = new JSONObject(propertyContractItemsMaps);
		assertThat(buildingsItemsJson.toString(), matchesJsonSchemaInClasspath(
				"jsonResources/schema/web/propertyContract/propertyContract_list_get.json"));

	}

	@Test
	// 租金报表明细生成
	public void rentReportDetails() throws IOException {
		JsonObject rentReportDetailsBody = propertyContracts_Flows
				.getPropertyContractParaJson("jsonResources/web/propertyContract/rentReportDetails.json");
		List<Map<String, Object>> rentReportDetailsList = given().body(rentReportDetailsBody.toString()).when()
				.post("/property/v2/contracts/pays").then().assertThat().statusCode(200).extract().path("");
		Map<String, Object> rentReportDetailsMaps = rentReportDetailsList
				.get(new Random().nextInt(rentReportDetailsList.size()));
		JSONObject schmaRentReportDetails = new JSONObject(rentReportDetailsMaps);
		assertThat(schmaRentReportDetails.toString(),
				matchesJsonSchemaInClasspath("jsonResources/schema/web/propertyContract/rentDetails.json"));
	}

	@Test
	// 物业合同详情查询
	public void propertyContract_d() {
		Map<String, Object> baseTerm = given()
				.pathParam("contractId", PropertyContracts_Hooks.propertyContract_Info.getPropertyContractID()).when()
				.get("/property/v2/contracts/{contractId}").then().assertThat().statusCode(200).extract()
				.path("baseTerm");
		JSONObject baseTermJson = new JSONObject(baseTerm);
		assertThat(baseTermJson.toString(),
				matchesJsonSchemaInClasspath("jsonResources/schema/web/propertyContract/propertyContract_info.json"));

	}

	@Test
	// 修改物业合同
	public void propertyContract_e() throws IOException {
		JsonObject modifyPropertyContractBody = propertyContracts_Flows.putRoomAndTenantParamToJson(
				"jsonResources/web/propertyContract/createPropertyContract.json",
				PropertyContracts_Hooks.propertyContract_Info.getRoomID(),
				PropertyContracts_Hooks.propertyContract_Info.getTenantId(),
				PropertyContracts_Hooks.propertyContract_Info.getTenantName());

		given().pathParam("contractId", PropertyContracts_Hooks.propertyContract_Info.getPropertyContractID())
				.body(modifyPropertyContractBody.toString()).when().put("/property/v2/contracts/{contractId}").then()
				.assertThat().statusCode(202);
	}

	@Test
	// 计算物业合同退租协议所需金额
	public void propertyContract_f() {
		Map<String, Object> withdrawalAmountMap = given()
				.pathParam("contractId", PropertyContracts_Hooks.propertyContract_Info.getPropertyContractID())
				.queryParam("terminationDate", collectionsSlave.getCurrentDay()).when()
				.get("/property/v2/contracts/{contractId}/termination/finance/term").then().assertThat().statusCode(200)
				.extract().path("depositIn");
		JSONObject withdrawalAmountJson = new JSONObject(withdrawalAmountMap);
		assertThat(withdrawalAmountJson.toString(),
				matchesJsonSchemaInClasspath("jsonResources/schema/web/propertyContract/withdrawalAmount.json"));
	}

	@Test
	// 创建物业合同退租协议
	public void propertyContract_g() throws IOException {
		JsonObject withdrawalPropertyContractBody = propertyContracts_Flows
				.getPropertyContractParaJson("jsonResources/web/propertyContract/withdrawalPropertyContract.json");
		PropertyContracts_Hooks.propertyContract_Info.setTerminationTermId(Integer.parseInt(
				given().pathParams("contractId", PropertyContracts_Hooks.propertyContract_Info.getPropertyContractID())
						.queryParam("buildingId", PropertyContracts_Hooks.propertyContract_Info.getBuildingID())
						.body(withdrawalPropertyContractBody.toString()).when()
						.post("/property/v2/contracts/{contractId}/termination").then().assertThat().statusCode(201)
						.extract().body().asString()));
		propertyContracts_Flows.initiateApproval(propertyContracts_Flows.getUserId(),
				PropertyContracts_Hooks.propertyContract_Info.getTerminationTermId(),
				PropertyContracts_Hooks.propertyContract_Info.getBuildingID(), "TERMINATION_APPROVAL",
				"PROPERTY_CONTRACT", "自动化测试");
		propertyContracts_Flows.instances("DISAPPROVED", "自动化测试");
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	@Test
	// 查询物业合同退租协议
	public void propertyContract_h() {
		given().pathParams("contractId", PropertyContracts_Hooks.propertyContract_Info.getPropertyContractID(),
				"terminationTermId", PropertyContracts_Hooks.propertyContract_Info.getTerminationTermId()).when()
				.get("/property/v2/contracts/{contractId}/termination/{terminationTermId}").then().assertThat()
				.statusCode(200)
				.body(matchesJsonSchemaInClasspath("jsonResources/schema/web/propertyContract/searchWithdrawal.json"));
	}

	@Test
	// 更新物业合同退租协议
	public void propertyContract_i() throws IOException {
		JsonObject updateWithdrawalPropertyContractBody = propertyContracts_Flows.putTenantNameToJson(
				"jsonResources/web/propertyContract/updateWithdrawalPropertyContract.json",
				PropertyContracts_Hooks.propertyContract_Info.getTenantName());
		given().pathParams("contractId", PropertyContracts_Hooks.propertyContract_Info.getPropertyContractID(),
				"terminationTermId", PropertyContracts_Hooks.propertyContract_Info.getTerminationTermId())
				.queryParam("buildingId", PropertyContracts_Hooks.propertyContract_Info.getBuildingID())
				.body(updateWithdrawalPropertyContractBody.toString()).when()
				.put("/property/v2/contracts/{contractId}/termination/{terminationTermId}").then().assertThat()
				.statusCode(202);
	}

	@Test
	// 作废物业合同
	public void propertyContract_j() {
		Map<String, Object> invalidInfoPostBody = propertyContracts_Flows.invalidInfoMap("CLOSE_ALL_BILLS", "string");
		PropertyContracts_Hooks.propertyContract_Info.setNullificationTermId(Integer.parseInt(
				given().pathParam("contractId", PropertyContracts_Hooks.propertyContract_Info.getPropertyContractID())
						.queryParam("buildingId", PropertyContracts_Hooks.propertyContract_Info.getBuildingID())
						.body(invalidInfoPostBody).when().post("/property/v2/contracts/{contractId}/nullification")
						.then().assertThat().statusCode(201).extract().body().asString()));
	}

	@Test
	// 查询物业合同作废协议
	public void propertyContract_k() {
		given().pathParams("contractId", PropertyContracts_Hooks.propertyContract_Info.getPropertyContractID(),
				"nullificationTermId", PropertyContracts_Hooks.propertyContract_Info.getNullificationTermId()).when()
				.get("/property/v2/contracts/{contractId}/nullification/{nullificationTermId}").then().assertThat()
				.statusCode(200).body(matchesJsonSchemaInClasspath(
						"jsonResources/schema/web/propertyContract/searchInvalidProperty.json"));
		propertyContracts_Flows.initiateApproval(propertyContracts_Flows.getUserId(),
				PropertyContracts_Hooks.propertyContract_Info.getNullificationTermId(),
				PropertyContracts_Hooks.propertyContract_Info.getBuildingID(), "NULLIFICATION_APPROVAL",
				"PROPERTY_CONTRACT", "自动化测试");
		propertyContracts_Flows.instances("APPROVED", "自动化测试");
	}

}
