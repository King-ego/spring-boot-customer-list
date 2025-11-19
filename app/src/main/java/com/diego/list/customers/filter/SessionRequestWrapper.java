package com.diego.list.customers.filter;

import com.diego.list.customers.model.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

class SessionRequestWrapper extends HttpServletRequestWrapper {
    private final Session session;

    public SessionRequestWrapper(HttpServletRequest request, Session session) {
        super(request);
        this.session = session;
    }

    public Session getCustomSession() {
        return session;
    }

    @Override
    public Object getAttribute(String name) {
        if ("customSession".equals(name) || "session".equals(name)) {
            return session;
        }
        return super.getAttribute(name);
    }
}