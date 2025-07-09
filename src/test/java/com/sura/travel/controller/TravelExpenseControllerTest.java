package com.sura.travel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sura.travel.domain.Employee;
import com.sura.travel.domain.TravelExpense;
import com.sura.travel.dto.ExpenseSummaryDto;
import com.sura.travel.dto.SimpleExpenseRequestDto;
import com.sura.travel.dto.UpdateExpenseRequestDto;
import com.sura.travel.service.TravelExpenseService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TravelExpenseController.class)
class TravelExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TravelExpenseService travelExpenseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getExpenseSummary() throws Exception {
        ExpenseSummaryDto summaryDto = new ExpenseSummaryDto(
                new BigDecimal("100000"),
                Collections.emptyList()
        );

        when(travelExpenseService.getExpenseSummary()).thenReturn(summaryDto);

        mockMvc.perform(get("/api/expenses/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grandTotal").value("100000"));
    }

    @Test
    void getExpenseById() throws Exception {
        TravelExpense expense = new TravelExpense();
        expense.setId(1L);
        expense.setValue(new BigDecimal("300000"));
        expense.setExpenseDate(LocalDate.now());
        expense.setEmployee(new Employee("Juan"));

        when(travelExpenseService.getExpenseById(1L)).thenReturn(expense);

        mockMvc.perform(get("/api/expenses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(300000));
    }

    @Test
    void createSimpleExpense() throws Exception {
        SimpleExpenseRequestDto request = new SimpleExpenseRequestDto();
        request.setEmployeeName("Juan");
        request.setValue(new BigDecimal("300000"));

        TravelExpense createdExpense = new TravelExpense();
        createdExpense.setId(1L);
        createdExpense.setValue(request.getValue());
        createdExpense.setExpenseDate(LocalDate.now());
        createdExpense.setEmployee(new Employee(request.getEmployeeName()));

        when(travelExpenseService.createSimpleExpense(any())).thenReturn(createdExpense);

        mockMvc.perform(post("/api/expenses/simple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.value").value(300000));
    }

    @Test
    void updateExpense() throws Exception {
        UpdateExpenseRequestDto request = new UpdateExpenseRequestDto();
        request.setEmployeeName("Carlos");
        request.setValue(new BigDecimal("500000"));
        request.setExpenseDate(LocalDate.now());

        TravelExpense updatedExpense = new TravelExpense();
        updatedExpense.setId(1L);
        updatedExpense.setValue(request.getValue());
        updatedExpense.setExpenseDate(request.getExpenseDate());
        updatedExpense.setEmployee(new Employee(request.getEmployeeName()));

        when(travelExpenseService.updateExpense(any(Long.class), any(UpdateExpenseRequestDto.class)))
                .thenReturn(updatedExpense);

        mockMvc.perform(put("/api/expenses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(500000));
    }
}
