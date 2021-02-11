package org.gty.demo.config.properties;

import com.google.common.base.MoreObjects;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;

@ConstructorBinding
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 2913237737054759921L;

    private final String publicKeyLocation;
    private final String privateKeyLocation;
    private final String issuer;
    private final String authorizationPrefix;
    private final Duration expiration;

    public JwtProperties(
        final String publicKeyLocation,
        final String privateKeyLocation,
        final String issuer,
        final String authorizationPrefix,
        final Duration expiration
    ) {
        this.publicKeyLocation = publicKeyLocation;
        this.privateKeyLocation = privateKeyLocation;
        this.issuer = issuer;
        this.authorizationPrefix = authorizationPrefix;
        this.expiration = expiration;
    }

    public String getPublicKeyLocation() {
        return publicKeyLocation;
    }

    public String getPrivateKeyLocation() {
        return privateKeyLocation;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getAuthorizationPrefix() {
        return authorizationPrefix;
    }

    public Duration getExpiration() {
        return expiration;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof JwtProperties)) return false;
        final var that = (JwtProperties) o;
        return Objects.equals(publicKeyLocation, that.publicKeyLocation) &&
            Objects.equals(privateKeyLocation, that.privateKeyLocation) &&
            Objects.equals(issuer, that.issuer) &&
            Objects.equals(authorizationPrefix, that.authorizationPrefix) &&
            Objects.equals(expiration, that.expiration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicKeyLocation, privateKeyLocation, issuer, authorizationPrefix, expiration);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("publicKeyLocation", publicKeyLocation)
            .add("privateKeyLocation", privateKeyLocation)
            .add("issuer", issuer)
            .add("authorizationPrefix", authorizationPrefix)
            .add("expiration", expiration)
            .toString();
    }
}
