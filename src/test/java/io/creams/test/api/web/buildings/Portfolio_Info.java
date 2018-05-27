package io.creams.test.api.web.buildings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Portfolio_Info {
	private String portfolioName = null;
	private Integer portfolioID = null;
	private List<Integer> portfolioBuildingIdList = new ArrayList<Integer>();
	private List<Integer> BuildingIDList = new ArrayList<>();

	public Portfolio_Info(String portfolioName) {
		super();
		this.portfolioName = portfolioName;
	}

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public Integer getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Integer portfolioID) {
		this.portfolioID = portfolioID;
	}

	public List<Integer> getPortfolioBuildingIdList() {
		return portfolioBuildingIdList;
	}

	public void setPortfolioBuildingIdList(List<Integer> portfolioBuildingIdList) {
		this.portfolioBuildingIdList = portfolioBuildingIdList;
	}

	public List<Integer> getBuildingIDList() {
		return BuildingIDList;
	}

	public void setBuildingIDList(List<Integer> buildingIDList) {
		BuildingIDList = buildingIDList;
		Collections.sort(BuildingIDList);
	}

}
