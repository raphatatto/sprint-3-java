package br.com.fiap.nextpark.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


public class MotoCreateDTO {
    @NotBlank
    @Size(min = 7, max = 8, message = "A placa deve ter entre 8 e 7 caracteres")
    private String placa;

    @NotBlank(message = "Modelo é obrigatório")
    private String modelo;

    private String cor;

    private String codigoVaga;

}
