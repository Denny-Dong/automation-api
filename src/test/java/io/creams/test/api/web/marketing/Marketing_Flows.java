package io.creams.test.api.web.marketing;

import com.google.gson.JsonObject;
import io.creams.test.api.HooksApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Marketing_Flows {
	protected Logger logger = LoggerFactory.getLogger(getClass().getName());
	
	public JsonObject getChannelContactJson(String filepath) throws IOException{
		return HooksApi.driver.jsonObjectProducer(filepath);
	}
}
