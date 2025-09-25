// src/main/java/br/com/fiap/nextpark/controller/VagaController.java
package br.com.fiap.nextpark.controller;

import br.com.fiap.nextpark.model.StatusVaga;                 // <- import
import br.com.fiap.nextpark.model.Vaga;
import br.com.fiap.nextpark.repository.AlocacaoRepository;    // <- import
import br.com.fiap.nextpark.repository.VagaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/vagas")
@Validated
public class VagaController {

    private final VagaRepository vagaRepo;
    private final AlocacaoRepository alocRepo;   // <- declare o campo

    public VagaController(VagaRepository vagaRepo, AlocacaoRepository alocRepo) { // <- injete no ctor
        this.vagaRepo = vagaRepo;
        this.alocRepo = alocRepo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("vagas", vagaRepo.findAll());
        return "vagas/list";
    }

    @GetMapping("/nova")
    public String formNova(Model model) {
        model.addAttribute("vaga", new Vaga());
        return "vagas/form";
    }

    @PostMapping
    public String criar(@ModelAttribute("vaga") Vaga vaga) {
        vagaRepo.save(vaga);
        return "redirect:/vagas";
    }

    @GetMapping("/{id}/editar")
    public String formEditar(@PathVariable Long id, Model model) {
        Optional<Vaga> v = vagaRepo.findById(id);
        if (v.isEmpty()) return "redirect:/vagas";
        model.addAttribute("vaga", v.get());
        return "vagas/form";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute("vaga") Vaga form) {
        return vagaRepo.findById(id).map(v -> {
            v.setCodigo(form.getCodigo());
            v.setSetor(form.getSetor());
            vagaRepo.save(v);
            return "redirect:/vagas";
        }).orElse("redirect:/vagas");
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        return vagaRepo.findById(id).map(v -> {
            boolean temAtiva = alocRepo.existsByVagaIdAndAtiva(v.getId(), "S"); // <- usa o repo
            if (temAtiva || v.getStatus() != StatusVaga.LIVRE) {
                ra.addFlashAttribute("erro", "Não é possível excluir vaga ocupada ou com alocação ativa.");
                return "redirect:/vagas";
            }
            vagaRepo.delete(v);
            ra.addFlashAttribute("ok", "Vaga excluída.");
            return "redirect:/vagas";
        }).orElseGet(() -> {
            ra.addFlashAttribute("erro", "Vaga não encontrada.");
            return "redirect:/vagas";
        });
    }
}
