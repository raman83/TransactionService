package com.transaction.service;

import org.springframework.data.jpa.domain.Specification;

import com.transaction.entity.Transaction;

import jakarta.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class TransactionSpecifications {

    public static Specification<Transaction> withFilters(OffsetDateTime startDate, OffsetDateTime endDate, String type, String category) {
        return (root, query, cb) -> {
            Predicate p = cb.conjunction();

            if (startDate != null) {
                p = cb.and(p, cb.greaterThanOrEqualTo(root.get("postedDate"), startDate));
            }
            if (endDate != null) {
                p = cb.and(p, cb.lessThanOrEqualTo(root.get("postedDate"), endDate));
            }
            if (type != null && !type.isEmpty()) {
                p = cb.and(p, cb.equal(root.get("type"), type));
            }
            if (category != null && !category.isEmpty()) {
                p = cb.and(p, cb.equal(root.get("category"), category));
            }
            return p;
        };
    }

    public static Specification<Transaction> accountEquals(UUID accountId) {
        return (root, query, cb) -> cb.equal(root.get("accountId"), accountId);
    }
}
