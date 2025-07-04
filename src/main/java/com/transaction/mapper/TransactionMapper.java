package com.transaction.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.transaction.dto.TransactionRequest;
import com.transaction.dto.TransactionResponse;
import com.transaction.entity.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "amount.currency", source = "currency")
    @Mapping(target = "amount.value", source = "amount")
    @Mapping(target = "runningBalance.currency", source = "currency")
    @Mapping(target = "runningBalance.value", source = "runningBalance")
    TransactionResponse toResponse(Transaction transaction);

    @Mapping(target = "transactionId", ignore = true)
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "runningBalance", source = "runningBalance")
    Transaction toEntity(TransactionRequest request);
}
