package com.transaction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transactions", indexes = {
    @Index(name = "idx_account_posted_date", columnList = "accountId, postedDate")
})
public class Transaction {

	@Id
    @Column(name = "transaction_id", nullable = false, updatable = false)
    private UUID transactionId;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private OffsetDateTime postedDate;

    @Column(nullable = false)
    private OffsetDateTime transactionDate;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private String status;

    @Column(length = 100)
    private String category;

    @Column(precision = 19, scale = 4)
    private BigDecimal runningBalance;

    @Column(length = 100)
    private String merchantName;

    @Column(length = 100)
    private String referenceNumber;
}
