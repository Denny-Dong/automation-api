package io.creams.test.api.web.propertyContracts;

public class PropertyContract_Info {
	private String propertyContractName = null;
	private Integer propertyContractID = null;
	private Integer buildingID = null;
	private Integer roomID = null;
	private Integer nullificationTermId = null;
	private Integer terminationTermId = null;
	private Integer tenantId = null;
	private String tenantName = null;

	public PropertyContract_Info(String propertyContractName) {
		super();
		this.propertyContractName = propertyContractName;
	}

	public Integer getPropertyContractID() {
		return propertyContractID;
	}

	public void setPortfolioID(Integer propertyContractID) {
		this.propertyContractID = propertyContractID;
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

	public Integer getNullificationTermId() {
		return nullificationTermId;
	}

	public void setNullificationTermId(Integer nullificationTermId) {
		this.nullificationTermId = nullificationTermId;
	}

	public Integer getTerminationTermId() {
		return terminationTermId;
	}

	public void setTerminationTermId(Integer terminationTermId) {
		this.terminationTermId = terminationTermId;
	}

	public Integer getTenantId() {
		return tenantId;
	}

	public void setTenantId(Integer tenantId) {
		this.tenantId = tenantId;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

}
