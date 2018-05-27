package io.creams.test.api.web.buildings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Floor_Info {

	private Integer buildingId;
	private List<Map<String, Object>> floorInfoList = new ArrayList<>();

	public Floor_Info() {
		super();
	}

	public List<Map<String, Object>> getFloorInfoList() {
		return floorInfoList;
	}

	public void setFloorInfoList(List<Map<String, Object>> floorInfoList) {
		this.floorInfoList = floorInfoList;
	}

	public Integer getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
	}

}
