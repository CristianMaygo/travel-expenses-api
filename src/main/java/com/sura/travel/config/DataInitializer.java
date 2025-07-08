package com.sura.travel.config;

import com.sura.travel.domain.TravelExpense;
import com.sura.travel.repository.TravelExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TravelExpenseRepository travelExpenseRepository;

    @Override
    public void run(String... args) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/mm/yy");

        List<TravelExpense> expenses = Arrays.asList(
                createExpense("Adam", "1/01/21", "2000000", formatter),
                createExpense("Adam", "2/01/21", "1000000", formatter),
                createExpense("Adam", "3/02/21", "500000", formatter),
                createExpense("Bolton", "1/01/21", "400000", formatter),
                createExpense("Bolton", "2/01/21", "500000", formatter),
                createExpense("Bolton", "3/02/21", "1100000", formatter),
                createExpense("Chelsea", "2/01/21", "59999", formatter),
                createExpense("Chelsea", "2/01/21", "900000", formatter),
                createExpense("Chelsea", "3/02/21", "1100000", formatter),
                createExpense("Elsy", "2/01/21", "4000000", formatter),
                createExpense("Vincent", "3/02/21", "899999", formatter),
                createExpense("Warden", "2/01/21", "5100000", formatter),
                createExpense("Warden", "3/02/21", "1100000", formatter)
        );
        travelExpenseRepository.saveAll(expenses);

        System.out.println("    >>>>>>>>>>>>>>DATOS DE PRUEBA INICIADOS EN LA BASE DE DATOS<<<<<<<       ");
    }

    private TravelExpense createExpense(String employeeName, String date, String value, DateTimeFormatter formatter) {
        TravelExpense expense = new TravelExpense();
        expense.setEmployeeName(employeeName);
        expense.setExpenseDate(LocalDate.parse(date, formatter));
        expense.setValue(new BigDecimal(value));
        return expense;
    }
}
