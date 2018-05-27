package io.creams.test.api.web.contractsv2;

public class ContractData {
	//合同姓名
	private String ContractName;
	//合同id
	private Integer ContractID;
	//楼宇id
	private Integer buildingID;
	//房源id
	private Integer roomID;
	//作废协议id
	private Integer invalidContractId = null;
	//退租协议Id
	private Integer terminationId = null;
	
	public ContractData(String contractName) {
		super();
		ContractName = contractName;
	}
	
	public String getContractName() {
		return ContractName;
	}
	public void setContractName(String contractName) {
		ContractName = contractName;
	}
	public Integer getContractID() {
		return ContractID;
	}
	public void setContractID(Integer contractID) {
		ContractID = contractID;
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
	public Integer getInvalidContractId() {
		return invalidContractId;
	}
	public void setInvalidContractId(Integer invalidContractId) {
		this.invalidContractId = invalidContractId;
	}
	public Integer getTerminationId() {
		return terminationId;
	}
	public void setTerminationId(Integer terminationId) {
		this.terminationId = terminationId;
	}
}
