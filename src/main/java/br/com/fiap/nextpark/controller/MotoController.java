package br.com.fiap.nextpark.controller;

import br.com.fiap.nextpark.dto.MotoCreateDTO;
import br.com.fiap.nextpark.service.CadastroMotoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;                 // <-- importe certo
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/motos")
@Validated
public class MotoController {

    private final CadastroMotoService cadastro;

    public MotoController(CadastroMotoService cadastro) {
        this.cadastro = cadastro;
    }

    @GetMapping("/novo")
    public String formNovo(Model model) {      // <-- Model do Spring
        model.addAttribute("moto", new MotoCreateDTO());  // <-- addAttribute (2 “t”)
        return "motos/form";
    }

    @PostMapping
    public String criar(MotoCreateDTO dto) {
        cadastro.cadastrarEAlocar(dto);
        return "redirect:/localizar";
    }
}
