package com.sura.travel.controller;

import com.sura.travel.domain.TravelExpense;
import com.sura.travel.dto.ExpenseSummaryDto;
import com.sura.travel.dto.SimpleExpenseRequestDto;
import com.sura.travel.service.TravelExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expenses")
public class TravelExpenseController {

    @Autowired
    private TravelExpenseService travelExpenseService;

    @GetMapping("/summary")
    public ResponseEntity<ExpenseSummaryDto> getExpenseSummary() {
        ExpenseSummaryDto summary = travelExpenseService.getExpenseSummary();
        return ResponseEntity.ok(summary);
    }

    @PostMapping("/simple")
    @ResponseStatus(HttpStatus.CREATED)
    public TravelExpense createSimpleExpense(@Valid @RequestBody SimpleExpenseRequestDto expenseRequets) {
        return travelExpenseService.createSimpleExpense(expenseRequets);
    }

}
