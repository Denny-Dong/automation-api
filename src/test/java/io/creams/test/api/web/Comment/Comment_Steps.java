package io.creams.test.api.web.Comment;

import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertThat;

public class Comment_Steps extends Comment_Hooks {

	private Comment_Flows comment_Flows;

	public Comment_Steps() {
		super();
		comment_Flows = new Comment_Flows();
	}

	@Test
	// 新增评论
	public void comment_a() throws IOException {
		String BuildingsIdString = comment_Flows.getBuildingIdList().get(0).toString();
		int objectId = comment_Flows.getObjectId(BuildingsIdString);
		Comment_Hooks.comment_Info.setObjectId(objectId);

		given().body(comment_Flows.commentInfoMap(objectId)).when().post("/v2/comments").then().assertThat()
				.statusCode(201);

	}

	@Test
	// 查询评论列表
	public void comment_b() {
		List<Map<String, Object>> commentItemsList = given()
				.params("objectType", "CONTRACT", "objectId", Comment_Hooks.comment_Info.getObjectId()).when()
				.get("/v2/comments").then().assertThat().statusCode(200).extract().path("items");
		Map<String, Object> commentItemsMaps = commentItemsList.get(new Random().nextInt(commentItemsList.size()));

		int commentId = Integer.parseInt(commentItemsMaps.get("id").toString());
		Comment_Hooks.comment_Info.setCommentId(commentId);
		JSONObject commentItemsJson = new JSONObject(commentItemsMaps);
		assertThat(commentItemsJson.toString(),
				matchesJsonSchemaInClasspath("jsonResources/schema/web/comment/comment_get_items.json"));

	}

	@Test
	// 修改评论
	public void comment_c() {

		given().pathParam("commentId", Comment_Hooks.comment_Info.getCommentId())
				.body(comment_Flows.updateCommentInfoMap()).when().put("/v2/comments/{commentId}").then().assertThat()
				.statusCode(202);
	}

	@Test
	// 删除评论
	public void comment_d() {
		given().pathParam("commentId", Comment_Hooks.comment_Info.getCommentId()).when()
				.delete("/v2/comments/{commentId}").then().assertThat().statusCode(204);
	}

}
