// br/com/fiap/nextpark/service/LocalizarMotoService.java
package br.com.fiap.nextpark.service;

import br.com.fiap.nextpark.model.Alocacao;
import br.com.fiap.nextpark.repository.AlocacaoRepository;
import br.com.fiap.nextpark.repository.MotoRepository;
import org.springframework.stereotype.Service;

@Service
public class LocalizarMotoService {

    private final MotoRepository motoRepo;
    private final AlocacaoRepository alocRepo;

    public LocalizarMotoService(MotoRepository motoRepo, AlocacaoRepository alocRepo) {
        this.motoRepo = motoRepo;
        this.alocRepo = alocRepo;
    }

    public Alocacao localizacaoAtualPorPlaca(String placa) {
        return motoRepo.findByPlacaIgnoreCase(placa)
                .flatMap(m -> alocRepo.findTopAtivaByMoto(m.getId()))
                .orElse(null);
    }
}
