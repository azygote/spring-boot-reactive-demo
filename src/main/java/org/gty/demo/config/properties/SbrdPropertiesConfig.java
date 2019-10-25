package org.gty.demo.config.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SbrdProperties.class)
public class SbrdPropertiesConfig {
}
