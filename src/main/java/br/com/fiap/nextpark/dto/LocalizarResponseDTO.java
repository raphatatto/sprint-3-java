package br.com.fiap.nextpark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LocalizarResponseDTO {
    private String placa;
    private String codigoVaga;
    private LocalDateTime inicio;
}
