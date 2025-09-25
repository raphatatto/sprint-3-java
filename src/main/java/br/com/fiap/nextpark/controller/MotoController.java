// src/main/java/br/com/fiap/nextpark/controller/MotoController.java
package br.com.fiap.nextpark.controller;

import br.com.fiap.nextpark.dto.MotoCreateDTO;
import br.com.fiap.nextpark.model.Alocacao;
import br.com.fiap.nextpark.model.Moto;
import br.com.fiap.nextpark.model.StatusMoto;
import br.com.fiap.nextpark.repository.AlocacaoRepository;
import br.com.fiap.nextpark.repository.MotoRepository;
import br.com.fiap.nextpark.repository.VagaRepository;
import br.com.fiap.nextpark.security.ManagerPasswordGuard;
import br.com.fiap.nextpark.service.CadastroMotoService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/motos")
public class MotoController {

    private final CadastroMotoService cadastro;
    private final VagaRepository vagaRepo;
    private final MotoRepository motoRepo;
    private final AlocacaoRepository alocRepo;
    private final ManagerPasswordGuard guard;

    public MotoController(CadastroMotoService cadastro,
                          VagaRepository vagaRepo,
                          MotoRepository motoRepo,
                          AlocacaoRepository alocRepo,
                          ManagerPasswordGuard guard) {
        this.cadastro = cadastro;
        this.vagaRepo = vagaRepo;
        this.motoRepo = motoRepo;
        this.alocRepo = alocRepo;
        this.guard = guard;
    }

    // LISTAR
    @GetMapping
    @PreAuthorize("hasAnyRole('OPERATOR','MANAGER')")
    public String list(Model model) {
        model.addAttribute("motos", motoRepo.findAll());
        return "motos/list";
    }

    @GetMapping("/novo")
    @PreAuthorize("hasAnyRole('OPERATOR','MANAGER')")
    public String formNovo(Model model) {
        if (!model.containsAttribute("moto")) {
            model.addAttribute("moto", new MotoCreateDTO());
        }
        model.addAttribute("vagas", vagaRepo.findAll());
        return "motos/form";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERATOR','MANAGER')")
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
            return "redirect:/motos";
        } catch (IllegalArgumentException e) {
            binding.rejectValue("placa", "placa.jaCadastrada", e.getMessage());
            model.addAttribute("vagas", vagaRepo.findAll());
            return "motos/form";
        }
    }

    @GetMapping("/{id}/editar")
    public String formEditar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Moto> opt = motoRepo.findById(id);
        if (opt.isEmpty()) {
            ra.addFlashAttribute("erro", "Moto não encontrada.");
            return "redirect:/motos";
        }
        Moto m = opt.get();
        MotoCreateDTO dto = new MotoCreateDTO();
        dto.setPlaca(m.getPlaca());
        dto.setModelo(m.getModelo());
        dto.setCor(m.getCor());
        model.addAttribute("moto", dto);
        model.addAttribute("motoId", id);
        model.addAttribute("vagas", vagaRepo.findAll());
        return "motos/form";
    }

    // ATUALIZAR (requer senha do gerente)
    @PostMapping("/{id}/atualizar")
    public String atualizar(@PathVariable Long id,
                            @RequestParam String adminPass,
                            @ModelAttribute("moto") MotoCreateDTO form,
                            RedirectAttributes ra,
                            Model model) {
        if (!guard.isValid(adminPass)) {
            ra.addFlashAttribute("erro", "Senha do gerente inválida.");
            return "redirect:/motos/" + id + "/editar";
        }
        return motoRepo.findById(id).map(m -> {
            // checar duplicidade (ignora a própria moto)
            var existente = motoRepo.findByPlacaIgnoreCase(form.getPlaca())
                    .filter(x -> x.getId() != m.getId());
            if (existente.isPresent()) {
                ra.addFlashAttribute("erro", "Essa placa já está cadastrada em outra moto.");
                return "redirect:/motos/" + id + "/editar";
            }
            m.setPlaca(form.getPlaca());
            m.setModelo(form.getModelo());
            m.setCor(form.getCor());
            motoRepo.save(m);
            ra.addFlashAttribute("ok", "Moto atualizada.");
            return "redirect:/motos";
        }).orElseGet(() -> {
            ra.addFlashAttribute("erro", "Moto não encontrada.");
            return "redirect:/motos";
        });
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id,
                          @RequestParam String adminPass,
                          RedirectAttributes ra) {
        if (!guard.isValid(adminPass)) {
            ra.addFlashAttribute("erro", "Senha do gerente inválida.");
            return "redirect:/motos";
        }
        return motoRepo.findById(id).map(m -> {
            // Bloqueia exclusão se estiver alocada
            boolean temAlocAtiva = alocRepo
                    .existsByMotoIdAndAtiva(m.getId(), "S");
            if (temAlocAtiva || m.getStatus() == StatusMoto.ALOCADA) {
                ra.addFlashAttribute("erro", "Não é possível excluir moto alocada. Desaloque antes.");
                return "redirect:/motos";
            }
            motoRepo.delete(m);
            ra.addFlashAttribute("ok", "Moto excluída.");
            return "redirect:/motos";
        }).orElseGet(() -> {
            ra.addFlashAttribute("erro", "Moto não encontrada.");
            return "redirect:/motos";
        });
    }
}
