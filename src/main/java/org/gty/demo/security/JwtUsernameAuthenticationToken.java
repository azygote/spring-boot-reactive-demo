package org.gty.demo.security;

import com.google.common.base.Preconditions;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.io.Serial;
import java.util.Collection;

public class JwtUsernameAuthenticationToken extends AbstractAuthenticationToken {

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final Object principal;

    public JwtUsernameAuthenticationToken(final Object principal) {
        super(null);
        this.principal = principal;
        setAuthenticated(false);
    }

    public JwtUsernameAuthenticationToken(
        final Object principal,
        final Collection<? extends GrantedAuthority> authorities
    ) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {
        Preconditions.checkArgument(!isAuthenticated,
            "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
