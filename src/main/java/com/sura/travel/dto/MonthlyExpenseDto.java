package com.sura.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyExpenseDto {

    private String month;
    private BigDecimal subtotal;
    private BigDecimal ivaAmount;
    private BigDecimal totalWithIva;
    private boolean suraAssumes;
}
