package com.diego.list.customers.controller;

import com.diego.list.customers.services.ChallengeServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/challenges")
@Slf4j
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeServices challengeServices;

    @GetMapping("/:user_id")
    public String getChallenge(@Valid @PathVariable UUID customer_id) {
        return challengeServices.getChallengeEnglish();
    }
}
