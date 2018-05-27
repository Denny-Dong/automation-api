package io.creams.test.api.web.businessCommon;

public class Category_info {
	private String categoryEnum = null;
	private String name = null;
	private long id = 0L;
	
	
	public Category_info() {
		
	}

	public Category_info(String categoryEnum, String name) {
		super();
		this.categoryEnum = categoryEnum;
		this.name = name;
	}

	public String getCategoryEnum() {
		return categoryEnum;
	}


	public void setCategory(String categoryEnum) {
		this.categoryEnum = categoryEnum;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}
}
