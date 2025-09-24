// br/com/fiap/nextpark/dto/RegistroDTO.java
package br.com.fiap.nextpark.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistroDTO {
    @NotBlank @Size(min = 3, max = 50)
    private String username;

    @NotBlank @Size(min = 6, max = 60) // senha crua (antes de criptografar)
    private String password;

    // getters/setters
    public String getUsername() { return username; }
    public void setUsername(String u) { this.username = u; }
    public String getPassword() { return password; }
    public void setPassword(String p) { this.password = p; }
}
