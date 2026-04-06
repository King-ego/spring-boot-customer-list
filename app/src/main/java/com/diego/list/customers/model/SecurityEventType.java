package com.diego.list.customers.model;

public enum SecurityEventType {
    LOGIN_SUCCESS,
    LOGIN_FAILURE,
    LOGOUT,
    SESSION_REVOKED,
    MFA_VERIFICATION,
    PASSWORD_CHANGE,
    SUSPICIOUS_ACTIVITY,
    ACCOUNT_LOCKED
}
