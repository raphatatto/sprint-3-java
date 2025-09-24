package br.com.fiap.nextpark.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vaga")
public class Vaga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 10)
    @Column(nullable = false, length = 10)
    private String codigo;

    @Size(max = 20)
    @Column(length = 20)
    private String setor;

    @OneToMany(mappedBy = "vaga", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    private Set<Alocacao> alocacoes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusVaga status = StatusVaga.LIVRE;

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vaga)) return false;
        Vaga that = (Vaga) o;
        return Objects.equals(id, that.id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}
