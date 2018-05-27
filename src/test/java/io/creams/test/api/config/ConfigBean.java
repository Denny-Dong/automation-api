package io.creams.test.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "restful")
@Getter
@Setter
public class ConfigBean {
    private String clientId;
}
