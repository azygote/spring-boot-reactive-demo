package org.gty.demo.config.properties;

import com.google.common.base.MoreObjects;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Objects;

@ConstructorBinding
@ConfigurationProperties(prefix = "sbrd")
public class SbrdProperties implements Serializable {

    private static final long serialVersionUID = -1183042085547036856L;

    private final String storageContent;

    public SbrdProperties(String storageContent) {
        this.storageContent = storageContent;
    }

    public String getStorageContent() {
        return storageContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SbrdProperties)) return false;
        SbrdProperties that = (SbrdProperties) o;
        return Objects.equals(storageContent, that.storageContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storageContent);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("storageContent", storageContent)
            .toString();
    }
}
