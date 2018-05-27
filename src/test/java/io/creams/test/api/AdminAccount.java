package io.creams.test.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.creams.test.api.framework.Config;
import io.creams.test.api.framework.driver.APIDriver;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

public class AdminAccount {

	private static final Logger logger = LoggerFactory.getLogger(HooksApi.class.getName());
	private static Config config = Config.getInstance();

	private APIDriver driver;

	public AdminAccount() {
		this.driver = HooksApi.driver;
		try {
			setToken();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// TokenConfiguration
	private String accessToken;
	private String refreshToken;
	private Integer expiresIn;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = "Bearer " + accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

	public void setToken() throws IOException {
		logger.info("Start to get Admin User Token");
		driver.switchToBackstageUrlWithoutAuthentication();
		JsonObject LoginRequestJsonObject = driver
				.jsonObjectProducer("jsonResources/backStage/account/AdminAccount.json");
		Response response = given().body(LoginRequestJsonObject.toString()).when()
				.post(config.get("restful.base.url.login"));
		response.then().statusCode(201).time(lessThan(10L), TimeUnit.SECONDS);
		JsonObject LoginResponsejsonObject = (JsonObject) (new JsonParser()).parse(response.getBody().asString());
		setAccessToken(LoginResponsejsonObject.get("accessToken").getAsString());
		setRefreshToken(LoginResponsejsonObject.get("refreshToken").getAsString());
		setExpiresIn(LoginResponsejsonObject.get("expiresIn").getAsInt());
		logger.info("Successful get Admin User Token");
	}

}
