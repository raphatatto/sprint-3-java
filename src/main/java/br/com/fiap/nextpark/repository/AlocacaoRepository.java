package br.com.fiap.nextpark.repository;

import br.com.fiap.nextpark.model.Alocacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlocacaoRepository extends JpaRepository<Alocacao, Long> {
    Optional<Alocacao> findTopByMotoIdAndAtivaTrueOrderByInicioDesc(Long motoId);
}
