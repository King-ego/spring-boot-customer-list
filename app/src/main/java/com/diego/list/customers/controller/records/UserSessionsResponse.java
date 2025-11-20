package com.diego.list.customers.controller.records;

import com.diego.list.customers.model.Session;

import java.util.List;
import java.util.UUID;

public record UserSessionsResponse(UUID userId, List<Session> sessions) {}