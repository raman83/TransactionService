package com.transaction.mapper;

import com.transaction.dto.AmountDTO;
import com.transaction.dto.TransactionRequest;
import com.transaction.dto.TransactionResponse;
import com.transaction.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    // Request -> Entity: names mostly match; BigDecimal -> BigDecimal auto maps.
    Transaction toEntity(TransactionRequest request);

    // Entity -> Response: wrap BigDecimal into AmountDTO with default CAD.
    @Mappings({
        @Mapping(target = "amount",         expression = "java( toAmountDTO(entity.getAmount()) )"),
        @Mapping(target = "runningBalance", expression = "java( toAmountDTO(entity.getRunningBalance()) )")
    })
    TransactionResponse toResponse(Transaction entity);

    // Helper
    default AmountDTO toAmountDTO(BigDecimal value) {
        if (value == null) return null;
        return AmountDTO.builder().currency("CAD").value(value).build();
    }
}
