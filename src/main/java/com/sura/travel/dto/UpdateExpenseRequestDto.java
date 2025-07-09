package com.sura.travel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateExpenseRequestDto {

    @NotBlank(message = "El nombre del empleado no puede estar vacío")
    private String employeeName;

    @NotNull(message = "La fecha del gasto no puede ser nula")
    @PastOrPresent(message = "La fecha del gasto no puede ser futura")
    private LocalDate expenseDate;

    @NotNull(message = "El valor del gasto no puede ser nulo")
    @Positive(message = "El valor del gasto debe ser positivo")
    private BigDecimal value;
}
