package com.sura.travel.controller;

import com.sura.travel.domain.TravelExpense;
import com.sura.travel.dto.EmployeeExpenseDto;
import com.sura.travel.service.ServiceTravelExpense;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/travel-expenses")
public class ControllerTravelExpense {

    private final ServiceTravelExpense serviceTravelExpense;

    public ControllerTravelExpense(ServiceTravelExpense serviceTravelExpense) {
        this.serviceTravelExpense = serviceTravelExpense;
    }

    @GetMapping
    public ResponseEntity<List<TravelExpense>> getAllExpenses() {
        return ResponseEntity.status(HttpStatus.OK).body(
                serviceTravelExpense.getAllExpenses()
        );
    }
}
