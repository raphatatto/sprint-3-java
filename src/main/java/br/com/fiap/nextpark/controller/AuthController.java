// br/com/fiap/nextpark/controller/AuthController.java
package br.com.fiap.nextpark.controller;

import br.com.fiap.nextpark.dto.RegistroDTO;
import br.com.fiap.nextpark.service.RegistroService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final RegistroService registroService;

    public AuthController(RegistroService registroService) {
        this.registroService = registroService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/auth/registro")
    public String formRegistro(Model model) {
        model.addAttribute("form", new RegistroDTO());
        return "auth/registro";
    }

    @PostMapping("/auth/registro")
    public String registrar(@Valid @ModelAttribute("form") RegistroDTO form,
                            BindingResult binding, Model model) {
        if (binding.hasErrors()) {
            return "auth/registro";
        }
        try {
            registroService.registrarOperator(form);
            // redireciona para login com msg
            return "redirect:/login?msg=Cadastro%20realizado.%20Faça%20login.";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "auth/registro";
        }
    }
}
