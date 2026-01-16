package com.diego.list.customers.http.responses;
import com.diego.list.customers.model.Customer;
import com.diego.list.customers.model.Seller;
import com.diego.list.customers.model.User;
import com.diego.list.customers.model.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReturnUsers(
        UUID id,
        String name,
        String email,
        String phone,
        UserRole role,
        boolean enabled,
        boolean accountNonLocked,
        boolean credentialsNonExpired,
        boolean accountNonExpired,
        LocalDateTime createdAt,
        LocalDateTime lastLogin,
        String usualTimezone,
        String country,
        LocalDateTime birthDate,
        CustomerInfo customer,
        SellerInfo seller
    ) {

    public static ReturnUsers from(User user) {
        return new ReturnUsers(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.isEnabled(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                user.isAccountNonExpired(),
                user.getCreatedAt(),
                user.getLastLogin(),
                user.getUsualTimezone(),
                user.getCountry(),
                user.getBirthDate(),
                user.getCustomer() != null ? CustomerInfo.from(user.getCustomer()) : null,
                user.getSeller() != null ? SellerInfo.from(user.getSeller()) : null
        );
    }

    public record CustomerInfo(
            String document,
            Integer totalOrders,
            Double totalSpent,
            LocalDateTime lastPurchaseAt
    ){
        public static CustomerInfo from(Customer customer){
            return new CustomerInfo(
                    customer.getDocument(),
                    customer.getTotalOrders(),
                    customer.getTotalSpent(),
                    customer.getLastPurchaseAt()
            );
        }
    }

    public record SellerInfo(
            String storeName
    ){
        public static  SellerInfo from(Seller seller){
            return new SellerInfo(
                    seller.getStoreName()
            );
        }
    }


}