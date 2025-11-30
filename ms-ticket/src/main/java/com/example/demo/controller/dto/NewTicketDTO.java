package com.example.demo.controller.dto;


import java.util.Optional;

import org.hibernate.validator.constraints.Length;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewTicketDTO(

        @NotNull(message = "O dono é obrigatório")
        Integer ownerId,

        Optional<Integer> recipientId,
        
        @NotNull(message = "O objeto é obrigatório")
        @NotBlank(message = "Não pode ser composta apenas de espaços")
        @Length(min = 3, message = "O objeto deve ter no mínimo 3 caracteres")
        String object,

        @NotNull(message = "A ação é obrigatório")
        @NotBlank(message = "Não pode ser composta apenas de espaços")
        @Length(min = 3, message = "A ação deve ter no mínimo 3") 
        String action,

        @NotNull(message = "O detalhe é obrigatório")
        @NotBlank(message = "Não pode ser composta apenas de espaços")
        @Length(min = 3, message = "Os detalhes devem ter no mínimo 3")
        String details,

        @NotNull(message = "O local é obrigatório")
        @NotBlank(message = "Não pode ser composta apenas de espaços")
        @Length(min = 3, message = "O local deve ter no mínimo 3")
        String local
        
        
)  {

}
