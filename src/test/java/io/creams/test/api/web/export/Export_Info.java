package io.creams.test.api.web.export;

public class Export_Info {
	private String propertyContractName = null;
	private Integer buildingID = null;
	private Integer regionId = null;

	public Export_Info(String propertyContractName) {
		super();
		this.propertyContractName = propertyContractName;
	}

	public Integer getBuildingID() {
		return buildingID;
	}

	public void setBuildingID(Integer buildingID) {
		this.buildingID = buildingID;
	}

	public Integer getRegionIds() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

}
