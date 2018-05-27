package io.creams.test.api.web.contractsv2;

import com.google.gson.JsonObject;
import org.hamcrest.CoreMatchers;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class Contract_Steps extends Contract_Hooks {
	private Contract_Flows contract_Flows;

	public Contract_Steps() {
		super();
		contract_Flows = new Contract_Flows();
	}

	@Test
	//新建合同
	public void Contract_contract_a() throws IOException {
		//获取该用户的所有楼宇
		List<Integer> buildingIdList = contract_Flows.getBuildingIdList();
		//随机选择一个楼宇
		Integer buildingId = Integer
				.parseInt(buildingIdList.get(new Random().nextInt(buildingIdList.size())).toString());
		//选择一个可招商房源
		int roomId = contract_Flows.getRooms(buildingId.toString());
		Contract_Hooks.contractData.setBuildingID(buildingId);
		Contract_Hooks.contractData.setRoomID(roomId);
		//System.out.println(buildingId);
		//System.out.println(roomId);
		//修改json中的roomId,rroomId属于Map，新增一个value，会替换掉原来的value
		JsonObject createContractBody = contract_Flows
				.putRoomAndFollowParamToJson("jsonResources/web/contractv2/contract_create.json", roomId, contract_Flows.getUserId());
		//拿到新建成功的合同id赋值给contractData
		Contract_Hooks.contractData.setContractID(Integer.parseInt(given().body(createContractBody.toString()).when()
				.post("/v2/contracts").then().assertThat().statusCode(201).extract().body().asString()));
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//提交新建待审核
		contract_Flows.initiateApproval(contract_Flows.getUserId(), Contract_Hooks.contractData.getBuildingID(),
				"自动化接口测试", "CONTRACT", Contract_Hooks.contractData.getContractID(), "NEW_APPROVAL");
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//审核通过,需要拿到审核id后,审核通过
		contract_Flows.approvalInstance(contract_Flows.getNewApprovalId(Contract_Hooks.contractData.getContractID(), "NEW_APPROVAL"), "APPROVED", "自动化接口测试合同审核");
		//RobbitMQ异步，加2s延迟
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	//查询合同列表
	public void Contract_contract_b() throws Exception{
		//获取楼宇id
		long id = Long.valueOf(Contract_Hooks.contractData.getBuildingID().toString());
		//获取合同id
		Integer contractId = Contract_Hooks.contractData.getContractID();
		//输出合同id
		System.out.println(contractId);
		//获取房源id
		long roomId = Contract_Hooks.contractData.getRoomID();
		//调用查询接口
		List<Map<String, Object>> contractList = given().queryParam("buildingIds", id).queryParam("roomIds", roomId)
				.queryParam("page",1).queryParam("size",100000000).when().get("/v2/contracts").then().assertThat()
				.statusCode(200).extract().path("items");
		//初始化合同name为null;
		String contractName = null;
        for(Map<String,Object> map:contractList){
			try {
				if (contractId.equals(Integer.parseInt (map.get("id").toString()))) {
					contractName = (String) map.get("tenantName");
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("循环没有找到");
			}
        }
		System.out.println(contractName);
		Assert.assertThat(contractName, CoreMatchers.containsString(Contract_Hooks.contractData.getContractName()));
		// contractJson = new JSONObject(map);
		//assertThat(contractJson.toString(),matchesJsonSchemaInClasspath("jsonResources/schema/web/contractsv2/contract_get_items.json"));
	}

	@Test
	//租金报表明细生成
	public void Contract_pays() throws IOException {
		JsonObject contractsPaysBody = contract_Flows.getContractParaJson("jsonResources/web/contractv2/pays_post.json");
		List<Map<String, Object>> contractPaysItemsList = given().body(contractsPaysBody.toString()).when().post("/v2/contracts/pays").then().assertThat().statusCode(200).extract().path("");
		Map<String, Object> contractPaysItemMap = contractPaysItemsList.get(new Random().nextInt(contractPaysItemsList.size()));
		JSONObject contractPaysJson = new JSONObject(contractPaysItemMap);
		assertThat(contractPaysJson.toString(), matchesJsonSchemaInClasspath("jsonResources/schema/web/contractsv2/contractPaysList.json"));

	}

	@Test
	//合同详情查询
	public void Contract_contract_c() {
		long contractId = Long.parseLong(Contract_Hooks.contractData.getContractID().toString());
		Map<String, Object> createMap = given().pathParam("contractId", contractId).when()
				.get("/v2/contracts/{contractId}").then().assertThat().statusCode(200)
				.body(matchesJsonSchemaInClasspath("jsonResources/schema/web/contractsv2/contract_get_detail.json"))
				.extract().path("createdBy");
		JSONObject createJson = new JSONObject(createMap);
		assertThat(createJson.toString(),
				matchesJsonSchemaInClasspath("jsonResources/schema/web/contractsv2/contract_get_items.json"));
	}

	@Test
	//租赁合同租金明细列表导出
	public void Contract_contract_d() {
		String responseBody = given().pathParam("contractId", Contract_Hooks.contractData.getContractID()).when()
				.get("/export/v2/contracts/{contractId}").then().assertThat().statusCode(200).extract().asString();
		assertThat(responseBody, containsString("https://temporary.creams.io/temp"));
	}

	@Test
	//修改合同
	public void Contract_contract_e() throws IOException {
		//修改房号
		JsonObject modifyContractBody = contract_Flows
				.putRoomAndFollowParamToJson("jsonResources/web/contractv2/contract_create.json", Contract_Hooks.contractData.getRoomID(), contract_Flows.getUserId());
		given().body(modifyContractBody.toString()).pathParam("contractId", Contract_Hooks.contractData.getContractID())
				.when().put("/v2/contracts/{contractId}").then().assertThat().statusCode(202);
	}

	@Test
	//计算退租协议所需金额
	public void Contract_contract_f() {
		given().pathParams("contractId", Contract_Hooks.contractData.getContractID())
				.queryParam("terminationDate", collectionsSlave.getCurrentDay())
				.when().get("/v2/contracts/{contractId}/termination/finance/term")
				.then().assertThat().statusCode(200);
	}

	@Test
	//退租合同
	public void Contract_contract_g() throws IOException {
		//JsonObject terminationPostBody = contract_Flows.getContractParaJson("jsonResources/web/contractv2/contract_termination.json");
		//修改退租时间为今天(该时间自动获取系统时间)
		JsonObject terminationPostBody = contract_Flows
				.putTerminationTime("jsonResources/web/contractv2/contract_termination.json", collectionsSlave.getCurrentDay(),collectionsSlave.getCurrentDay());
		//post
		Contract_Hooks.contractData.setTerminationId(Integer.parseInt(given().pathParams("contractId", Contract_Hooks.contractData.getContractID())
				.queryParam("buildingId", Contract_Hooks.contractData.getBuildingID())
				.body(terminationPostBody.toString()).when().post("/v2/contracts/{contractId}/termination").then().assertThat().statusCode(201).extract().body().asString()));
		//start
		contract_Flows.initiateApproval(contract_Flows.getUserId(), Contract_Hooks.contractData.getBuildingID(),
				"退租接口测试我要提交退租2", "CONTRACT", Contract_Hooks.contractData.getTerminationId(), "TERMINATION_APPROVAL");
		//审核通过,需要拿到审核id后,审核通过
		contract_Flows.approvalInstance(contract_Flows.getNewApprovalId(Contract_Hooks.contractData.getTerminationId(), "TERMINATION_APPROVAL"), "APPROVED", "接口退租测试通过");
		//RobbitMQ异步，加2s延迟
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	//作废合同
	public void Contract_contract_i() {
		Map<String, Object> invalidInfoPostBody = contract_Flows.invalidInfoMap("CLOSE_ALL_BILLS", "关闭所有账单");
		//发起作废
		Contract_Hooks.contractData.setInvalidContractId(Integer.parseInt(given().pathParam("contractId", Contract_Hooks.contractData.getContractID()).queryParam("buildingId", Contract_Hooks.contractData.getBuildingID())
				.body(invalidInfoPostBody).when().post("/v2/contracts/{contractId}/nullification").then().assertThat().statusCode(201).extract().body().asString()));

		contract_Flows.initiateApproval(contract_Flows.getUserId(), Contract_Hooks.contractData.getBuildingID(),
				"作废接口测试我要提交作废2", "CONTRACT", Contract_Hooks.contractData.getInvalidContractId(), "NULLIFICATION_APPROVAL");
		System.out.println("作废id:"+Contract_Hooks.contractData.getInvalidContractId());
		//审核通过
		long id = contract_Flows.getNewApprovalId(Contract_Hooks.contractData.getInvalidContractId(),"NULLIFICATION_APPROVAL");
		System.out.println(id);
		contract_Flows.approvalInstance(contract_Flows.getNewApprovalId(Contract_Hooks.contractData.getInvalidContractId(), "NULLIFICATION_APPROVAL"), "APPROVED", "接口作废测试通过");
		//RobbitMQ异步，加2s延迟
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Ignore
	//删除所有新建待修改的合同(通过合同名)----不做测试
	public void deleteContract() throws IOException {
		//想对列表中的所有新建的合同发起审核,但是失败了
		//拿到合同id和楼宇id
		List<Integer> contractListInformationList = contract_Flows.getContractId(Contract_Hooks.contractData.getContractName());

		for (Integer contractId : contractListInformationList) {
			contract_Flows.initiateApproval(contract_Flows.getUserId(), 4067,
					"自动化接口测试", "CONTRACT", contractId, "NEW_APPROVAL");
			contract_Flows.approvalDelete(contract_Flows.getNewApprovalId(contractId, "NEW_APPROVAL"), true);
		}
	}


	@Ignore
	//作废所有正常的合同--不做测
	public void nullificationAllContract() {
		//拿到正常合同id
		List<Integer> contractListInformationList = contract_Flows.getNormalContractId(Contract_Hooks.contractData.getContractName());

		Map<String, Object> invalidInfoPostBody = contract_Flows.invalidInfoMap("CLOSE_ALL_BILLS", "string");
		for (Integer contractId : contractListInformationList) {
			//发起作废
			Contract_Hooks.contractData.setInvalidContractId(Integer.parseInt(given().pathParam("contractId", contractId).queryParam("buildingId", 3660)
					.body(invalidInfoPostBody).when().post("/v2/contracts/{contractId}/nullification").then().assertThat().statusCode(201).extract().body().asString()));

			contract_Flows.initiateApproval(contract_Flows.getUserId(), 3660,
					"自动化接口测试", "CONTRACT", Contract_Hooks.contractData.getInvalidContractId(), "NULLIFICATION_APPROVAL");

			contract_Flows.approvalInstance(contract_Flows.getNewApprovalId(Contract_Hooks.contractData.getInvalidContractId(),"NULLIFICATION_APPROVAL"),"APPROVED", "接口作废测试通过");
		}
	}

}