package br.com.fiap.nextpark.service;

import br.com.fiap.nextpark.model.Alocacao;
import br.com.fiap.nextpark.model.Moto;
import br.com.fiap.nextpark.repository.AlocacaoRepository;
import br.com.fiap.nextpark.repository.MotoRepository;
import org.springframework.stereotype.Service;

@Service
public class LocalizarMotoService {

    private final MotoRepository motoRepo;
    private final AlocacaoRepository alocRepo;

    public LocalizarMotoService(MotoRepository m, AlocacaoRepository a) {
        this.motoRepo = m;
        this.alocRepo = a;
    }

    /** Retorna a alocação ATUAL (ativa) pela placa; lança exceção se a placa não existir. */
    public Alocacao localizacaoAtualPorPlaca(String placa) {
        Moto moto = motoRepo.fyndByPlacaIgnoreCase(placa)  // <- nome correto e sem typos
                .orElseThrow(() -> new IllegalArgumentException("Placa não encontrada."));

        return alocRepo.findTopByMotoIdAndAtivaTrueOrderByInicioDesc(moto.getId())
                .orElse(null); // se não está alocada, volta null
    }
}
