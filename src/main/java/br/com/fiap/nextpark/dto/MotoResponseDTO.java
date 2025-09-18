package br.com.fiap.nextpark.dto;

import br.com.fiap.nextpark.model.StatusMoto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class MotoResponseDTO {

    private String placa;
    private String  modelo;
    private String cor;
    private StatusMoto status;
    private String codigoVaga;


}
