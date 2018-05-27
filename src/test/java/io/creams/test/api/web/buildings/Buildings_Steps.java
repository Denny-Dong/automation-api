package io.creams.test.api.web.buildings;

import org.hamcrest.CoreMatchers;
import org.hamcrest.collection.IsIn;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.*;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Buildings_Steps extends Buildings_Hooks {

	private Buildings_Flows buildings_Flows;

	public Buildings_Steps() {
		buildings_Flows = new Buildings_Flows();
	}


	@Test
	// 根据Ids查询楼
	public void batch() {
		String subBuildingsIdsString = Buildings_Hooks.collectionsSlave
				.getIntListItemRandom(buildings_Flows.getBuildingsIdList());
		List<Map<String, Object>> buildingItemList = given().pathParam("ids", subBuildingsIdsString).when()
				.get("/batch/{ids}").then().assertThat().statusCode(200).extract().body().path("items");
		Buildings_Hooks.collectionsSlave.verifySubItemListRandom(buildingItemList,
				"jsonResources/schema/web/buildings/batch_get.json");

	}

	@Test
	// 查询楼列表
	public void buildings_a() {
		List<Map<String, Object>> buildingsItemsList = buildings_Flows.getBuildingsItemsList();
		collectionsSlave.verifySubItemListRandom(buildingsItemsList,
				"jsonResources/schema/web/buildings/buildings_get_items.json");
	}

	@Test
	// 删除楼盘
	public void buildings_b() {
		List<Integer> buildingsList = buildings_Flows.getBuildingsIdList("测试大楼");
		for (Integer integer : buildingsList) {
			given().pathParam("id", integer).when().delete("/buildings/{id}").then().assertThat().statusCode(204);
		}
	}

	@Test
	// 创建楼盘集合
	public void buildings_portfolios_a() {
		// Random get 5 Buildings ID list for portfolio
		Buildings_Hooks.portfolio_Info.setBuildingIDList(buildings_Flows.getBuildingIdListRandom(3));
		// Initialize post body
		Map<String, Object> buildingsPortfoliosPostBody = buildings_Flows.initPortfolioInfoMap(
				Buildings_Hooks.portfolio_Info.getBuildingIDList(), Buildings_Hooks.portfolio_Info.getPortfolioName());
		// Send post message
		given().body(buildingsPortfoliosPostBody).when().post("/buildings/portfolios").then().assertThat()
				.statusCode(201);
	}

	@Test
	// 查询楼盘集合
	public void buildings_portfolios_b() {

		// get actual BuildingIdList and verification
		List<Integer> actualBuildingIdList = buildings_Flows
				.getPortfolioBuildingIdList(Buildings_Hooks.portfolio_Info.getPortfolioName());
		Collections.sort(actualBuildingIdList);
		Buildings_Hooks.portfolio_Info.setPortfolioID((Integer) buildings_Flows
				.getPortfolioInfoMap(Buildings_Hooks.portfolio_Info.getPortfolioName()).get("PortfolioId"));
		assertThat(actualBuildingIdList, CoreMatchers.is(Buildings_Hooks.portfolio_Info.getBuildingIDList()));

	}

	@Test
	// 修改楼盘集合
	public void buildings_portfolios_c() {
		List<Integer> expectedBuildingIDList = Buildings_Hooks.portfolio_Info.getBuildingIDList();
		expectedBuildingIDList.remove(expectedBuildingIDList.get(new Random().nextInt(expectedBuildingIDList.size())));
		Map<String, Object> portfolioBody = buildings_Flows.initPortfolioInfoMap(expectedBuildingIDList,
				Buildings_Hooks.portfolio_Info.getPortfolioName());
		given().body(portfolioBody).pathParam("id", Buildings_Hooks.portfolio_Info.getPortfolioID()).when()
				.put("/buildings/portfolios/{id}").then().assertThat().statusCode(201);
		List<Integer> actualBuildingIdList = buildings_Flows
				.getPortfolioBuildingIdList(Buildings_Hooks.portfolio_Info.getPortfolioName());
		assertThat(actualBuildingIdList, CoreMatchers.is(expectedBuildingIDList));

	}

	@Test
	// 删除楼盘集合
	public void buildings_portfolios_d() {
		given().pathParam("id", Buildings_Hooks.portfolio_Info.getPortfolioID()).when()
				.delete("/buildings/portfolios/{id}").then().assertThat().statusCode(204);
		@SuppressWarnings("unchecked")
		List<String> actualPortfolioNameList = (List<String>) buildings_Flows
				.getPortfolioInfoMap(Buildings_Hooks.portfolio_Info.getPortfolioName()).get("portfolioNameList");
		assertThat(actualPortfolioNameList,
				CoreMatchers.not(CoreMatchers.hasItem(Buildings_Hooks.portfolio_Info.getPortfolioName())));

	}

	@Deprecated
	// 获取楼盘下所有合同模板列表
	public void buildingsBuildingIdContractTemplates() {
		List<Integer> buildingIdList = buildings_Flows.getBuildingsIdList();
		Integer buildingId = buildingIdList.get(new Random().nextInt(buildingIdList.size()));
		given().pathParam("buildingId", buildingId).queryParam("isProperty", true).when()
				.get("/buildings/{buildingId}/contract-templates").then().assertThat().statusCode(200);
	}

	@Deprecated
	// 楼盘详情右侧栏统计
	public void buildingsChart_data() {
		given().pathParam("id", buildings_Flows.getBuildingIdRandom()).when().get("/buildings/{id}/chart-data").then()
				.assertThat().statusCode(400);

	}

	@Test
	// @Deprecated
	// 图表:每月成交面积(数量/面积)
	public void buildingsChartsContracts() {
		String idString = Buildings_Hooks.collectionsSlave.getIntListItemRandom(buildings_Flows.getBuildingsIdList());
		given().pathParam("ids", idString).when().get("/buildings/{ids}/charts/contracts").then().assertThat()
				.statusCode(404);

	}

	@Test
	// @Deprecated
	// 图表:每月租金均价
	public void buildingsChartsContractsPrice() {
		String idString = Buildings_Hooks.collectionsSlave.getIntListItemRandom(buildings_Flows.getBuildingsIdList());
		given().pathParam("ids", idString).when().get("/buildings/{ids}/charts/contracts/price").then().assertThat()
				.statusCode(404);
	}

	@Test
	// 当前公司可建楼盘限制
	public void buildingsConfigurationLimitation() {
		given().when().get("/buildings/configuration/limitation").then().assertThat().statusCode(200)
				.body(matchesJsonSchemaInClasspath(
						"jsonResources/schema/web/buildings/buildingsConfigurationLimitation_get.json"));
	}

	@Test
	// 查询楼盘的楼层列表
	public void buildingsFloors_a() {
		floor_Info.setBuildingId(buildings_Flows.getBuildingIdRandom());
		floor_Info.setFloorInfoList(buildings_Flows.getBuildingFloorList(floor_Info.getBuildingId()));
		Map<String, Object> buildingFloorMap = Buildings_Hooks.collectionsSlave
				.getListItemRandom(floor_Info.getFloorInfoList());
		Buildings_Hooks.collectionsSlave.verifyMap(buildingFloorMap,
				"jsonResources/schema/web/buildings/buildings_buildingId_floors_get.json");
	}

	@Test
	// 创建楼层
	public void buildingsFloors_b() {
		List<Map<String, Object>> newbuildingFloorList = new ArrayList<>(floor_Info.getFloorInfoList());
		Map<String, Object> newBuildingFloorMap = new HashMap<>();
		newBuildingFloorMap.put("active", true);
		newBuildingFloorMap.put("areaSize", 0);
		newBuildingFloorMap.put("floor", "ApiTesingFloor");
		newBuildingFloorMap.put("queue", newbuildingFloorList.size());
		newbuildingFloorList.add(newBuildingFloorMap);
		given().pathParam("buildingId", floor_Info.getBuildingId()).body(newbuildingFloorList).when()
				.post("/buildings/{buildingId}/floors").then().assertThat().statusCode(201);
		Map<String, Object> actualBuildingFloorInfoMap = new HashMap<>(buildings_Flows
				.getBuildingFloorInfoMap(buildings_Flows.getBuildingFloorList(floor_Info.getBuildingId())));
		assertThat(newBuildingFloorMap.entrySet(),
				CoreMatchers.everyItem(IsIn.isIn(actualBuildingFloorInfoMap.entrySet())));
	}

	@Test
	// 删除楼层
	public void buildingsFloors_c() {
		List<Map<String, Object>> recoverBuildingFloorList = buildings_Flows
				.getBuildingFloorList(floor_Info.getBuildingId());
		for (Map<String, Object> map : recoverBuildingFloorList) {
			if (map.get("floor").equals("ApiTesingFloor")) {
				map.put("active", false);
			}
		}
		given().pathParam("buildingId", floor_Info.getBuildingId()).body(recoverBuildingFloorList).when()
				.post("/buildings/{buildingId}/floors").then().assertThat().statusCode(201);
		List<Map<String, Object>> actualBuildingFloorList = buildings_Flows
				.getBuildingFloorList(floor_Info.getBuildingId());
		assertThat(actualBuildingFloorList, CoreMatchers.is(floor_Info.getFloorInfoList()));
	}

	@Test
	// 创建权限单价
	public void permit_price_a() {
		given().body(permitPrice_Info.getPermitPriceBody()).when().post("/permit-price").then().assertThat()
				.statusCode(201);
	}

	@Test
	// 查询权限单价列表
	public void permit_price_b() {
		List<Map<String, Object>> permit_priceInfoList = buildings_Flows
				.getPermitPriceInfoList(Buildings_Hooks.permitPrice_Info);
		permitPrice_Info.setPermitPriceId((Integer) permit_priceInfoList.get(0).get("id"));
		buildings_Flows.verifyPermitPriceBody(permit_priceInfoList, Buildings_Hooks.permitPrice_Info);
	}

	@Test
	// 修改权限单价
	public void permit_price_c() {
		PermitPrice_Info modifiedPermitPrice = permitPrice_Info.clone();
		Map<String, Object> modifiedPermitPriceBodyMap = modifiedPermitPrice.getPermitPriceBody();
		modifiedPermitPriceBodyMap.put("price", 16.66);
		modifiedPermitPrice.setPrice(16.66);
		given().pathParam("id", modifiedPermitPrice.getPermitPriceId()).body(modifiedPermitPriceBodyMap).when()
				.put("/permit-price/{id}").then().statusCode(200);
		List<Map<String, Object>> permit_priceInfoList = buildings_Flows.getPermitPriceInfoList(modifiedPermitPrice);
		buildings_Flows.verifyPermitPriceBody(permit_priceInfoList, modifiedPermitPrice);
		Buildings_Hooks.permitPrice_Info = modifiedPermitPrice;
	}

	@Test
	// 权限单价详情
	public void permit_price_d() {
		given().pathParam("id", permitPrice_Info.getPermitPriceId()).when().get("/permit-price/{id}/detail").then()
				.assertThat().statusCode(200)
				.body(matchesJsonSchemaInClasspath("jsonResources/schema/web/buildings/permit_price_detail_get.json"));
	}

	@Test
	// 删除权限单价
	public void permit_price_e() {
		given().pathParam("id", permitPrice_Info.getPermitPriceId()).when().delete("/permit-price/{id}").then()
				.statusCode(204);
	}

}
