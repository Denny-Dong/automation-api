package io.creams.test.api.web.Comment;

import com.google.gson.JsonObject;
import io.creams.test.api.HooksApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class Comment_Flows {
	protected Logger logger = LoggerFactory.getLogger(getClass().getName());

	public JsonObject getCommentJson(String filepath) throws IOException {
		return HooksApi.driver.jsonObjectProducer(filepath);
	}

	public JsonObject putCommentJson(String filepath) throws IOException {
		return HooksApi.driver.jsonObjectProducer(filepath);
	}

	// 获取buildingId
	public List<Object> getBuildingIdList() {
		@SuppressWarnings("unchecked")
		Map<String, Object> userMap = (Map<String, Object>) Comment_Hooks.userAccount.getUserInfoMap().get("user");
		@SuppressWarnings("unchecked")
		List<Object> buildingIdList = (List<Object>) userMap.get("buildingIdList");
		return buildingIdList;
	}

	// 获取ObjectId
	public int getObjectId(String buildingId) {
		List<Map<String, Object>> commentItemsList = given().queryParam("buildingIds", buildingId).when()
				.get("/v2/contracts").then().assertThat().statusCode(200).extract().path("items");

		Map<String, Object> commentItemsMaps = commentItemsList.get(new Random().nextInt(commentItemsList.size()));
		int objectId = Integer.parseInt(commentItemsMaps.get("id").toString());
		return objectId;
	}

	public Map<String, Object> commentInfoMap(int objectId) {
		Map<String, Object> commentPostBody = new HashMap<String, Object>();
		commentPostBody.put("content", "测试API自动化评论接口");
		commentPostBody.put("objectId", objectId);
		commentPostBody.put("objectType", "CONTRACT");
		return commentPostBody;
	}

	public Map<String, Object> updateCommentInfoMap() {
		Map<String, Object> updateCommentPostBody = new HashMap<String, Object>();
		updateCommentPostBody.put("content", "测试修改API自动化评论接口");
		return updateCommentPostBody;
	}

}
