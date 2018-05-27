package io.creams.test.api.framework;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;

public class Config {
	private static final Logger logger = LoggerFactory.getLogger(Config.class.getName());
	// Browser Type
	public static final String FIREFOX = "firefox";
	public static final String IE = "ie";
	public static final String CHROME = "chrome";
	public static final String SAFARI = "safari";
	public static final String EDGE = "edge";
	public static final String OPERA = "opera";

	public static final String IOS = "iOS";
	public static final String ANDROID = "android";

	public static final String IPAD = "ipad";
	public static final String MOBILE = "mobile";
	public static final String WEB = "web";

	private static String configFilePath = "config.properties";
	private static String configLoadError;
	private static final String CONFIG_ENV_NAME = "config";

	private static Config _instance = null;

	private Properties props = null;

	private Config() {
		String configFile = getValue(Config.CONFIG_ENV_NAME);
		if (StringUtils.isBlank(configFile)) {
			loadConfigFile(Config.configFilePath);
		} else {
			loadConfigFile(configFile);
		}

	}

	public synchronized static Config getInstance() {
		if (Config._instance == null) {
			Config._instance = new Config();
		}
		return Config._instance;
	}

	public String get(final String key) {
		String value = null;
		if (this.props.containsKey(key)) {
			value = (String) this.props.get(key);
		} else {
			// the property is absent
			Config.logger.info("The property is absent - " + key);
		}

		String propertyValue = System.getProperty(key);
		// Update by Ray to get every config from property or
		// environment variable
		if (!StringUtils.isBlank(propertyValue)) {
			value = propertyValue;
			Config.logger.info(String.format("Load property from System property : %s - %s", key, propertyValue));
		} else {
			String envValue = System.getenv(key);
			if (!StringUtils.isBlank(envValue)) {
				value = envValue;
				Config.logger
						.info(String.format("Load property from environment variable : %s - %s", key, propertyValue));
			}
		}

		return value;
	}

	private void loadConfigFile(String configFile) {
		Config.configLoadError = null;
		String configPath = getResourcePath(System.getProperty("config"));
		configFile = getResourcePath(configFile);
		Config.logger.info("Config file loading...... " + configFile);
		// load config file
		this.props = new Properties();
		try {
			if (StringUtils.isBlank(configPath) || !new File(configPath).exists()) {
				String envPath = getResourcePath(System.getenv("config"));
				// value from environment
				if (StringUtils.isBlank(envPath) || !new File(envPath).exists()) {
					configPath = configFile;
					Config.logger.info("Loading config from specified file: " + configFile);
				} else {
					configPath = envPath;
					Config.logger.info("Loading config from System Environment - config: " + configFile);
				}
			} else {
				Config.logger.info("Loading config from System Property - config: " + configFile);
			}
			if (StringUtils.isBlank(configPath)) {
				Config.logger.info("Loading config terminated as the path is blank.");
				return;
			}
			if (!new File(configPath).exists()) {
				Config.configLoadError = "Loading config terminated as file NOT exist!";
				Config.logger.error(Config.configLoadError);
				return;
			}
			Config.configFilePath = configPath;
			FileInputStream input = null;
			try {
				input = new FileInputStream(configPath);
				this.props.load(input);
			} finally {
				if (input != null) {
					input.close();
				}
			}
		} catch (Exception e) {
			Config.configLoadError = "Error on loading config: " + configPath;
			Config.logger.error(Config.configLoadError, e);
		}
	}

	public boolean isMobile() {
		return this.get("app.type").equals("mobile");
	}

	public boolean isBrowser() {
		return this.get("app.type").equals("browser");
	}

	public boolean isAndroid() {
		return this.get("mobile.device.type").equalsIgnoreCase("android")
				&& this.get("app.type").equalsIgnoreCase("mobile");
	}

	public boolean isIOS() {
		return this.get("mobile.device.type").equalsIgnoreCase("ios")
				&& this.get("app.type").equalsIgnoreCase("mobile");
	}

	public String getResourcePath(final String path) {
		if (StringUtils.isBlank(path)) {
			return "";
		} else {
			String absPath = getClass().getClassLoader().getResource(path).getFile();
			String decoded = null;
			try {
				decoded = URLDecoder.decode(absPath, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return decoded;
		}
	}

	private static String getValue(final String key) {
		String value = "";
		String env = System.getenv(key);
		String property = System.getProperty(key);
		if (!StringUtils.isBlank(property)) {
			value = property;
		} else if (!StringUtils.isBlank(env)) {
			value = env;
		}
		return value;
	}

	public String getPlatformName() {
		return this.get("mobile.device.type");
	}
}
