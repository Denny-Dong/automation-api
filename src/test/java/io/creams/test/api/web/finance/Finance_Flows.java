package io.creams.test.api.web.finance;

import com.google.gson.JsonObject;
import io.creams.test.api.HooksApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Finance_Flows {

	public class Comment_Flows {
		protected Logger logger = LoggerFactory.getLogger(getClass().getName());

		public JsonObject getCommentJson(String filepath) throws IOException {
			return HooksApi.driver.jsonObjectProducer(filepath);
		}

		public JsonObject putCommentJson(String filepath) throws IOException {
			return HooksApi.driver.jsonObjectProducer(filepath);
		}
	}
}
