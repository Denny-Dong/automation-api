package io.creams.test.api.web.buildings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermitPrice_Info implements Cloneable {
	private Integer buildingId;
	private Buildings_Flows buildings_Flows;
	private Integer customerId;
	private Integer floorId;
	private Map<String, Object> permitPriceBody;
	private Integer permitPriceId;
	private double price;
	private String priceUnit;
	private List<Integer> roomIdlist;
	private List<Integer> salsemanIdList;

	public PermitPrice_Info(Integer roomCount, double price, String priceUnit) {
		this.initPermitPrice_Info(roomCount, price, priceUnit);
	}

	@Override
	protected PermitPrice_Info clone() {
		PermitPrice_Info permitPrice_InfoClone = null;
		try {
			permitPrice_InfoClone = (PermitPrice_Info) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return permitPrice_InfoClone;
	}

	public Integer getBuildingId() {
		return buildingId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public Map<String, Object> getPermitPriceBody() {
		return permitPriceBody;
	}

	private Map<String, Object> getPermitPriceBody(Double price, String priceUnit, Integer buildingId,
			List<Integer> roomIdlist, List<Integer> salsemanIdList) {
		@SuppressWarnings("unchecked")
		Map<String, Object> userInfoMap = (Map<String, Object>) Buildings_Hooks.userAccount.getUserInfoMap()
				.get("user");
		Integer customerId = (Integer) userInfoMap.get("customerId");
		Map<String, Object> permitBody = new HashMap<>();
		permitBody.put("price", price);
		permitBody.put("priceUnit", priceUnit);
		permitBody.put("buildingId", buildingId);
		permitBody.put("roomIds", roomIdlist);
		permitBody.put("userIds", salsemanIdList);
		permitBody.put("customerId", customerId);
		return permitBody;
	}

	public Integer getPermitPriceId() {
		return permitPriceId;
	}

	public Map<String, Object> getPermitPriceInfoMap() {
		Map<String, Object> permitPriceMap = new HashMap<>();
		permitPriceMap.put("priceUnit", this.priceUnit);
		permitPriceMap.put("roomIdList", this.roomIdlist);
		permitPriceMap.put("price", this.price);
		permitPriceMap.put("usersIdList", this.salsemanIdList);
		permitPriceMap.put("buildingId", this.buildingId);
		return permitPriceMap;
	}

	private Integer initCustomerId() {
		@SuppressWarnings("unchecked")
		Map<String, Object> userInfoMap = (Map<String, Object>) Buildings_Hooks.userAccount.getUserInfoMap()
				.get("user");
		return (Integer) userInfoMap.get("customerId");
	}

	private void initPermitPrice_Info(Integer roomCount, double price, String priceUnit) {
		this.buildings_Flows = new Buildings_Flows();
		this.buildingId = buildings_Flows.getBuildingIdRandom();
		this.floorId = buildings_Flows.getFloorId(roomCount, buildingId);
		this.roomIdlist = buildings_Flows.getRoomIdList(floorId);
		this.salsemanIdList = buildings_Flows.getSalesmanIdList(buildingId, floorId, roomIdlist);
		this.permitPriceBody = this.getPermitPriceBody(price, priceUnit, buildingId, roomIdlist, salsemanIdList);
		this.priceUnit = priceUnit;
		this.price = price;
		this.customerId = this.initCustomerId();
	}

	public void setPermitPriceId(Integer permitPriceId) {
		this.permitPriceId = permitPriceId;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
