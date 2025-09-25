package br.com.fiap.nextpark.service;

import br.com.fiap.nextpark.dto.MotoCreateDTO;
import br.com.fiap.nextpark.model.*;
import br.com.fiap.nextpark.repository.AlocacaoRepository;
import br.com.fiap.nextpark.repository.MotoRepository;
import br.com.fiap.nextpark.repository.VagaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CadastroMotoService {

    private final MotoRepository motoRepo;
    private final VagaRepository vagaRepo;
    private final AlocacaoRepository alocRepo;

    public CadastroMotoService(MotoRepository m, VagaRepository v, AlocacaoRepository a) {
        this.motoRepo = m;
        this.vagaRepo = v;
        this.alocRepo = a;
    }

    @Transactional
    public Moto cadastrarEAlocar(MotoCreateDTO dto) {
        // 1) impedir placa duplicada
        motoRepo.findByPlacaIgnoreCase(dto.getPlaca()).ifPresent(m -> {
            throw new IllegalArgumentException("Essa placa já está cadastrada.");
        });

        // 2) escolher a vaga: por código (se informado) OU primeira LIVRE
        Vaga vaga = (dto.getCodigoVaga() != null && !dto.getCodigoVaga().isBlank())
                ? vagaRepo.findByCodigoIgnoreCase(dto.getCodigoVaga())
                .orElseThrow(() -> new EntityNotFoundException("Vaga " + dto.getCodigoVaga() + " não encontrada."))
                : vagaRepo.findFirstByStatusOrderByCodigoAsc(StatusVaga.LIVRE)
                .orElseThrow(() -> new IllegalStateException("Não há vagas LIVRES disponíveis."));

        if (vaga.getStatus() != StatusVaga.LIVRE) {
            throw new IllegalStateException("A vaga " + vaga.getCodigo() + " não está LIVRE.");
        }

        // 3) criar a moto já como ALOCADA
        Moto moto = new Moto();
        moto.setPlaca(dto.getPlaca().trim().toUpperCase());
        moto.setModelo(dto.getModelo());
        moto.setCor(dto.getCor());
        moto.setStatus(StatusMoto.ALOCADA);
        moto = motoRepo.saveAndFlush(moto);
        // 4) criar a alocação ativa e marcar a vaga como OCUPADA
        Alocacao aloc = new Alocacao();
        aloc.setMoto(moto);
        aloc.setVaga(vaga);
        aloc.setInicio(LocalDateTime.now());
        aloc.setAtiva("S");
        alocRepo.save(aloc);

        vaga.setStatus(StatusVaga.OCUPADA);
        vagaRepo.save(vaga);

        return moto;
    }
}