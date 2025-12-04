package com.diego.list.customers.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class ChallengeServices {
    public String getChallengeEnglish() {
        return "Challenge endpoint is working!";
    }
}