package com.nl.bank.digibank.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
 
import java.io.Serial;
import java.util.Collection;
 
@Getter
public class LocalUser extends org.springframework.security.core.userdetails.User {
 
    @Serial
    private static final long serialVersionUID = -2845160792248762779L;
 
    public LocalUser(final String userID, final String password, final boolean enabled, final boolean accountNonExpired, final boolean credentialsNonExpired,
                     final boolean accountNonLocked, final Collection<? extends GrantedAuthority> authorities) {
        super(userID, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}