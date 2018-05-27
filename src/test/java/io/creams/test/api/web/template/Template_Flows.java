package io.creams.test.api.web.template;

import java.util.List;
import java.util.Map;

public class Template_Flows {

	public List<Object> getBuildingIdList() {
		@SuppressWarnings("unchecked")
		Map<String, Object> userMap = (Map<String, Object>) Template_Hooks.userAccount.getUserInfoMap().get("user");
		@SuppressWarnings("unchecked")
		List<Object> buildingIdList = (List<Object>) userMap.get("buildingIdList");
		return buildingIdList;
	}

	public int getCustomerId() {
		@SuppressWarnings("unchecked")
		Map<String, Object> userMap = (Map<String, Object>) Template_Hooks.userAccount.getUserInfoMap().get("user");
		int userId = (int) userMap.get("customerId");
		return userId;
	}
}
