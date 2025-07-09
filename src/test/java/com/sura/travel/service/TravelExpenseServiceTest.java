package com.sura.travel.service;

import com.sura.travel.domain.Employee;
import com.sura.travel.domain.TravelExpense;
import com.sura.travel.dto.*;
import com.sura.travel.exception.ResourceNotFoundException;
import com.sura.travel.repository.EmployeeRepository;
import com.sura.travel.repository.TravelExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TravelExpenseServiceTest {

    @InjectMocks
    private TravelExpenseService travelExpenseService;

    @Mock
    private TravelExpenseRepository travelExpenseRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para createSimpleExpense
    @Test
    void testCreateSimpleExpense_WhenEmployeeExists() {
        SimpleExpenseRequestDto request = new SimpleExpenseRequestDto();
        request.setEmployeeName("Adam");
        request.setValue(new BigDecimal("100000"));

        Employee employee = new Employee("Adam");
        TravelExpense expense = new TravelExpense();
        expense.setEmployee(employee);
        expense.setValue(request.getValue());
        expense.setExpenseDate(LocalDate.now());

        when(employeeRepository.findByName("Adam")).thenReturn(Optional.of(employee));
        when(travelExpenseRepository.save(any(TravelExpense.class))).thenReturn(expense);

        TravelExpense result = travelExpenseService.createSimpleExpense(request);

        assertEquals("Adam", result.getEmployee().getName());
        assertEquals(new BigDecimal("100000"), result.getValue());
        assertNotNull(result.getExpenseDate());
    }

    @Test
    void testCreateSimpleExpense_WhenEmployeeDoesNotExist() {
        SimpleExpenseRequestDto request = new SimpleExpenseRequestDto();
        request.setEmployeeName("John");
        request.setValue(new BigDecimal("150000"));

        Employee newEmployee = new Employee("John");

        when(employeeRepository.findByName("John")).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);

        TravelExpense savedExpense = new TravelExpense();
        savedExpense.setEmployee(newEmployee);
        savedExpense.setValue(request.getValue());
        savedExpense.setExpenseDate(LocalDate.now());

        when(travelExpenseRepository.save(any(TravelExpense.class))).thenReturn(savedExpense);

        TravelExpense result = travelExpenseService.createSimpleExpense(request);

        assertEquals("John", result.getEmployee().getName());
        assertEquals(new BigDecimal("150000"), result.getValue());
    }

    // Test para getExpenseById
    @Test
    void testGetExpenseById_Found() {
        TravelExpense expense = new TravelExpense();
        expense.setId(1L);

        when(travelExpenseRepository.findById(1L)).thenReturn(Optional.of(expense));

        TravelExpense result = travelExpenseService.getExpenseById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void testGetExpenseById_NotFound() {
        when(travelExpenseRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                travelExpenseService.getExpenseById(99L)
        );

        assertTrue(exception.getMessage().contains("Gasto no encontrado"));
    }

    // Test para updateExpense
    @Test
    void testUpdateExpense_Success() {
        Long id = 1L;

        Employee employee = new Employee("Adam");
        TravelExpense expense = new TravelExpense();
        expense.setId(id);
        expense.setEmployee(employee);

        UpdateExpenseRequestDto request = new UpdateExpenseRequestDto();
        request.setEmployeeName("Adam Updated");
        request.setValue(new BigDecimal("123456"));
        request.setExpenseDate(LocalDate.of(2024, 1, 1));

        when(travelExpenseRepository.findById(id)).thenReturn(Optional.of(expense));
        when(travelExpenseRepository.save(any(TravelExpense.class))).thenReturn(expense);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        TravelExpense result = travelExpenseService.updateExpense(id, request);

        assertEquals("Adam Updated", result.getEmployee().getName());
        assertEquals(new BigDecimal("123456"), result.getValue());
        assertEquals(LocalDate.of(2024, 1, 1), result.getExpenseDate());
    }

    // Test para getExpenseSummary
    @Test
    void testGetExpenseSummary_Basic() {
        Employee emp = new Employee();
        emp.setName("John");

        TravelExpense t1 = new TravelExpense();
        t1.setEmployee(emp);
        t1.setValue(new BigDecimal("100000"));
        t1.setExpenseDate(LocalDate.of(2025, Month.JANUARY, 10));

        TravelExpense t2 = new TravelExpense();
        t2.setEmployee(emp);
        t2.setValue(new BigDecimal("200000"));
        t2.setExpenseDate(LocalDate.of(2025, Month.JANUARY, 15));

        when(travelExpenseRepository.findAllByOrderByEmployee_NameAsc()).thenReturn(List.of(t1, t2));

        ExpenseSummaryDto result = travelExpenseService.getExpenseSummary();

        assertNotNull(result);
        assertEquals(new BigDecimal("300000.00"), result.getGrandTotal());
        assertEquals(1, result.getEmployeeSummaries().size());

        EmployeeExpenseDto employeeSummary = result.getEmployeeSummaries().get(0);
        assertEquals("John", employeeSummary.getEmployeeName());
        assertEquals(1, employeeSummary.getMonthlyExpenses().size());

        MonthlyExpenseDto monthly = employeeSummary.getMonthlyExpenses().get(0);
        assertEquals("JANUARY", monthly.getMonth());
        assertEquals(new BigDecimal("300000.00"), monthly.getSubtotal());
    }
}
