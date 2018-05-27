package io.creams.test.api.web.template;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class Template_Steps extends Template_Hooks {
	private Template_Flows template_Flows;

	public Template_Steps() {
		super();
		template_Flows = new Template_Flows();
	}

	@Test
	// 合同模板下，特定楼宇的关键词
	public void contracts_buildingIds_keyword_a() throws IOException {
		String BuildingsIdString = template_Flows.getBuildingIdList().get(1).toString();
		int customerId = template_Flows.getCustomerId();
		Template_Hooks.template_Info.setBuildingId(Integer.parseInt(BuildingsIdString));
		Template_Hooks.template_Info.setCustomerId(customerId);
		given().queryParams("customerId", customerId, "buildingId", Template_Hooks.template_Info.getBuildingId(),
				"agreementType", "CONTRACT").when().get("contracts/buildingIds/keyword").then().assertThat()
				.statusCode(200).body(matchesJsonSchemaInClasspath(
						"jsonResources/schema/web/template/contracts_buildingIds_keyword.json"));

	}

	@Test
	// 根据业主合同模版通过关键词
	public void contracts_buildingIds_keyword_b() {
		given().queryParams("buildingId", Template_Hooks.template_Info.getBuildingId(), "customerId",
				Template_Hooks.template_Info.getCustomerId(), "agreementType", "CONTRACT").when()
				.get("/contracts/keyword/template").then().assertThat().statusCode(200)
				.body(matchesJsonSchemaInClasspath(
						"jsonResources/schema/web/template/contracts_keyword_template.json"));
	}

	@Test
	// 获取合同模版中，特定楼宇的审核人关键词
	public void contracts_buildingIds_keyword_c() {
		given().queryParam("buildingId", Template_Hooks.template_Info.getBuildingId(), "agreementType", "CONTRACT")
				.when().get("/contracts/keyword/workflow").then().assertThat().statusCode(200)
				.body(matchesJsonSchemaInClasspath(
						"jsonResources/schema/web/template/contracts_keyword_workflow.json"));
	}

	@Test
	// 获取大业主下所有合同模板列表
	public void contracts_templates_b() {
		List<Map<String, Object>> templatesList = given().queryParam("agreementType", "CONTRACT").when()
				.get("/contracts/templates").then().assertThat().statusCode(200).extract().path("");
		Map<String, Object> templatesMap = templatesList.get(templatesList.size() - 1);
		Template_Hooks.template_Info.setTemplateId(Integer.parseInt(templatesMap.get("id").toString()));
	}

	@Test
	// 新建合同模板
	public void contracts_templates_a() {
		String name = Long.toString(System.currentTimeMillis());
		given().queryParams("word", "jsonResources/web/template/合同模板.docx", "pdf",
				"jsonResources/web/template/合同模板.docx", "name", name, "agreementType", "CONTRACT").when()
				.post("/contracts/templates").then().assertThat().statusCode(200);

	}

	@Test
	// 删除合同模板
	public void contracts_templates_c() {
		int TemplateId = Template_Hooks.template_Info.getTemplateId();
		given().pathParam("id", TemplateId).when().delete("/contracts/templates/{id}").then().assertThat()
				.statusCode(200);
	}
}
