package org.gty.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration(proxyBeanMethods = false)
@EnableRetry(proxyTargetClass = true)
public class RetryConfig {
}
