package br.com.fiap.nextpark.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistroDTO {
    @NotBlank @Size(min = 3, max = 50)
    private String username;

    @NotBlank @Size(min = 6, max = 60)
    private String password;
}
