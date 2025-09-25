package br.com.fiap.nextpark.controller;

import java.security.Principal;
import java.util.List;

import br.com.fiap.nextpark.model.enums.StatusMoto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.com.fiap.nextpark.model.entity.Moto;
import br.com.fiap.nextpark.model.enums.StatusMoto;
import br.com.fiap.nextpark.service.MotoService;

@Controller
@RequestMapping("/moto")
public class MotoController {

    private final MotoService motoService;

    public MotoController(MotoService motoService) { this.motoService = motoService; }

    @GetMapping
    public String list(@RequestParam(value="q", required=false) String q,
                       Principal principal,
                       @RequestParam(value="all", required=false) Boolean all,
                       Model model) {
        boolean gerente = all != null && all; // não usado; segurança já filtra pelo service
        // descobrir pelo Spring Security (ROLE):
        gerente = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_GERENTE"));
        List<Moto> motos = motoService.listarPara(principal.getName(), gerente, q);
        model.addAttribute("motos", motos);
        return "moto/list";
    }

    @GetMapping("/nova")
    public String nova(Model model) {
        model.addAttribute("moto", new Moto());
        model.addAttribute("status", StatusMoto.values());
        return "moto/form";
    }

    @PostMapping
    public String criar(@ModelAttribute Moto moto, Principal principal, RedirectAttributes ra) {
        motoService.criar(principal.getName(), moto);
        ra.addFlashAttribute("msg","Moto criada!");
        return "redirect:/moto";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Moto m = motoService.listarPara("", true, null) // gambit só para pegar via repo? Melhor:
                .stream().filter(x -> x.getId().equals(id)).findFirst()
                .orElseThrow(); // se preferir injete o repo e busque direto.
        model.addAttribute("moto", m);
        model.addAttribute("status", StatusMoto.values());
        return "moto/form";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute Moto src,
                            Principal principal, RedirectAttributes ra) {
        motoService.atualizarCliente(principal.getName(), id, src);
        ra.addFlashAttribute("msg","Moto atualizada!");
        return "redirect:/moto";
    }

    @PostMapping("/{id}/delete")
    public String deletar(@PathVariable Long id, Principal principal, RedirectAttributes ra) {
        motoService.deletarCliente(principal.getName(), id);
        ra.addFlashAttribute("msg","Moto excluída!");
        return "redirect:/moto";
    }

    // ====== Ações de pátio: só GERENTE ======
    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/{id}/desalocar")
    public String desalocar(@PathVariable Long id, Principal principal, RedirectAttributes ra) {
        motoService.desalocar(id, principal.getName());
        ra.addFlashAttribute("msg","Moto desalocada!");
        return "redirect:/moto";
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/{id}/alocar")
    public String alocar(@PathVariable Long id, @RequestParam Long vagaId,
                         Principal principal, RedirectAttributes ra) {
        motoService.alocar(id, vagaId, principal.getName());
        ra.addFlashAttribute("msg","Moto alocada!");
        return "redirect:/moto";
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/{id}/transferir")
    public String transferir(@PathVariable Long id, @RequestParam Long destinoId,
                             Principal principal, RedirectAttributes ra) {
        motoService.transferir(id, destinoId, principal.getName());
        ra.addFlashAttribute("msg","Moto transferida!");
        return "redirect:/moto";
    }

    @GetMapping("/{id}/historico")
    public String historico(@PathVariable Long id, Model model) {
        model.addAttribute("historico", motoService.historico(id));
        return "moto/historico";
    }
}
