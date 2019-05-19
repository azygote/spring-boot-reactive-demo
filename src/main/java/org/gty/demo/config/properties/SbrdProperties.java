package org.gty.demo.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;

@Component
@ConfigurationProperties(prefix = "sbrd")
public class SbrdProperties {

    private String storageContent;

    public String getStorageContent() {
        return storageContent;
    }

    public void setStorageContent(String storageContent) {
        this.storageContent = storageContent;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SbrdProperties.class.getSimpleName() + "[", "]")
                .add("storageContent='" + storageContent + "'")
                .toString();
    }
}
