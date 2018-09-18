package org.gty.demo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;

public class DbUserDetails implements UserDetails {

    private static final long serialVersionUID = 7783210902028650561L;

    private String username;

    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    DbUserDetails(@Nonnull String username,
                  @Nonnull String password,
                  @Nonnull Collection<? extends GrantedAuthority> authorities) {
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

}
