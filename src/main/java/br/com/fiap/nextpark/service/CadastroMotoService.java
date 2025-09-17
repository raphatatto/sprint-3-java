package br.com.fiap.nextpark.service;

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
    private final MotoRepository motorepo;
    private final VagaRepository vagarepo;
    private final AlocacaoRepository alocacaorepo;

    public CadastroMotoService(MotoRepository m, VagaRepository v,  AlocacaoRepository a){
        this.motorepo = m; this.vagarepo =v; this.alocacaorepo = a;
    }
    public cadastrarEAlocar(MotoCreateDTO dto){
        motorepo.fyndByPlacaIgnoreCase(dto.getPlaca()).ifPresent(moto -> {
            throw new IllegalArgumentException("Essa placa ja esta cadastrada.");
        });

        Vaga vaga = (dto.getCodigoVaga() !=null && !dto.getCodigoVaga().isBlank())
        ? vagarepo.findByCodigoIgnoreCase(dto.getCodigoVaga())
                .orElseThrow(()-> new EntityNotFoundException("Vaga " + dto.getCodigoVaga()+ "Vaga não encontrada."))
        : vagarepo.findFirstByStatusOrderByCodigoAsc(StatusVaga.LIVRE)
                .orElseThrow(()-> new IllegalStateException("Não existem vagas livres disponíveis"));
        if (vaga.getStatus() !=StatusVaga.LIVRE) throw new IllegalArgumentException("A vaga não esta livre.");
    }

    Moto moto = new Moto();
    moto.setPlaca(dto.getPlaca());
    moto.setModelo(dto.getModelo());
    moto.setCor(dto.getCor());
    moto.setStatus(StatusMoto.ALOCADA);
    moto.motoRepo.save(moto);

    Alocacao aloc =  new Alocacao();
    aloc.setMoto(moto);
    aloc.setVaga(vaga)
    aloc.setInicio(LocalDateTime.now());
    aloc.setAtiva(true);
    alocRepo.save(aloc);

    vaga.setStatus(StatusVaga.OCUPADA);
    vagaRepo.save(vaga);

    return moto;

}
