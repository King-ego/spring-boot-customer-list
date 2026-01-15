package com.diego.list.customers.security;

import com.diego.list.customers.model.Session;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collection;


public class SessionAuthentication extends AbstractAuthenticationToken{
    private final Session session;

    public SessionAuthentication(Session session, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.session = session;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return session.getSessionId();
    }

    @Override
    public Object getPrincipal() {
        return session;
    }

    public Session getSession() {
        return session;
    }
}
