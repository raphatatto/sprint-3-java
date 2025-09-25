package br.com.fiap.nextpark.repository;

import br.com.fiap.nextpark.model.Alocacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlocacaoRepository extends JpaRepository<Alocacao, Long> {

    Optional<Alocacao> findTopByMotoIdAndAtivaOrderByInicioDesc(Long motoId, String ativa);

    default Optional<Alocacao> findTopAtivaByMoto(Long motoId) {
        return findTopByMotoIdAndAtivaOrderByInicioDesc(motoId, "S");
    }

    boolean existsByMotoIdAndAtiva(Long motoId, String ativa);
    boolean existsByVagaIdAndAtiva(Long vagaId, String ativa);

}
