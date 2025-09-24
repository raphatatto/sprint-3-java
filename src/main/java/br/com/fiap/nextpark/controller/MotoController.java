// br/com/fiap/nextpark/controller/MotoController.java
package br.com.fiap.nextpark.controller;

import br.com.fiap.nextpark.dto.MotoCreateDTO;
import br.com.fiap.nextpark.repository.VagaRepository;
import br.com.fiap.nextpark.service.CadastroMotoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/motos")
public class MotoController {

    private final CadastroMotoService cadastro;
    private final VagaRepository vagaRepo;

    public MotoController(CadastroMotoService cadastro, VagaRepository vagaRepo) {
        this.cadastro = cadastro;
        this.vagaRepo = vagaRepo;
    }

    @GetMapping("/novo")
    public String formNovo(Model model) {
        if (!model.containsAttribute("moto")) {
            model.addAttribute("moto", new MotoCreateDTO());
        }
        model.addAttribute("vagas", vagaRepo.findAll());
        return "motos/form";
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute("moto") MotoCreateDTO dto,
                        BindingResult binding,
                        Model model,
                        RedirectAttributes ra) {
        if (binding.hasErrors()) {
            model.addAttribute("vagas", vagaRepo.findAll());
            return "motos/form";
        }
        try {
            cadastro.cadastrarEAlocar(dto);
            ra.addFlashAttribute("ok", "Moto cadastrada e alocada com sucesso!");
            return "redirect:/localizar";
        } catch (IllegalArgumentException e) {
            binding.rejectValue("placa", "placa.jaCadastrada", e.getMessage()); // erro no campo
            model.addAttribute("vagas", vagaRepo.findAll());
            return "motos/form";
        }
    }
}
