package com.diego.list.customers.command.createUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSellerCommand {
    private UUID userId;

    private String storeName;

    private String documentNumber;

    private String storeDescription;
}
