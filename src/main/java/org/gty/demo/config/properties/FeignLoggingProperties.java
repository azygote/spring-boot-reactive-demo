package org.gty.demo.config.properties;

import com.google.common.base.MoreObjects;
import feign.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.io.Serializable;
import java.util.Objects;

@ConstructorBinding
@ConfigurationProperties(prefix = "feign.logging")
public class FeignLoggingProperties implements Serializable {

    private static final long serialVersionUID = -4347495201799832999L;

    private final Logger.Level level;

    public FeignLoggingProperties(final Logger.Level level) {
        this.level = level != null ? level : Logger.Level.NONE;
    }

    public Logger.Level getLevel() {
        return level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FeignLoggingProperties)) return false;
        FeignLoggingProperties that = (FeignLoggingProperties) o;
        return level == that.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(level);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("level", level)
            .toString();
    }
}
