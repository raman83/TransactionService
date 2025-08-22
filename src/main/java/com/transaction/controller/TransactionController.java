// controller/TransactionController.java
package com.transaction.controller;

import com.transaction.dto.TransactionRequest;
import com.transaction.dto.TransactionResponse;
import com.transaction.entity.Transaction;
import com.transaction.mapper.TransactionMapper;
import com.transaction.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @PostMapping("/transactions")
    @PreAuthorize("hasAuthority('SCOPE_fdx:transactions.write')")
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionRequest request,
    		@RequestHeader(name = "Idempotency-Key", required = false) String idempotencyKey) {
    	Transaction tx = transactionMapper.toEntity(request);
    	tx.setTransactionId(UUID.randomUUID());
    	if (idempotencyKey != null && !idempotencyKey.isBlank()) {
    	tx.setRequestFingerprint(idempotencyKey.trim());
    	}
    	Transaction saved = transactionService.save(tx);
    	return ResponseEntity.status(HttpStatus.CREATED)
    	.eTag('"' + String.valueOf(saved.getVersion()) + '"')
    	.body(transactionMapper.toResponse(saved));
    	}
    
    
    
    @GetMapping("/transactions")
    @PreAuthorize("hasAuthority('SCOPE_fdx:transactions.read')")
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category) {

        Page<Transaction> page = transactionService.findTransactions(startDate, endDate, limit, offset, type, category);
        List<TransactionResponse> dtos = page.getContent().stream()
                .map(transactionMapper::toResponse)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/accounts/{accountId}/transactions")
    @PreAuthorize("hasAuthority('SCOPE_fdx:transactions.read')")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAccount(
            @PathVariable UUID accountId,
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category) throws AccessDeniedException {

        transactionService.validateOwnership(accountId, authHeader);

        Page<Transaction> page = transactionService.findTransactionsByAccount(accountId, startDate, endDate, limit, offset, type, category);
        List<TransactionResponse> dtos = page.getContent().stream()
                .map(transactionMapper::toResponse)
                .toList();

        return ResponseEntity.ok(dtos);
    }
    @GetMapping("/transactions/{transactionId}")
    @PreAuthorize("hasAuthority('SCOPE_fdx:transactions.read')")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable UUID transactionId) {
        Transaction transaction = transactionService.findById(transactionId);
        TransactionResponse dto = transactionMapper.toResponse(transaction);

        return ResponseEntity.ok(dto);
    }
    
}
