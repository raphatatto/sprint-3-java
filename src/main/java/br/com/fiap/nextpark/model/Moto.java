package br.com.fiap.nextpark.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Moto {
    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    @Size(min = 7, max = 8)
    @Pattern(regexp = "^[A-Z0-9-]{6,8}$", message = "Placa inválida")
    @Column(nullable = false, length = 10)
    private String placa;

    @Size(max = 60)
    @Column(length = 60)
    private String modelo;

    @Size(max = 30)
    @Column(length = 30)

    private String cor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusMoto status = StatusMoto.VISTORIA;

    private Set<Alocacao> alocacoes;
}
