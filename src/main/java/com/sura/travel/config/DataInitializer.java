package com.sura.travel.config;

import com.sura.travel.domain.Employee;
import com.sura.travel.domain.TravelExpense;
import com.sura.travel.repository.EmployeeRepository;
import com.sura.travel.repository.TravelExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TravelExpenseRepository travelExpenseRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) throws Exception {
        Map<String, Employee> employees = Stream.of("Adam", "Bolton", "Chelsea", "Elsy", "Vincent", "Warden")
                .map(Employee::new)
                .map(employeeRepository::save)
                .collect(Collectors.toMap(Employee::getName, employee -> employee));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yy");

        travelExpenseRepository.saveAll(Arrays.asList(
                createExpense(employees.get("Adam"), "1/01/21", "2000000", formatter),
                createExpense(employees.get("Adam"), "2/01/21", "1000000", formatter),
                createExpense(employees.get("Adam"), "3/02/21", "500000", formatter),
                createExpense(employees.get("Bolton"), "1/01/21", "400000", formatter),
                createExpense(employees.get("Bolton"), "2/01/21", "500000", formatter),
                createExpense(employees.get("Bolton"), "3/02/21", "1100000", formatter),
                createExpense(employees.get("Chelsea"), "2/01/21", "59999", formatter),
                createExpense(employees.get("Chelsea"), "2/01/21", "900000", formatter),
                createExpense(employees.get("Chelsea"), "3/02/21", "1100000", formatter),
                createExpense(employees.get("Elsy"), "2/01/21", "4000000", formatter),
                createExpense(employees.get("Vincent"), "3/02/21", "899999", formatter),
                createExpense(employees.get("Warden"), "2/01/21", "5100000", formatter),
                createExpense(employees.get("Warden"), "3/02/21", "1100000", formatter)
        ));

        System.out.println("    >>>>>>>>>>>>>>DATOS DE PRUEBA INICIADOS EN LA BASE DE DATOS<<<<<<<       ");
    }

    private TravelExpense createExpense (Employee employee, String date, String value, DateTimeFormatter formatter){
        TravelExpense expense = new TravelExpense();
        expense.setEmployee(employee);
        expense.setExpenseDate(LocalDate.parse(date, formatter));
        expense.setValue(new BigDecimal(value));
        return expense;
    }
}
