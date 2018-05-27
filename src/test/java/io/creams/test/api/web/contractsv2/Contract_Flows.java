package io.creams.test.api.web.contractsv2;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.creams.test.api.HooksApi;
import org.omg.CORBA.INTERNAL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static io.restassured.RestAssured.given;

public class Contract_Flows {
	protected Logger logger = LoggerFactory.getLogger(getClass().getName());

	public JsonObject getContractParaJson(String filepath) throws IOException {
		return HooksApi.driver.jsonObjectProducer(filepath);
	}
	
	public JsonObject getRoomsJson(String filepath) throws IOException {
		return HooksApi.driver.jsonObjectProducer(filepath);
	}
	
	//获取该用户的楼宇集合
	public List<Integer> getBuildingIdList() {
		@SuppressWarnings("unchecked")
		Map<String, Object> userMap = (Map<String, Object>) Contract_Hooks.userAccount.getUserInfoMap().get("user");
		@SuppressWarnings("unchecked")
		List<Integer> buildingIdList = (List<Integer>) userMap.get("buildingIdList");
		return buildingIdList;
	}

	
	//获取楼宇ID
	/*
	public Integer getBuildingsId() {
		List<Map<String, Object>> buildingsItemsList = given().when().get("/buildings").then().assertThat()
				.statusCode(200).extract().path("items");
		Integer buildingId = (Integer)buildingsItemsList.get(new Random().nextInt(buildingsItemsList.size())).get("id");
		return buildingId;
	}
	*/
	
	//获取用户的可招商房源
	public int getRooms(String buildingId) throws IOException {
		List<Map<String, Object>> contractItemsList = given().pathParam("buildingids", buildingId).when()
				.get("/buildings/{buildingids}/rooms/marketing").then().assertThat().statusCode(200).extract()
				.path("items");
		if(contractItemsList.size()<=0) {
			//自己创建一个房源
			JsonObject createRooms = this.getRoomsJson("jsonResources/web/contractv2/create_room.json");
			int roomId = Integer.parseInt(given().pathParam("buildingId", buildingId)
					.body(createRooms.toString()).when().post("/buildings/{buildingId}/rooms").then().assertThat()
					.statusCode(201).extract().body().asString());
			return roomId;
		}
		Map<String, Object> contractItemsMaps = contractItemsList
				.get(new Random().nextInt(contractItemsList.size()));
		int roomId = Integer.parseInt(contractItemsMaps.get("id").toString());
		return roomId;
	}

	//修改租赁合同的房源和followId
	public JsonObject putRoomParamToJson(String filepath, int room) throws IOException {
		JsonObject createContractBody = this.getContractParaJson(filepath);
		JsonArray roomsList = new JsonArray();
		roomsList.add(room);
		createContractBody.add("roomIds", roomsList);
		return createContractBody;
	}

	//修改租赁合同的房源和followId
	public JsonObject putRoomAndFollowParamToJson(String filepath, int room, Integer userId) throws IOException {
		JsonObject createContractBody = this.getContractParaJson(filepath);
		JsonArray roomsList = new JsonArray();
		roomsList.add(room);
		createContractBody.add("roomIds", roomsList);
		createContractBody.addProperty("followId", userId);
		return createContractBody;
	}

	//修改退租协议的退租时间
	public JsonObject putTerminationTime(String filepath, String terminationDate, String signDate) throws IOException{
		JsonObject terminationBody = this.getContractParaJson(filepath);
		terminationBody.addProperty("terminationDate", terminationDate);
		terminationBody.addProperty("signDate", signDate);
		return terminationBody;
	}
	
	//获取token中的userId
	public int getUserId() {
		@SuppressWarnings("unchecked")
		Map<String, Object> userMap = (Map<String, Object>) Contract_Hooks.userAccount.getUserInfoMap()
				.get("user");
		int userId = (int) userMap.get("userId");
		return userId;
	}
	
	//我发起的审核条件,选择了一个楼宇
	public Map<String,Object> approvalPostBody(int userId,Integer buildingId,String description,String domainType, Integer objectId,String subDomainType){
		List<Map<String, Object>> userIdList = new ArrayList<Map<String, Object>>();
		Map<String, Object> userInfoMap = new HashMap<String, Object>();
		userInfoMap.put("queue", 1);
		userInfoMap.put("userId", userId);
		userIdList.add(userInfoMap);
		Map<String,Object> approverMap = new HashMap<String, Object>();
		approverMap.put("approvers",userIdList);
		approverMap.put("buildingId",buildingId);
		approverMap.put("description",description);
		approverMap.put("domainType",domainType);
		approverMap.put("objectId",objectId);
		approverMap.put("subDomainType",subDomainType);
		return approverMap;
	}
	
	//通过楼宇名称获取楼宇id
	public Integer getBuildingId(String buildingName) {
		Integer buildingId = 0;
		List<Map<String,Object>> buildingList = given().when().get().then().assertThat().statusCode(200).extract().path("items");
		for(Map<String,Object> buldingMap:buildingList) {
			if(buldingMap.get("name").equals(buildingName)) {
				buildingId = Integer.valueOf(buldingMap.get("id").toString());
				break;
			}
		}
		return buildingId;
	}
	
	//获取所有新建合同的合同id和他的buildingId
	public List<Integer> getContractId(String tenantName) {
		//定义一个list集合，里面存在所有的合同和楼宇
		List<Integer> contractInformationList = new ArrayList<Integer>();
		List<Map<String, Object>> contractList = given().queryParam("buildingIds",4284).queryParam("auditStatus","CREATING_UNPASS").   //3665,3666,3660,3981
				queryParam("page", 1).queryParam("size", 2000).when().get("/v2/contracts").then().assertThat()
				.statusCode(200).extract().path("items");
		Integer contractId = 0;
		for(Map<String,Object> contractMap:contractList) {
			if(contractMap.get("tenantName").equals(tenantName)) {
				contractId = Integer.valueOf(contractMap.get("id").toString());
				contractInformationList.add(contractId);
			}
		}
		return contractInformationList;
	}
	
	//获取所有正常合同的合同id和他的buildingId
	public List<Integer> getNormalContractId(String tenantName) {
		//定义一个list集合，里面存在所有的合同和楼宇
		List<Integer> contractInformationList = new ArrayList<Integer>();
		List<Map<String, Object>> contractList = given().queryParam("buildingIds",3660).queryParam("auditStatus","CREATING_PASS").
				queryParam("page", 1).queryParam("size", 1000000000).when().get("/v2/contracts").then().assertThat()
				.statusCode(200).extract().path("items");
		Integer contractId = 0;
		for(Map<String,Object> contractMap:contractList) {
			if(contractMap.get("tenantName").equals(tenantName)) {
				contractId = Integer.valueOf(contractMap.get("id").toString());
				contractInformationList.add(contractId);
			}
		}
		return contractInformationList;
	}
	
	
	//发起审核
	public void initiateApproval(int userId,Integer buildingId,String description,String domainType, Integer objectId,String subDomainType) {
		Map<String,Object> initiateApprovalPostBody = this.approvalPostBody(userId, buildingId, description, domainType, objectId, subDomainType);
		given().body(initiateApprovalPostBody).when().post("/oa/start").then().assertThat().statusCode(200);
	}
	//对所有合同发起审核
	public void initiateApprovalAll() {
		//暂时没想好，感觉要使用嵌套循环啊
	}
	
	//拿到待审核合同的审核Id(通过subDomainTypes来确定审核类型)
	public long getNewApprovalId(Integer objectId,String subDomainTypes) {
		List<Map<String,Object>> myApprovalList = given().queryParam("domainType", "CONTRACT").queryParam("subDomainTypes", subDomainTypes).queryParam("page", 1).queryParam("size", 100).
		when().get("/oa/instances/todo").then().assertThat().statusCode(200).extract().path("items");
		long approvalId = 0L;
		for(Map<String,Object> myApprovalMap:myApprovalList) {
			if (myApprovalMap.get("objectId").equals(objectId)) {
				approvalId = Long.valueOf(String.valueOf(myApprovalMap.get("id"))).longValue();
				 break;
			}
		}
		return approvalId;
	}

	//审核通过
	public void approvalInstance(long approvalId, String status,String comment) {
		given().pathParam("id", approvalId).queryParam("status", status).queryParam("comment", comment).
		when().put("/oa/instances/{id}").then().assertThat().statusCode(200);
	}
	
	//删除合同
	public void approvalDelete(long approvalId,boolean flag) {
		given().pathParam("id", approvalId).queryParam("flag", flag).
		when().delete("/oa/instances/{id}").then().assertThat().statusCode(200);
	}
	
	//作废合同时,对账单的处理结果
	public Map<String, Object> invalidInfoMap(String action, String memo) {
		Map<String, Object> invalidInfoPostBody = new HashMap<String, Object>();
		invalidInfoPostBody.put("action", action);
		invalidInfoPostBody.put("memo", memo);
		return invalidInfoPostBody;
	}
}