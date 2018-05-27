package io.creams.test.api.web.property;

import java.util.List;

public class Property_Info {

	private List<Integer> roomId = null;
	private Integer buildingId = null; 
	
	public Property_Info(){
		super();
		this.buildingId=buildingId;
	}

	

	public List<Integer> getRoomId() {
		return roomId;
	}



	public void setRoomId(List<Integer> roomId) {
		this.roomId = roomId;
	}



	public Integer getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
	}
	
}
