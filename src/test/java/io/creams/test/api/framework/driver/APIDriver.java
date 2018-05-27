package io.creams.test.api.framework.driver;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import io.creams.test.api.framework.Config;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.lessThan;

public class APIDriver {

	private static final Logger logger = LoggerFactory.getLogger(APIDriver.class.getName());
	private static Config config = Config.getInstance();

	public APIDriver() {
		this.initRestfulSetting();
	}

	private Long responseTimelimit = Long.parseLong(Config.getInstance().get("restful.verify.response.time.duration"));

	public void initRestfulSetting() {
		RestAssured.baseURI = config.get("restful.url.web");
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
		RestAssured.responseSpecification = new ResponseSpecBuilder()
				.expectResponseTime(lessThan(responseTimelimit), TimeUnit.SECONDS).build();
	}

	public void switchToAppUrlWithAuthentication(String stringToken) {
		RestAssured.reset();
		RestAssured.baseURI = config.get("restful.base.url.app");
		// RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON)
				.addHeader("Authorization", stringToken).build();
		RestAssured.responseSpecification = new ResponseSpecBuilder()
				.expectResponseTime(lessThan(responseTimelimit), TimeUnit.SECONDS).build();
	}

	public void switchToAppUrlWithoutAuthentication() {
		RestAssured.reset();
		RestAssured.baseURI = config.get("restful.base.url.app");
		// RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
		RestAssured.responseSpecification = new ResponseSpecBuilder()
				.expectResponseTime(lessThan(responseTimelimit), TimeUnit.SECONDS).build();
	}

	public void switchToAgentUrlWithoutAuthentication() {
		RestAssured.reset();
		RestAssured.baseURI = config.get("restful.base.url.agent");
		// RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
		RestAssured.responseSpecification = new ResponseSpecBuilder()
				.expectResponseTime(lessThan(responseTimelimit), TimeUnit.SECONDS).build();
	}

	public void switchToAgentUrlWithAuthentication(String stringToken) {
		RestAssured.reset();
		RestAssured.baseURI = config.get("restful.base.url.agent");
		// RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON)
				.addHeader("Authorization", stringToken).build();
		RestAssured.responseSpecification = new ResponseSpecBuilder()
				.expectResponseTime(lessThan(responseTimelimit), TimeUnit.SECONDS).build();
	}

	public void switchToBackstageUrlWithAuthentication(String stringToken) {
		RestAssured.reset();
		RestAssured.baseURI = config.get("restful.base.url.backstage");
		// RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON)
				.addHeader("Authorization", stringToken).build();
		RestAssured.responseSpecification = new ResponseSpecBuilder()
				.expectResponseTime(lessThan(responseTimelimit), TimeUnit.SECONDS).build();
	}

	public void switchToBackstageUrlWithoutAuthentication() {
		RestAssured.reset();
		RestAssured.baseURI = config.get("restful.base.url.backstage");
		// RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
		RestAssured.responseSpecification = new ResponseSpecBuilder()
				.expectResponseTime(lessThan(responseTimelimit), TimeUnit.SECONDS).build();
	}

	public void createNewDriverInstance() throws Exception {
		logger.info("Initialize Restful API Driver");
	}

	public void destroyDriver() {

	}

	public JsonObject jsonObjectProducer(String filepath) throws IOException {
		String fileReader = new String(
				Files.readAllBytes(Paths.get(Config.getInstance().get("restful.base.url.json") + filepath)),
				StandardCharsets.UTF_8);
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = (JsonObject) jsonParser.parse(fileReader);
		return jsonObject;

	}

	public Map<String, Object> mapObjectProducer(String filepath) throws IOException {
		JsonObject jsonObject = jsonObjectProducer(filepath);
		Type type = new TypeToken<Map<String, Object>>() {
		}.getType();
		Gson gson = new Gson();
		Map<String, Object> mapObject = gson.fromJson(jsonObject, type);
		return mapObject;

	}

}
