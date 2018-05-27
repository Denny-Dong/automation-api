package io.creams.test.api.web.oa;

import java.util.Map;

public class OA_Info {

	private String oaName;
	private Integer contractID = null;
	private Integer buildingID = null;
	private Integer roomID = null;
	private Integer userID = null;
	private Map<String, Object> oaInstancesStartedItemsMaps;

	public OA_Info(String oaName) {
		super();
		this.oaName = oaName;
	}

	public Integer getBuildingID() {
		return buildingID;
	}

	public void setBuildingID(Integer buildingID) {
		this.buildingID = buildingID;
	}

	public Integer getRoomID() {
		return roomID;
	}

	public void setRoomID(Integer roomID) {
		this.roomID = roomID;
	}

	public void setContractID(Integer contractID) {
		this.contractID = contractID;
	}

	public Integer getContractID() {
		return contractID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public Integer getUserID() {
		return userID;
	}

	public void setOaInstancesStartedItemsMaps(Map<String, Object> oaInstancesStartedItemsMaps) {
		this.oaInstancesStartedItemsMaps = oaInstancesStartedItemsMaps;
	}

	public Map<String, Object> getOaInstancesStartedItemsMaps() {
		return oaInstancesStartedItemsMaps;
	}
}
