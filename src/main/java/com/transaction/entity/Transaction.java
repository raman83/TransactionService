package com.transaction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transaction",
       indexes = {
           @Index(name = "idx_tx_account", columnList = "accountId"),
           @Index(name = "idx_tx_posted", columnList = "postedDate")
       },
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_tx_request_fp", columnNames = {"requestFingerprint"})
       })
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private UUID transactionId;
    private UUID accountId;

    private String type;     // DEBIT/CREDIT
    @Column(precision = 19, scale = 4)
    private BigDecimal amount;

    private OffsetDateTime postedDate;
    private OffsetDateTime transactionDate;

    private String description;
    private String status;   // PENDING/POSTED/etc
    private String category;

    @Column(precision = 19, scale = 4)
    private BigDecimal runningBalance;

    private String merchantName;
    private String referenceNumber;

    @Column(length = 64)
    private String requestFingerprint;

    @Version
    private Integer version;

    @PrePersist
    void prePersist() {
        if (postedDate == null) postedDate = OffsetDateTime.now();
        if (transactionDate == null) transactionDate = postedDate;
        if (version == null) version = 0;
    }
}