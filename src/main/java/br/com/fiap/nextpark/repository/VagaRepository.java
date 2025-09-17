package br.com.fiap.nextpark.repository;

import br.com.fiap.nextpark.model.StatusVaga;
import br.com.fiap.nextpark.model.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VagaRepository extends JpaRepository <Vaga, Long> {
    Optional<Vaga> findByCodigoIgnoreCase(String codigo);
    Optional<Vaga> findFirstByStatusOrderByCodigoAsc(StatusVaga status);
}
