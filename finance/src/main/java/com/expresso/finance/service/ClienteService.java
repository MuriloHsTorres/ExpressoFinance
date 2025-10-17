package com.expresso.finance.service;

import com.expresso.finance.dto.request.AlterarSenhaRequest;
import com.expresso.finance.dto.request.AtualizarPerfilRequest;
import com.expresso.finance.dto.response.ClienteResponse;
import com.expresso.finance.entity.Cliente;
import com.expresso.finance.exception.BusinessException;
import com.expresso.finance.exception.ResourceNotFoundException;
import com.expresso.finance.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteResponse buscarPerfil(String clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getDataCriacao()
        );
    }

    @Transactional
    public ClienteResponse atualizarPerfil(String clienteId, AtualizarPerfilRequest request) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        // Verifica se o email já está em uso por outro cliente
        if (!cliente.getEmail().equals(request.getEmail())) {
            clienteRepository.findByEmail(request.getEmail())
                    .ifPresent(c -> {
                        throw new BusinessException("Email já está em uso");
                    });
        }

        cliente.setNome(request.getNome());
        cliente.setEmail(request.getEmail());

        Cliente clienteAtualizado = clienteRepository.save(cliente);

        return new ClienteResponse(
                clienteAtualizado.getId(),
                clienteAtualizado.getNome(),
                clienteAtualizado.getEmail(),
                clienteAtualizado.getDataCriacao()
        );
    }

    @Transactional
    public void alterarSenha(String clienteId, AlterarSenhaRequest request) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        // Verifica se a senha atual está correta
        if (!passwordEncoder.matches(request.getSenhaAtual(), cliente.getSenha())) {
            throw new BusinessException("Senha atual incorreta");
        }

        // Atualiza para a nova senha
        cliente.setSenha(passwordEncoder.encode(request.getNovaSenha()));
        clienteRepository.save(cliente);
    }

    @Transactional
    public void excluirConta(String clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        // O CASCADE vai deletar automaticamente:
        // - Todas as contas
        // - Todas as transações
        // - Todas as categorias
        // - Todas as metas
        clienteRepository.delete(cliente);
    }
}