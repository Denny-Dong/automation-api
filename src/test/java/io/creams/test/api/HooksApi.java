package io.creams.test.api;

import io.creams.test.api.framework.driver.APIDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HooksApi {
	protected static final Logger logger = LoggerFactory.getLogger(HooksApi.class.getName());
	public static APIDriver driver = new APIDriver();

	public HooksApi() {
		if (driver == null) {
			throw new UnsupportedOperationException();
		}
		try {
			HooksApi.initBaseStep();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void destroyDriverFactory() {
		HooksApi.driver.destroyDriver();
		HooksApi.driver = null;

	}

	public static void initBaseStep() throws Exception {

	}

}
