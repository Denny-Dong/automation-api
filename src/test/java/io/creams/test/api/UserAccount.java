package io.creams.test.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.creams.test.api.framework.Config;
import io.creams.test.api.framework.driver.APIDriver;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

public class UserAccount {

	private static Config config = Config.getInstance();
	private APIDriver driver;
	// TokenConfiguration
	private String accessToken;
	private String refreshToken;
	private Integer expiresIn;
	private Integer customerId;
	private Map<String, Object> userInfoMap = new HashMap<>();


	public UserAccount() {
		this.driver = HooksApi.driver;
		try {
			setToken();
			userInfoMap = this.initializeUserInfo(this.accessToken);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<String, Object> getUserInfoMap() {
		return userInfoMap;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = "Bearer " + accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setToken() throws IOException {
		HooksApi.logger.info("Start to get User Token");
		driver.switchToAppUrlWithoutAuthentication();
		Map<String, String> accountInfo = new HashMap<String, String>();
		accountInfo.put("clientId", config.get("restful.clientId"));
		accountInfo.put("username", config.get("login.user.name"));
		accountInfo.put("password", config.get("login.user.password"));
		Response response = given().body(accountInfo).when().post(config.get("restful.base.url.login"));
		response.then().statusCode(201).time(lessThan(10L), TimeUnit.SECONDS);
		JsonObject LoginResponsejsonObject = (JsonObject) (new JsonParser()).parse(response.getBody().asString());
		this.setAccessToken(LoginResponsejsonObject.get("accessToken").getAsString());
		this.refreshToken = LoginResponsejsonObject.get("refreshToken").getAsString();
		this.expiresIn = LoginResponsejsonObject.get("expiresIn").getAsInt();
		HooksApi.logger.info("Successful get User Token");
	}

	private Map<String, Object> initializeUserInfo(String accessTokenString) {
		Map<String, Claim> userTokenClaimMap = new HashMap<>();
		Map<String, Object> userInfoMap = new HashMap<>();

		try {
			DecodedJWT jwt = JWT.decode(this.accessToken.split(" ")[1]);
			userTokenClaimMap = jwt.getClaims();
		} catch (JWTDecodeException e) {
			e.printStackTrace();
		}
		userInfoMap.put("user_name", userTokenClaimMap.get("user_name").asString());
		userInfoMap.put("user", userTokenClaimMap.get("user").asMap());
		userInfoMap.put("authorities", userTokenClaimMap.get("authorities").asList(String.class));
		return userInfoMap;
	}

}
