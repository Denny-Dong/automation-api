package io.creams.test.api.web.businessCommon;
/**
 * 标签类
 * @author silence
 *
 */
public class Tags_Info {
	private String name = null;
	private long tagId = 0;
	private String typeEnum = null;
	
	public Tags_Info() {
		
	}

	public Tags_Info(String name, String typeEnum) {
		super();
		this.name = name;
		this.typeEnum = typeEnum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTagId() {
		return tagId;
	}

	public void setTagId(long tagId) {
		this.tagId = tagId;
	}

	public String getTypeEnum() {
		return typeEnum;
	}

	public void setTypeEnum(String typeEnum) {
		this.typeEnum = typeEnum;
	}
}
