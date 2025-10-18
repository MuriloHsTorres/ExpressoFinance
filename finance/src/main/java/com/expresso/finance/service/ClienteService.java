package com.expresso.finance.service;

import com.expresso.finance.dto.request.ClienteRequest;
import com.expresso.finance.dto.request.LoginRequest;
import com.expresso.finance.dto.response.ClienteResponse;
import com.expresso.finance.entity.Cliente;
import com.expresso.finance.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class    ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // Injetando os outros serviços que acabamos de criar
    @Autowired
    private ContaService contaService;

    @Autowired
    private CategoriaService categoriaService;

    // (O PasswordUtil viria aqui, mas estamos pulando por enquanto)

    /**
     * Lógica de Cadastro (POST /api/clientes/cadastro)
     */
    @Transactional // Garante que ou TUDO funciona, ou NADA é salvo.
    public Cliente cadastrar(ClienteRequest request) {

        // 1. Validar se o email já existe
        if (clienteRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado.");
        }

        // 2. Criar a nova entidade Cliente
        Cliente cliente = new Cliente();
        cliente.setNome(request.getNome());
        cliente.setEmail(request.getEmail());

        // 3. Salvar a senha em TEXTO SIMPLES (como combinado)
        cliente.setSenha(request.getSenha());
        // O @PrePersist na entidade 'Cliente' vai cuidar da 'dataCriacao'

        // 4. Salvar o cliente no banco
        Cliente clienteSalvo = clienteRepository.save(cliente);

        // 5. Executar as Regras de Negócio Pós-Cadastro
        //[cite_start]// Regra: "Criar conta padrão 'Carteira' com saldoAbertura $0" [cite: 166]
        contaService.criarCarteiraPadrao(clienteSalvo);

        //cite_start// Regra: "Criar categorias padrão: Salário, Alimentação, etc" [cite: 92-96, 167]
        categoriaService.criarCategoriasPadrao(clienteSalvo);

        return clienteSalvo;
    }

    /**
     * Lógica de Login (POST /api/clientes/login)
     */
    public ClienteResponse login(LoginRequest request) {

        // 1. Buscar o cliente pelo email
        Cliente cliente = clienteRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou senha inválidos."));

        // 2. Verificar a senha (em TEXTO SIMPLES)
        if (!cliente.getSenha().equals(request.getSenha())) {
            // (Aqui usaremos o PasswordUtil.checkPassword no futuro)
            throw new RuntimeException("Email ou senha inválidos.");
        }

       // [cite_start]// 3. Se deu tudo certo, retornar o DTO de Resposta [cite: 72-80]
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getDataCriacao()
        );
    }
}