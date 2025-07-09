package com.sura.travel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SimpleExpenseRequestDto {
    @NotBlank(message = "El nombre del empleado no puede estar vacío")
    private String employeeName;

    @NotNull(message = "El valor del gasto no puede ser nulo")
    @Positive(message = "El valor del gasto debe ser positivo")
    private BigDecimal value;


}
