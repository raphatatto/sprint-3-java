package br.com.fiap.nextpark.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(
        name = "vaga",
        uniqueConstraints = @UniqueConstraint(name = "uk_vaga_codigo", columnNames = "codigo")
)
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

    @OneToMany(mappedBy = "vaga", fetch = FetchType.LAZY) // sem cascade para não excluir alocações
    private Set<Alocacao> alocacoes = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusVaga status = StatusVaga.LIVRE;

    @PrePersist @PreUpdate
    private void upper() {
        if (codigo != null) codigo = codigo.toUpperCase();
        if (setor != null)   setor  = setor.toUpperCase();
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vaga)) return false;
        Vaga that = (Vaga) o;
        return Objects.equals(id, that.id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}
