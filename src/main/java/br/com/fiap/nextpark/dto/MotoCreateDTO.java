// src/main/java/br/com/fiap/nextpark/dto/MotoCreateDTO.java
package br.com.fiap.nextpark.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MotoCreateDTO {

    @NotBlank
    private String placa;

    @NotBlank
    private String modelo;

    private String cor;

    private String vagaCodigo;
}
