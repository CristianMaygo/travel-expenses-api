package com.sura.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeExpenseDto {

    private String employeeName;
    private List<MonthlyExpenseDto> monthlyExpenses;
}
