package com.diego.list.customers.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MFAVerificationRequest {
    @NotBlank(message = "MFA ID é obrigatório")
    private String mfaId;

    @NotBlank(message = "Código é obrigatório")
    private String code;

    @NotBlank(message = "Token temporário é obrigatório")
    private String tempToken;
}