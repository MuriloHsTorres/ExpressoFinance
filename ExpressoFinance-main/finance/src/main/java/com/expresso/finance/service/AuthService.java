package com.expresso.finance.service;

import com.expresso.finance.dto.request.CadastroRequest;
import com.expresso.finance.dto.request.LoginRequest;
import com.expresso.finance.dto.response.LoginResponse;
import com.expresso.finance.entity.Cliente;
import com.expresso.finance.exception.BusinessException;
import com.expresso.finance.repository.ClienteRepository;
import com.expresso.finance.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public Cliente cadastrar(CadastroRequest cadastroRequest) {
        if (clienteRepository.existsByEmail(cadastroRequest.getEmail())) {
            throw new BusinessException("O email informado já está em uso.");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(cadastroRequest.getNome());
        cliente.setEmail(cadastroRequest.getEmail());
        cliente.setSenha(passwordEncoder.encode(cadastroRequest.getSenha()));

        return clienteRepository.save(cliente);
        // Futuramente, aqui também se criará a conta "Carteira" e categorias padrão.
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getSenha()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Cliente cliente = (Cliente) authentication.getPrincipal();
        String jwt = tokenProvider.generateToken(cliente.getId(), cliente.getEmail());

        return LoginResponse.builder()
                .token(jwt)
                .tipo("Bearer")
                .clienteId(cliente.getId())
                .nome(cliente.getNome())
                .email(cliente.getEmail())
                .build();
    }
}