package com.diego.list.customers.application.command.users;

import com.diego.list.customers.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSellerCommand {
    private User user;

    private String storeName;

    private String documentNumber;

    private String storeDescription;
}
