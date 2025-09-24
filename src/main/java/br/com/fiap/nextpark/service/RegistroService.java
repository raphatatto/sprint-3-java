// br/com/fiap/nextpark/service/RegistroService.java
package br.com.fiap.nextpark.service;

import br.com.fiap.nextpark.dto.RegistroDTO;
import br.com.fiap.nextpark.model.Role;
import br.com.fiap.nextpark.model.Usuario;
import br.com.fiap.nextpark.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistroService {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public RegistroService(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public void registrarOperator(RegistroDTO dto) {
        if (repo.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Usuário já existe");
        }
        var u = new Usuario();
        u.setUsername(dto.getUsername());
        u.setPassword(encoder.encode(dto.getPassword())); // gera hash ~60 chars
        u.setRole(Role.ROLE_OPERATOR);
        u.setEnabled(true);
        repo.save(u);
    }
}
