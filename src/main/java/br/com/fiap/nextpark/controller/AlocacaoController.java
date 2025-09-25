package br.com.fiap.nextpark.controller;

import br.com.fiap.nextpark.model.Alocacao;
import br.com.fiap.nextpark.security.ManagerPasswordGuard;
import br.com.fiap.nextpark.service.DesalocarService;
import br.com.fiap.nextpark.repository.AlocacaoRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/alocacoes")
public class AlocacaoController {

    private final AlocacaoRepository alocRepo;
    private final DesalocarService desalocarService;
    private final ManagerPasswordGuard guard;

    public AlocacaoController(AlocacaoRepository alocRepo,
                              DesalocarService desalocarService,
                              ManagerPasswordGuard guard) {
        this.alocRepo = alocRepo;
        this.desalocarService = desalocarService;
        this.guard = guard;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("alocacoes", alocRepo.findAll());
        return "alocacoes/list";
    }

    @PostMapping("/{id}/encerrar")
    public String encerrar(@PathVariable Long id,
                           @RequestParam String adminPass,
                           RedirectAttributes ra) {
        if (!guard.isValid(adminPass)) {
            ra.addFlashAttribute("erro", "Senha do gerente inválida.");
            return "redirect:/alocacoes";
        }
        try {
            desalocarService.encerrarAlocacao(id);
            ra.addFlashAttribute("ok", "Alocação encerrada com sucesso.");
        } catch (Exception e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/alocacoes";
    }
}
