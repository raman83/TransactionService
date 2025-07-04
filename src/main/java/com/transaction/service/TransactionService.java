package com.transaction.service;

import com.account.dto.AccountOwnerResponse;
import com.commons.exception.ResourceNotFoundException;
import com.commons.exception.UnauthorizedAccessException;
import com.transaction.client.AccountClient;
import com.transaction.entity.Transaction;
import com.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.data.domain.Sort;


import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;


import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class TransactionService {
	  private final TransactionRepository transactionRepository;
	  private final AccountClient accountClient;

	    public Page<Transaction> findTransactions(OffsetDateTime startDate, OffsetDateTime endDate, int limit, int offset,
	                                              String type, String category) {
	        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("postedDate").descending());
	        Specification<Transaction> spec = TransactionSpecifications.withFilters(startDate, endDate, type, category);

	        return transactionRepository.findAll(spec, pageable);
	    }

	    
	    
	    private String getCurrentCustomerId() {
	        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        if (principal instanceof Jwt jwt) {
	            return jwt.getSubject();
	        }
	        throw new UnauthorizedAccessException("Invalid or missing authentication token.");
	    }
	    
	    
	    public  void validateOwnership(UUID accountId, String token) throws AccessDeniedException {
	        AccountOwnerResponse owner = accountClient.getAccountOwner(accountId);
	        String currentUserId = getCurrentCustomerId();

	        if (!owner.getCustomerId().equals(currentUserId)) {
	            throw new UnauthorizedAccessException("You do not own this account.");
	        }
	    }

	    
	    public Page<Transaction> findTransactionsByAccount(UUID accountId, OffsetDateTime startDate, OffsetDateTime endDate,
	                                                      int limit, int offset, String type, String category) {
	        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("postedDate").descending());
	        Specification<Transaction> spec = Specification.where(TransactionSpecifications.accountEquals(accountId))
	                .and(TransactionSpecifications.withFilters(startDate, endDate, type, category));

	        return transactionRepository.findAll(spec, pageable);
	    }

	    public Transaction findById(UUID transactionId) {
	        return transactionRepository.findById(transactionId)
	                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found for id: " + transactionId));
	    }

	    public Transaction save(Transaction transaction) {
	        return transactionRepository.save(transaction);
	    }
}