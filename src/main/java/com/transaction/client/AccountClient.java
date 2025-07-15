package com.transaction.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.account.dto.AccountOwnerResponse;

@FeignClient(name = "account-service", url = "${account.service.url}")
public interface AccountClient {

    @GetMapping("/api/v1/accounts/{id}/owner")
    public AccountOwnerResponse getAccountOwner(@PathVariable("id") UUID id);
}