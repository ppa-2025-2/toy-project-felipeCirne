package com.example.demo.controller.dto;


import java.util.Optional;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PatchTicketDTO(
    
    @NotNull(message = "O Resposavel")
    Integer managerId,

    @NotNull(message = "status nao pode ser null")
    @NotBlank(message = "Status nao pode conter espaços")
    String status,

    Optional <String> cancelReason
)  {

}
