package org.gty.demo.security.util;

import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static RSAPublicKey readPublicKey(@Nonnull final String path) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        Objects.requireNonNull(path, "[path] must not be null");

        final Resource resource = new ClassPathResource(path);
        final var keyFactory = KeyFactory.getInstance("RSA");

        try (var pemReader = new PemReader(new BufferedReader(new InputStreamReader(resource.getInputStream())))) {
            var pemObject = pemReader.readPemObject();
            byte[] content = pemObject.getContent();
            var publicKeySpec = new X509EncodedKeySpec(content);
            return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
        }
    }

    public static RSAPrivateKey readPrivateKey(@Nonnull final String path) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        Objects.requireNonNull(path, "[path] must not be null");

        final Resource resource = new ClassPathResource(path);
        final var keyFactory = KeyFactory.getInstance("RSA");

        try (var pemReader = new PemReader(new BufferedReader(new InputStreamReader(resource.getInputStream())))) {
            var pemObject = pemReader.readPemObject();
            byte[] content = pemObject.getContent();
            var privateKeySpec = new PKCS8EncodedKeySpec(content);
            return (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
        }
    }
}
