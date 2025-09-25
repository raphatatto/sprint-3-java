// MotoCreateDTO.java
package br.com.fiap.nextpark.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
// import jakarta.validation.constraints.Pattern; // opcional
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MotoCreateDTO {

    @NotBlank
    // Opcional: validação de padrão (ajuste se quiser Mercosul estrito)
    // @Pattern(regexp = "^[A-Z0-9-]{6,8}$", message = "Placa inválida")
    @Size(min = 7, max = 10)
    private String placa;

    @NotBlank
    @Size(max = 60)
    private String modelo;

    @Size(max = 30)
    private String cor;

    // opcional (pode ser null): escolher vaga específica
    private String codigoVaga;
}
