package com.expresso.finance.security;

import com.expresso.finance.entity.Cliente;
import com.expresso.finance.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return clienteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente não encontrado com o email: " + email));
    }

    @Transactional
    public UserDetails loadUserById(String id) {
        return clienteRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("Cliente não encontrado com o id: " + id)
        );
    }
}