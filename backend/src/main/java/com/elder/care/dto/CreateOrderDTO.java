package com.elder.care.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateOrderDTO {

    @NotNull(message = "护工ID不能为空")
    private Long nurseId;
}
