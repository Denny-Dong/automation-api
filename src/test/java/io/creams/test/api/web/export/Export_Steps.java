package io.creams.test.api.web.export;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Export_Steps extends Export_Hooks {

	private Export_Flows export_Flows;

	public Export_Steps() {
		super();
		export_Flows = new Export_Flows();
	}

	@Test
	// 收入摊销(按合同分析)报表导出
	public void a_incomeAmortizationExport() {
		List<Integer> buildingIdList = export_Flows.getBuildingIdList();
		Export_Hooks.export_Info.setBuildingID(buildingIdList.get(new Random().nextInt(buildingIdList.size())));
		String responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "beginDate", "2018-01-01",
						"endDate", "2018-12-31", "sort", "MONTH_PRIME_AMOUNT_ASC", "sortDate", "2018-02-01", "keyword",
						"接口自动化")
				.when().get("/export/assets/contracts/month").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 到期续租每月合同列表报表导出
	public void monthTerminationRenewExport() {
		String responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "beginDate", "2018-01-01",
						"endDate", "2018-12-31", "type", "AREA_SIZE")
				.when().get("/export/assets/contracts/month/termination/renew").then().assertThat().statusCode(200)
				.extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
		responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "beginDate", "2018-01-01",
						"endDate", "2018-12-31", "type", "STATION_COUNT")
				.when().get("/export/assets/contracts/month/termination/renew").then().assertThat().statusCode(200)
				.extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
		responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "beginDate", "2018-01-01",
						"endDate", "2018-12-31", "type", "CONTRACT_COUNT")
				.when().get("/export/assets/contracts/month/termination/renew").then().assertThat().statusCode(200)
				.extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
		responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "beginDate", "2018-01-01",
						"endDate", "2018-12-31", "type", "RENEW_RATE")
				.when().get("/export/assets/contracts/month/termination/renew").then().assertThat().statusCode(200)
				.extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 租金分布图(所有价格)报表导出
	public void contractsPriceTotalExport() {
		String responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "spaceUnit", "P", "priceUnit",
						"D")
				.when().get("/export/assets/contracts/price/total").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));

		responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "spaceUnit", "P", "priceUnit",
						"M")
				.when().get("/export/assets/contracts/price/total").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));

		responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "spaceUnit", "G", "priceUnit",
						"GD")
				.when().get("/export/assets/contracts/price/total").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));

		responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "spaceUnit", "G", "priceUnit",
						"GM")
				.when().get("/export/assets/contracts/price/total").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));

		responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "spaceUnit", "", "priceUnit",
						"YM")
				.when().get("/export/assets/contracts/price/total").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));

		responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "spaceUnit", "", "priceUnit",
						"YD")
				.when().get("/export/assets/contracts/price/total").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 中途退租统计报表导出
	public void monthTerminationExport() {
		String responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "spaceUnit", "P", "year", "2018")
				.when().get("/export/assets/month/termination").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));

		responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "spaceUnit", "", "year", "2017")
				.when().get("/export/assets/month/termination").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));

		responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "spaceUnit", "G", "year", "2016")
				.when().get("/export/assets/month/termination").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 中途退租统计详情报表导出
	public void monthTerminationDetailExport() {
		String responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "spaceUnit", "P", "year", "2018",
						"date", "2018-02-01", "properties",
						"tenantName,buildingName,roomNumbers,areaSize,price,beginDate,terminationDate,follow")
				.when().get("/export/assets/month/termination/detail").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
		responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "spaceUnit", "", "year", "2018",
						"date", "2018-02-01", "properties",
						"tenantName,buildingName,roomNumbers,areaSize,price,beginDate,terminationDate,follow")
				.when().get("/export/assets/month/termination/detail").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
		responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "spaceUnit", "G", "year", "2018",
						"date", "2018-02-01", "properties",
						"tenantName,buildingName,roomNumbers,areaSize,price,beginDate,terminationDate,follow")
				.when().get("/export/assets/month/termination/detail").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));

	}

	@Test
	// 租客逾期支付报表导出
	public void overduePayExport() {
		String responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "payDateFrom", "2018-01-01",
						"payDateTo", "2018-12-31")
				.when().get("/export/assets/overdue-pay").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 导出价格分布报表导出
	public void priceExport() {
		String responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "spaceUnit", "P", "priceUnit",
						"D", "price", "60", "properties", "tenantName,buildingName,roomNumbers,areaSize,price,follower")
				.when().get("/export/assets/price/excel").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));

		responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "spaceUnit", "P", "priceUnit",
						"M", "price", "60", "properties", "tenantName,buildingName,roomNumbers,areaSize,price,follower")
				.when().get("/export/assets/price/excel").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));

		responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "spaceUnit", "G", "priceUnit",
						"GD", "price", "60", "properties",
						"tenantName,buildingName,roomNumbers,areaSize,price,follower")
				.when().get("/export/assets/price/excel").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));

		responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "spaceUnit", "G", "priceUnit",
						"GM", "price", "60", "properties",
						"tenantName,buildingName,roomNumbers,areaSize,price,follower")
				.when().get("/export/assets/price/excel").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));

		responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "spaceUnit", "", "priceUnit",
						"YM", "price", "60", "properties",
						"tenantName,buildingName,roomNumbers,areaSize,price,follower")
				.when().get("/export/assets/price/excel").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));

		responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "spaceUnit", "", "priceUnit",
						"YD", "price", "60", "properties",
						"tenantName,buildingName,roomNumbers,areaSize,price,follower")
				.when().get("/export/assets/price/excel").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));

	}

	@Test
	// 房源排行榜报表导出
	public void roomsExport() {
		String responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "beginDate", "2018-01-01",
						"endDate", "2018-12-31", "type", "amount", "sort", "vacantAmount,desc")
				.when().get("/export/assets/rooms").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 户型分布图报表导出
	public void roomTypeChartExport() {
		String responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "filterType", "ALL").when()
				.get("/export/assets/rooms/room-type-chart").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));

		responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "filterType",
						"MARKETING_AVAILABLE")
				.when().get("/export/assets/rooms/room-type-chart").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 房源列表报表导出
	public void roomTypeChartBasicExport() {
		String responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "filterType",
						"MARKETING_AVAILABLE", "areaFrom", "100", "areaTo", "150")
				.when().get("/export/assets/rooms/room-type-chart-basic").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 收入摊销(按租客分析)报表导出
	public void incomeAmortizationExport() {
		String responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "beginDate", "2018-01-01",
						"endDate", "2018-12-31", "sort", "PRIME_AMOUNT_DESC", "sortDate", "", "keyword", "接口自动化")
				.when().get("/export/assets/tenants/month").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 租客收入贡献报表导出
	public void tenantsPaymentTotalExport() {
		String responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "beginDate", "2018-01-01",
						"endDate", "2018-12-31")
				.when().get("/export/assets/tenants/payment/total").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 租客收入贡献列表报表导出
	public void tenantsPaymentsExport() {
		String responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "beginDate", "2018-01-01",
						"endDate", "2018-12-31", "properties", "tenantName,buildingName,amount,tenantTags")
				.when().get("/export/assets/tenants/payments").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 到期续租分析图报表导出
	public void terminationRenewExport() {
		String responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "beginDate", "2018-01-01",
						"endDate", "2018-12-31", "type", "AREA_SIZE")
				.when().get("/export/assets/termination/renew").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
		responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "beginDate", "2018-01-01",
						"endDate", "2018-12-31", "type", "STATION_COUNT")
				.when().get("/export/assets/termination/renew").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
		responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "beginDate", "2018-01-01",
						"endDate", "2018-12-31", "type", "CONTRACT_COUNT")
				.when().get("/export/assets/termination/renew").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
		responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "beginDate", "2018-01-01",
						"endDate", "2018-12-31", "type", "RENEW_RATE")
				.when().get("/export/assets/termination/renew").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 楼宇管理列表导出
	public void buildingListExport() {
		String responseBody = given().pathParam("buildingIds", Export_Hooks.export_Info.getBuildingID())
				.queryParams("orderBy", "price", "asc", "false", "properties",
						"buildingName,purpose,areaSizeTotal,areaSizeRent,areaSizeAvailable,companyCount,price,provinceName,cityName,areaName,address,promotionTel,owner,landArea,constructionArea,tenantCount,vacantAreaSize,alertDaysNum,buildupDate")
				.when().get("/export/buildings/{buildingIds}").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 收付款收支流水报表导出
	public void cashExport() {
		String responseBody = given()
				.queryParams("keyword", "22", "beginDate", "2018-03-01", "endDate", "2018-03-31", "action", "OUT",
						"status", "MATCH", "sort", "AMOUNT_DESC", "buildingIds",
						Export_Hooks.export_Info.getBuildingID(), "properties",
						"tenantName,action,amount,hasMatchAmount,noMatchAmount,matchStatus,otherAccount,enterDate,flowNo,voucherNumber,remittanceMethod,receiptNo,billTypeName,buildingName,memo,digest")
				.when().get("/export/cash").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 招商管理客户管理列表报表导出
	public void demandsExport() {
		String responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "sort", "status,desc",
						"sourceTypes", "DOOR_TO_DOOR", "visitBeginDate", "2018-03-01", "visitEndDate", "2018-03-31",
						"commentBeginDate", "2018-03-01", "commentEndDate", "2018-04-30", "keyword", "小则", "properties",
						"companyName,visitDate,status,sourceType,areaSizeRange,industryName,expectSignDate,creatorName,roomIntention,contacts,turnoverRate,lastComment,agents")
				.when().get("/export/demands").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 收付款开票记录报表导出
	public void invoicesExport() {
		String responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "dateIncurredFrom", "2018-02-01",
						"dateIncurredTo", "2018-04-25", "status", "UNISSUE", "type", "NORMAL", "buyerName", "用户",
						"properties",
						"dateIncurred,buyerName,goodName,type,invoiceNumber,amount,taxAmount,taxRate,status,price,count,specification,unit,buyerBankName,invoiceCode,remark,buyerTaxNumber,buyerAddress,buyerTel")
				.when().get("/export/invoices").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 招商管理我的渠道列表报表导出
	public void maketingChannelExport() {
		List<Integer> regionIdsList = export_Flows.getRegionList();
		Export_Hooks.export_Info.setRegionId(regionIdsList.get(new Random().nextInt(regionIdsList.size())));
		String responseBody = given()
				.queryParams("keyword", "接口测试", "latestVisitBeginDate", "2018-03-01", "latestVisitEndDate",
						"2018-04-26", "regionIds", Export_Hooks.export_Info.getRegionIds(), "properties",
						"name,tel,chargeRegions,channelModels,visitTimes,latestVisitDate,dealCount,email,address,companyName")
				.when().get("/export/marketing/channel/contacts").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 物业合同列表导出
	public void propertyContractsExport() {
		String responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "archived", "false", "startDate",
						"2019-03-01", "endDate", "2019-05-04", "auditStatus", "TERMINATION_UNPASS", "sort",
						"tenantName,desc", "keyword", "接口自动化", "properties",
						"tenantName,buildingName,roomNumber,areaSize,spaceUnit,startDate,endDate,auditStatusName,firstPayTermPrice,priceUnit,renewContractName,hasRenewedName,signDate,deposit,contractNo")
				.when().get("/export/property/contracts").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 房源管理所有房源列表导出
	public void roomAllExport() {
		String responseBody = given().pathParam("buildingIds", Export_Hooks.export_Info.getBuildingID())
				.queryParams("roomType", "ALL", "orderBy", "AREA_SIZE", "asc", "false", "roomNumber", "接口自动化",
						"roomTags", "string", "properties",
						"buildingName,floor,roomNumber,fitment,areaSize,roomTags,tenantName,contractStartDate,terminationDate,contractPrice,contractPriceUnit,price,priceUnit,marketingAvailable")
				.when().get("/export/room/all/{buildingIds}").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 房源管理已租房源列表导出
	public void roomNoPromotionExport() {
		String responseBody = given().pathParam("buildingIds", Export_Hooks.export_Info.getBuildingID())
				.queryParams("roomType", "OCCUPIED", "orderBy", "AREA_SIZE", "asc", "false", "roomNumber", "接口自动化",
						"roomTags", "string", "properties",
						"buildingName,floor,roomNumber,fitment,areaSize,roomTags,tenantName,contractStartDate,terminationDate,contractPrice,contractPriceUnit,price,priceUnit,marketingAvailable")
				.when().get("/export/room/noPromotion/{buildingIds}").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 房源管理可招商房源列表导出
	public void roomPromotionExport() {
		String responseBody = given().pathParam("buildingIds", Export_Hooks.export_Info.getBuildingID())
				.queryParams("roomType", "AVAILABLE", "orderBy", "PRICE", "asc", "false", "roomNumber", "接口自动化",
						"roomTags", "string", "properties",
						"buildingName,floor,roomNumber,price,fitment,priceUnit,areaSize,roomTags,marketingAvailable")
				.when().get("/export/room/promotion/{buildingIds}").then().assertThat().statusCode(200).extract()
				.asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 租客信息列表导出
	public void tenantsExport() {
		String responseBody = given()
				.queryParams("sort", "businessInfo.foundingTime,desc", "name", "接口自动化", "tags", "string",
						"foundingTimeFrom", "2018-02-01", "foundingTimeTo", "2018-05-04", "properties",
						"certificateNumber,industry,buildingName,businessInfo__legalPerson,businessInfo__registeredCapital,businessInfo__foundingTime,businessInfo__businessTerm,tagJoin,contactJoin,tel,email,businessInfo__registrationAuthority,businessInfo__address,businessInfo__staffSize,businessInfo__companyType,businessInfo__operatingState,businessInfo__organizationCode,businessInfo__registrationNumber,businessInfo__taxpayerIdNumber,businessInfo__unifiedSocialCreditCode,name,businessInfo__approvalTime,businessInfo__englishName,businessInfo__district,businessInfo__businessIndustry,businessInfo__businessScope,invoiceInfo__bank,businessInfo__birthCountry,invoiceInfo__account,invoiceInfo__taxpayerIdNumber,invoiceInfo__invoiceInfoTel,invoiceInfo__address")
				.when().get("/export/tenants").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 收付款所有账单列表导出
	public void billsExport() {
		String responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "closedStatus", "NORMAL",
						"payDateFrom", "2018-01-03", "payDateTo", "2018-05-19", "startDate", "2018-01-03", "endDate",
						"2018-05-11", "sort", "AMOUNT_DESC", "dueStatus", "DUE", "invoiceStatus", "NO_MATCH",
						"settleStatus", "IN_PROCESS", "action", "IN", "type", "物业费", "keyword", "接口自动化", "properties",
						"buildingName,roomNumber,other,action,typeName,primeAmount,payedAmount,remainingAmount,invoiceAmount,payDate,dateScope,dueStatusName,invoiceStatusName,settleStatusName")
				.when().get("/export/v2/bills").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	// 租赁合同列表导出
	public void contractsExport() {
		String responseBody = given()
				.queryParams("buildingIds", Export_Hooks.export_Info.getBuildingID(), "archived", "false", "startDate",
						"2019-03-01", "endDate", "2019-05-04", "auditStatus", "CREATING_UNPASS", "sort",
						"tenantName,desc", "keyword", "接口自动化", "properties",
						"tenantName,buildingName,roomNumber,followName,areaSize,spaceUnit,startDate,endDate,auditStatusName,firstPayTermPrice,priceUnit,renewContractName,hasRenewedName,industryName,legalPerson,contractNo,signer,deposit,signDate")
				.when().get("/export/v2/contracts").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}
}
