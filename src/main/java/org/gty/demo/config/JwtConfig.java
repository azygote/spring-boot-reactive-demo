package org.gty.demo.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.impl.PublicClaims;
import org.gty.demo.config.properties.JwtProperties;
import org.gty.demo.constant.JwtConstants;
import org.gty.demo.security.util.SecurityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

@Configuration(proxyBeanMethods = false)
public class JwtConfig {

    @Bean
    public Algorithm algorithm(@Nonnull final JwtProperties jwtProperties)
        throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        Objects.requireNonNull(jwtProperties, "[jwtProperties] must not be null");

        return Algorithm.RSA512(
            SecurityUtils.readPublicKey(jwtProperties.getPublicKeyLocation()),
            SecurityUtils.readPrivateKey(jwtProperties.getPrivateKeyLocation())
        );
    }

    @Bean
    public JWTCreator.Builder jwt(@Nonnull final JwtProperties jwtProperties) {
        Objects.requireNonNull(jwtProperties, "[jwtProperties] must not be null");

        return JWT.create().withIssuer(jwtProperties.getIssuer());
    }

    @Bean
    public JWTVerifier jwtVerifier(
        @Nonnull final JwtProperties jwtProperties,
        @Nonnull final Algorithm algorithm
    ) {
        Objects.requireNonNull(jwtProperties, "[jwtProperties] must not be null");
        Objects.requireNonNull(algorithm, "[algorithm] must not be null");

        return JWT.require(algorithm)
            .withIssuer(jwtProperties.getIssuer())
            .withClaimPresence(PublicClaims.EXPIRES_AT)
            .withClaimPresence(JwtConstants.USERNAME)
            .build();
    }
}
