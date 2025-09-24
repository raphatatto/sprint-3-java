package br.com.fiap.nextpark.service;

import br.com.fiap.nextpark.model.Alocacao;
import br.com.fiap.nextpark.model.StatusVaga; // se estiver usando enum
import br.com.fiap.nextpark.repository.AlocacaoRepository;
import br.com.fiap.nextpark.repository.VagaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class DesalocarService {

    private final AlocacaoRepository alocRepo;
    private final VagaRepository vagaRepo;

    public DesalocarService(AlocacaoRepository alocRepo, VagaRepository vagaRepo) {
        this.alocRepo = alocRepo;
        this.vagaRepo = vagaRepo;
    }

    @Transactional
    public void encerrarAlocacao(Long alocacaoId) {
        Alocacao aloc = alocRepo.findById(alocacaoId)
                .orElseThrow(() -> new EntityNotFoundException("Alocação não encontrada"));


        aloc.setFim(LocalDateTime.now());
        aloc.setAtiva("N");
        alocRepo.save(aloc);

        // liberar a vaga
        if (aloc.getVaga() != null) {
            if (aloc.getVaga().getClass().getSimpleName().equals("Vaga")) {
                // se usa enum:
                try { aloc.getVaga().getClass().getMethod("setStatus", StatusVaga.class);
                    aloc.getVaga().getClass().getMethod("setStatus", StatusVaga.class)
                            .invoke(aloc.getVaga(), StatusVaga.LIVRE);
                } catch (Exception ignore) {
                    // se não usa enum, troca por boolean:
                    try { aloc.getVaga().getClass().getMethod("setDisponivel", boolean.class)
                            .invoke(aloc.getVaga(), true);
                    } catch (Exception ignored) {}
                }
            }
            vagaRepo.save(aloc.getVaga());
        }
    }
}
