package com.sura.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseSummaryDto {

    private BigDecimal grandTotal;
    private List<EmployeeExpenseDto> employeeSummaries;
}
