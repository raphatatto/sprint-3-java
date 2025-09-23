package br.com.fiap.nextpark.controller;

import br.com.fiap.nextpark.model.Alocacao;
import br.com.fiap.nextpark.service.DesalocarService;
import br.com.fiap.nextpark.repository.AlocacaoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/alocacoes")
public class AlocacaoController {

    private final AlocacaoRepository alocRepo;
    private final DesalocarService desalocarService;

    public AlocacaoController(AlocacaoRepository alocRepo, DesalocarService desalocarService) {
        this.alocRepo = alocRepo;
        this.desalocarService = desalocarService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("alocacoes", alocRepo.findAll());
        return "alocacoes/list";
    }

    // encerra a alocação (libera a vaga)
    @PostMapping("/{id}/encerrar")
    public String encerrar(@PathVariable Long id) {
        desalocarService.encerrarAlocacao(id);
        return "redirect:/alocacoes";
    }
}
