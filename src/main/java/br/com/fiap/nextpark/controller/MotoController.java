package br.com.fiap.nextpark.controller;

import br.com.fiap.nextpark.service.CadastroMotoService;
import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/motos")

public class MotoController {
private final CadastroMotoService cadastro;

    public MotoController(CadastroMotoService cadastro) {
        this.cadastro = cadastro;
    }

    @GetMapping("/novo")
    public String formNovo(Model model){
        model.addAtribute("moto", new MotoCreateDTO());
        return "motos/form";
    }

    @GetMapping
}
