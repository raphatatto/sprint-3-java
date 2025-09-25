package br.com.fiap.nextpark.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fiap.nextpark.model.entity.HistoricoMovimentacao;

public interface HistoricoMovimentacaoRepository extends JpaRepository<HistoricoMovimentacao, Long> {
    List<HistoricoMovimentacao> findByMoto_IdOrderByCreatedAtDesc(Long motoId);
}
