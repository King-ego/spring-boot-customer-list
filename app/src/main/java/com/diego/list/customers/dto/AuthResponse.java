package com.diego.list.customers.dto;

import com.diego.list.customers.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private boolean success;
    private boolean requiresMFA;
    private String sessionId;
    private String mfaId;
    private String tempToken;
    private UserInfo user;
    private Integer riskScore;
    private List<String> riskFactors;
    private Boolean mfaVerified;
    private String message;
}