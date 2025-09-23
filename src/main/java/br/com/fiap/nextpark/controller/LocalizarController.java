package br.com.fiap.nextpark.controller;

import br.com.fiap.nextpark.model.Alocacao;
import br.com.fiap.nextpark.service.LocalizarMotoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/localizar")
public class LocalizarController {

    private final LocalizarMotoService localizar;

    public LocalizarController(LocalizarMotoService localizar) {
        this.localizar = localizar;
    }

    @GetMapping
    public String form() {
        return "localizar/index"; // página com o input de placa
    }

    @PostMapping
    public String localizar(@RequestParam String placa, Model model) {
        Alocacao aloc = localizar.localizacaoAtualPorPlaca(placa);
        model.addAttribute("placa", placa.toUpperCase());
        model.addAttribute("alocacao", aloc); // null se não estiver alocada
        return "localizar/resultado";
    }
}
