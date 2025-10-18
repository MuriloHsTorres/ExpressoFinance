package com.expresso.finance.controller;

import com.expresso.finance.dto.request.ClienteRequest;
import com.expresso.finance.dto.request.LoginRequest;
import com.expresso.finance.dto.response.ClienteResponse;
import com.expresso.finance.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Marca esta classe como um Controller de API REST
@RequestMapping("/api/clientes") // Define a URL base para este controller [cite: 69, 70]
public class ClienteController {

    @Autowired // Pede ao Spring para injetar o serviço que criamos
    private ClienteService clienteService;

    /**
     * Endpoint: POST /api/clientes/cadastro
     * [cite: 71]
     */
    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@RequestBody ClienteRequest request) {
        // @RequestBody diz ao Spring para converter o JSON que o Postman enviar
        // para o nosso objeto ClienteRequest

        try {
            clienteService.cadastrar(request);
            // Retorna 201 Created (sucesso na criação)
            return ResponseEntity.status(201).body("Cliente cadastrado com sucesso!");

        } catch (Exception e) {
            // Se o service lançar a "RuntimeException", nós a capturamos
            // e retornamos um 400 Bad Request (ex: email já existe)
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint: POST /api/clientes/login
     * [cite: 71]
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {
            ClienteResponse response = clienteService.login(request);
            // Retorna 200 OK e o corpo da resposta (ClienteResponse) [cite: 72-80]
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Se o service lançar a "RuntimeException" (email ou senha inválidos)
            // retornamos 401 Unauthorized
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}