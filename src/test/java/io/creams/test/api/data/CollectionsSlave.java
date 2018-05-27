package io.creams.test.api.data;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertThat;

public class CollectionsSlave {

	public String getCurrentDay() {
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(day);
	}

	public String getIntListItemRandom(List<Integer> IntegerList) {
		Collections.shuffle(IntegerList);
		Integer maxInt = IntegerList.size() - 1;
		Integer minInt = 1;
		Integer randomInt = new Random().nextInt(maxInt - minInt + 1) + minInt;
		List<Integer> subIntegerList = IntegerList.subList(0, randomInt);
		String subListString = subIntegerList.toString();
		String subListStringResult = subListString.substring(1, subListString.length() - 1);
		return subListStringResult;
	}

	public Integer getListIndexRandom(Integer range) {
		if (range == 1) {
			return 0;
		} else {
			return new Random().nextInt(range);
		}
	}

	public Map<String, Object> getListItemRandom(List<Map<String, Object>> listObject) {
		return listObject.get(new Random().nextInt(listObject.size()));
	}

	public List<Object> getListMapKeyValueList(List<Map<String, Object>> listMap, String MapKey) {
		List<Object> mapValueList = new ArrayList<>();
		for (Map<String, Object> map : listMap) {
			mapValueList.add(map.get(MapKey));
		}
		return mapValueList;
	}

	public void verifyList(List<Map<String, Object>> itemList, String schemaFilePath) {
		JSONObject ItemsJson = new JSONObject(itemList);
		assertThat(ItemsJson.toString(), matchesJsonSchemaInClasspath(schemaFilePath));
	}

	public void verifyMap(Map<String, Object> actualMap, String schemaFilePath) {
		JSONObject ItemsJson = new JSONObject(actualMap);
		assertThat(ItemsJson.toString(), matchesJsonSchemaInClasspath(schemaFilePath));
	}

	public void verifySubItemListRandom(List<Map<String, Object>> itemList, String schemaFilePath) {
		Map<String, Object> ItemsMaps = itemList.get(new Random().nextInt(itemList.size()));
		JSONObject ItemsJson = new JSONObject(ItemsMaps);
		assertThat(ItemsJson.toString(), matchesJsonSchemaInClasspath(schemaFilePath));
	}

}
