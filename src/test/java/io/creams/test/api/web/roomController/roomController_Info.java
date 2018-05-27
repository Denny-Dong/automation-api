package io.creams.test.api.web.roomController;

import com.google.gson.Gson;

public class roomController_Info {
	private String roomNumber = null;
	private Integer roomId = null;
	private Gson gson = null;
	private Integer buildingId = null;
	private Integer buidingHasRooms = null;
	private Integer floorIdHasRooms = null;
	private String floorName = null;

	public void setfloorName(String floorName) {
		this.floorName = floorName;
	}

	public String getfloorName() {
		return floorName;
	}
	

	public Integer getFloorIdHasRooms() {
		return floorIdHasRooms;
	}

	public void setFloorIdHasRooms(Integer floorIdHasRooms) {
		this.floorIdHasRooms = floorIdHasRooms;
	}

	public Integer getBuidingHasRooms() {
		return buidingHasRooms;
	}

	public void setBuidingHasRooms(Integer buidingHasRooms) {
		this.buidingHasRooms = buidingHasRooms;
	}

	public roomController_Info(String roomNumber) {
		super();
		this.roomNumber = roomNumber;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setBuildingID(Integer buildingId) {
		this.buildingId = buildingId;
	}

	public Integer getBuildingId() {
		return buildingId;
	}

	public void setPortfolioID(Integer roomId) {
		this.roomId = roomId;
	}

}
