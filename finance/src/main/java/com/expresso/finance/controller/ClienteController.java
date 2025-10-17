package com.expresso.finance.controller;

import com.expresso.finance.dto.request.AlterarSenhaRequest;
import com.expresso.finance.dto.request.AtualizarPerfilRequest;
import com.expresso.finance.dto.response.ClienteResponse;
import com.expresso.finance.security.JwtTokenProvider;
import com.expresso.finance.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/perfil")
    public ResponseEntity<ClienteResponse> buscarPerfil(@RequestHeader("Authorization") String token) {
        String clienteId = extrairClienteId(token);
        ClienteResponse response = clienteService.buscarPerfil(clienteId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/perfil")
    public ResponseEntity<ClienteResponse> atualizarPerfil(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody AtualizarPerfilRequest request) {
        String clienteId = extrairClienteId(token);
        ClienteResponse response = clienteService.atualizarPerfil(clienteId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/senha")
    public ResponseEntity<Void> alterarSenha(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody AlterarSenhaRequest request) {
        String clienteId = extrairClienteId(token);
        clienteService.alterarSenha(clienteId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/perfil")
    public ResponseEntity<Void> excluirConta(@RequestHeader("Authorization") String token) {
        String clienteId = extrairClienteId(token);
        clienteService.excluirConta(clienteId);
        return ResponseEntity.noContent().build();
    }

    private String extrairClienteId(String token) {
        String jwt = token.replace("Bearer ", "");
        return jwtTokenProvider.getClienteIdFromToken(jwt);
    }
}