package io.creams.test.api.web.template;

public class Template_Info {
	private String templateName;
	private Integer BuildingId = null;
	private Integer customerId = null;
	private Integer TemplateId = null;

	public Integer getTemplateId() {
		return TemplateId;
	}

	public void setTemplateId(Integer templateId) {
		TemplateId = templateId;
	}

	public Template_Info(String templateName) {
		super();
		this.templateName = templateName;
	}

	public Integer getBuildingId() {
		return BuildingId;
	}

	public void setBuildingId(Integer buildingId) {
		BuildingId = buildingId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

}
