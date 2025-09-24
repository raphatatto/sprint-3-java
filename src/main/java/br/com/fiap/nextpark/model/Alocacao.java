package br.com.fiap.nextpark.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ALOCACAO")
public class Alocacao {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotNull
        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "moto_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_alocacao_moto"))
        private Moto moto;

        @NotNull
        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "vaga_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_alocacao_vaga"))
        private Vaga vaga;

        @Column(nullable = false)
        private LocalDateTime inicio;

        @Column
        private LocalDateTime fim;

        @Column(name = "ATIVA", nullable = false, length = 1)
        private String ativa = "S";

        public void encerrar(LocalDateTime fim) {
            this.fim = fim != null ? fim : LocalDateTime.now();
            this.ativa = "S";
        }

        @Transient public boolean isAtivaBool() { return "S".equalsIgnoreCase(ativa); }
        public void setAtivaBool(boolean v) { this.ativa = v ? "S" : "N"; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Alocacao)) return false;
            Alocacao that = (Alocacao) o;
            return Objects.equals(id, that.id);
        }


        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
