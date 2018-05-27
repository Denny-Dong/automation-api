package io.creams.test.api.web.businessCommon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BusinessCommon_Flows {
	protected Logger logger = LoggerFactory.getLogger(getClass().getName());
	
	
	//获取List
	public List<Map<String, Object>> getTagsItemsList() {
		List<Map<String, Object>> tagsItemsList = given().when().get("/tags").then().assertThat()
				.statusCode(200).extract().path("");
		return tagsItemsList;
	}
	
	//获取tags post参数
	public Map<String, Object> TagsInfoMap(String name, String typeEnum) {
		Map<String, Object> tagsPostBody = new HashMap<String, Object>();
		tagsPostBody.put("name", name);
		tagsPostBody.put("typeEnum", typeEnum);
		return tagsPostBody;
	}
	
	//获取category post参数
	public Map<String, Object> CategoryInfoMap(String categoryEnum, String name) {
		Map<String, Object> categoryPostBody = new HashMap<String, Object>();
		categoryPostBody.put("categoryEnum", categoryEnum);
		categoryPostBody.put("name", name);
		return categoryPostBody;
	}
	
	//获取get参数
	public Map<String, Object> TagsInfoMap(String typeEnum) {
		Map<String, Object> tagsGetBody = new HashMap<String, Object>();
		tagsGetBody.put("typeEnum", typeEnum);
		return tagsGetBody;
	}
	
	//获取tag id
	public long getTagId(String tagName) {
		List<Map<String, Object>> items = given().param("tagType","ROOM").when().get("/tags").then().statusCode(200)
				.extract().path("");
		long tagId = 0L;
		for (Map<String, Object> map : items) {
			if (map.get("name").equals(tagName)) {
				 tagId = Long.valueOf(String.valueOf(map.get("id"))).longValue();
				 break;
			}
		}
		return tagId;
	}
	
	//获取category id
	public long getCategoryId(String tagName) {
		List<Map<String, Object>> items = given().param("categoryEnum","BILL").when().get("/commons/custom-category").then().statusCode(200)
				.extract().path("");
		long categoryId = 0L;
		for (Map<String, Object> map : items) {
			if (map.get("name").equals(tagName)) {
				categoryId = Long.valueOf(String.valueOf(map.get("id"))).longValue();
				 break;
			}
		}
		return categoryId;
	}
	
	//过滤List集合
	public List<Map<String,Object>> filterList(List<Map<String,Object>> list){
		for (int i = list.size()-1; i >= 0; i--) {
			Map<String,Object> map = list.get(i);
			if(map.get("id")==null) {
				list.remove(i);
			}		
		}
		return list;
	}
	
}
