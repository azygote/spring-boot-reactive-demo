package org.gty.demo.config.properties;

import com.google.common.base.MoreObjects;
import feign.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Objects;

@ConstructorBinding
@ConfigurationProperties(prefix = "feign")
public class FeignProperties implements Serializable {

    private static final long serialVersionUID = -4347495201799832999L;

    private final Logger.Level logLevel;
    private final FeignEncoder encoder;
    private final FeignDecoder decoder;
    private final FeignClient client;

    public FeignProperties(@Nullable final Logger.Level logLevel,
                           @Nullable final FeignEncoder encoder,
                           @Nullable final FeignDecoder decoder,
                           @Nullable final FeignClient client) {
        this.logLevel = logLevel;
        this.encoder = encoder;
        this.decoder = decoder;
        this.client = client;
    }

    public Logger.Level getLogLevel() {
        return logLevel;
    }

    public FeignEncoder getEncoder() {
        return encoder;
    }

    public FeignDecoder getDecoder() {
        return decoder;
    }

    public FeignClient getClient() {
        return client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FeignProperties)) return false;
        FeignProperties that = (FeignProperties) o;
        return logLevel == that.logLevel &&
            encoder == that.encoder &&
            decoder == that.decoder &&
            client == that.client;
    }

    @Override
    public int hashCode() {
        return Objects.hash(logLevel, encoder, decoder, client);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("logLevel", logLevel)
            .add("encoder", encoder)
            .add("decoder", decoder)
            .add("client", client)
            .toString();
    }

    public enum FeignEncoder {
        DEFAULT,
        GSON,
        JACKSON
    }

    public enum FeignDecoder {
        DEFAULT,
        GSON,
        JACKSON
    }

    public enum FeignClient {
        DEFAULT,
        HTTP_2_CLIENT,
        OKHTTP
    }
}
