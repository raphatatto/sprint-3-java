package br.com.fiap.nextpark.service;

import br.com.fiap.nextpark.model.Alocacao;
import br.com.fiap.nextpark.model.StatusMoto;
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

        // encerra alocação
        aloc.encerrar(LocalDateTime.now());
        alocRepo.save(aloc);

        // libera a vaga
        if (aloc.getVaga() != null) {
            aloc.getVaga().setStatus(StatusVaga.LIVRE);
            vagaRepo.save(aloc.getVaga());
        }

        // (opcional) atualizar status da moto para VISTORIA
        if (aloc.getMoto() != null) {
            aloc.getMoto().setStatus(StatusMoto.VISTORIA);
            // se tiver MotoRepository injetado, salve também
        }
    }
}