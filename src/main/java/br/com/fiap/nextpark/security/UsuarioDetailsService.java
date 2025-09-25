// br/com/fiap/nextpark/security/UsuarioDetailsService.java
package br.com.fiap.nextpark.security;

import br.com.fiap.nextpark.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {
    private final UsuarioRepository repo;
    public UsuarioDetailsService(UsuarioRepository repo) { this.repo = repo; }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var u = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        return org.springframework.security.core.userdetails.User
                .withUsername(u.getUsername())
                .password(u.getPassword())
                .roles(u.getRole().name().replace("ROLE_", ""))
                .disabled(!u.isEnabled()) // mapeia seu 'S'/'N'
                .build();
    }
}
