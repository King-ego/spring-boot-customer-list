package com.diego.list.customers.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@NoArgsConstructor
@Controller
@Slf4j
@RequestMapping("/orders")
public class OrderController {
    @PostMapping
    public String createOrder(){
        return "Order created";
    }
}
