package org.gty.demo.security;

import com.google.common.base.MoreObjects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.util.Collection;
import java.util.Objects;

public class DbUserDetails implements UserDetails {

    @Serial
    private static final long serialVersionUID = 7783210902028650561L;

    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    DbUserDetails(@Nonnull final String username,
                  @Nonnull final String password,
                  @Nonnull final Collection<? extends GrantedAuthority> authorities) {
        this.username = Objects.requireNonNull(username, "[username] must not be null");
        this.password = Objects.requireNonNull(password, "[password] must not be null");
        this.authorities = Objects.requireNonNull(authorities, "[authorities] must not be null");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof DbUserDetails)) return false;
        final var that = (DbUserDetails) o;
        return username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("username", username)
                .add("password", password)
                .add("authorities", authorities)
                .toString();
    }
}
