package com.sura.travel.service;

import com.sura.travel.domain.Employee;
import com.sura.travel.domain.TravelExpense;

import com.sura.travel.dto.*;
import com.sura.travel.exception.ResourceNotFoundException;
import com.sura.travel.dto.SimpleExpenseRequestDto;
import com.sura.travel.repository.EmployeeRepository;
import com.sura.travel.repository.TravelExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TravelExpenseService {

    private static final BigDecimal IVA_RATE = new BigDecimal("0.19");
    private static final BigDecimal EXPENSE_LIMIT = new BigDecimal("1000000.00");

    @Autowired
    private TravelExpenseRepository travelExpenseRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    public ExpenseSummaryDto getExpenseSummary() {
        List<TravelExpense> allExpenses = travelExpenseRepository.findAllByOrderByEmployee_NameAsc();

        BigDecimal grandTotal = allExpenses.stream()
                .map(TravelExpense::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, List<TravelExpense>> expensesByEmployee = allExpenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getEmployee().getName(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<EmployeeExpenseDto> employeeSummaries = new ArrayList<>();

        for (Map.Entry<String, List<TravelExpense>> entry : expensesByEmployee.entrySet()) {
            String employeeName = entry.getKey();
            List<TravelExpense> employeeExpenses = entry.getValue();

            Map<Month, List<TravelExpense>> expensesByMonth = employeeExpenses.stream()
                    .collect(Collectors.groupingBy(
                            expense -> expense.getExpenseDate().getMonth()
                    ));

            List<MonthlyExpenseDto> monthlyExpenses = new ArrayList<>();

            for (Map.Entry<Month, List<TravelExpense>> monthEntry : expensesByMonth.entrySet()) {
                Month month = monthEntry.getKey();
                List<TravelExpense> monthExpenses = monthEntry.getValue();

                BigDecimal monthlySubtotal = monthExpenses.stream()
                        .map(TravelExpense::getValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal ivaAmount = monthlySubtotal.multiply(IVA_RATE).setScale(2, RoundingMode.HALF_UP);
                BigDecimal totalWithIva = monthlySubtotal.add(ivaAmount);
                boolean suraAssumes = totalWithIva.compareTo(EXPENSE_LIMIT) > 0;

                monthlyExpenses.add(new MonthlyExpenseDto(
                        month.toString(),
                        monthlySubtotal.setScale(2, RoundingMode.HALF_UP),
                        ivaAmount,
                        totalWithIva.setScale(2, RoundingMode.HALF_UP),
                        suraAssumes
                ));
            }

            employeeSummaries.add(new EmployeeExpenseDto(employeeName, monthlyExpenses));
        }

        return new ExpenseSummaryDto(grandTotal.setScale(2, RoundingMode.HALF_UP), employeeSummaries);
    }

    public TravelExpense getExpenseById(Long expenseId) {
        return travelExpenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto no encontrado con id: " + expenseId));
    }

    // --- MÉTODO CREATE ---
    public TravelExpense createSimpleExpense(SimpleExpenseRequestDto request) {
        Employee employee = employeeRepository.findByName(request.getEmployeeName())
                .orElseGet(() -> {
                    Employee newEmployee = new Employee(request.getEmployeeName());
                    return employeeRepository.save(newEmployee);
                });

        TravelExpense newExpense = new TravelExpense();
        newExpense.setEmployee(employee);
        newExpense.setValue(request.getValue());
        newExpense.setExpenseDate(LocalDate.now()); // Usa la fecha actual

        return travelExpenseRepository.save(newExpense);
    }


    public TravelExpense updateExpense(Long expenseId, UpdateExpenseRequestDto request) {
        TravelExpense existingExpense = travelExpenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Gasto no encontrado con id: " + expenseId));

        Employee employee = existingExpense.getEmployee();

        employee.setName(request.getEmployeeName());
        employeeRepository.save(employee); // Guardar los cambios del empleado

        existingExpense.setExpenseDate(request.getExpenseDate());
        existingExpense.setValue(request.getValue());

        return travelExpenseRepository.save(existingExpense);
    }




}
