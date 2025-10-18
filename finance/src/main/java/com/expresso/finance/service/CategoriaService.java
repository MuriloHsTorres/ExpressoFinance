package com.expresso.finance.service;

import com.expresso.finance.dto.request.CategoriaRequest;
import com.expresso.finance.dto.response.CategoriaResponse;
import com.expresso.finance.entity.Categoria;
import com.expresso.finance.entity.Cliente;
import com.expresso.finance.exception.ResourceNotFoundException;
import com.expresso.finance.repository.CategoriaRepository;
import com.expresso.finance.repository.ClienteRepository; // Importe este
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays; // Importe este
import java.util.List;
import java.util.stream.Collectors; // Importe este

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Precisamos do ClienteRepository para buscar o "dono" da categoria
    @Autowired
    private ClienteRepository clienteRepository;

    // REGRA DE NEGÓCIO: Lista de categorias padrão que não podem ser alteradas
    private static final List<String> CATEGORIAS_PADRAO_NOMES = Arrays.asList(
            "Salário", "Alimentação", "Transporte", "Transferências"
    );

    /**
     * Método antigo (do Módulo 1)
     * Regra de Negócio: "Criar categorias padrão"
     */
    public void criarCategoriasPadrao(Cliente cliente) {
        for (String nome : CATEGORIAS_PADRAO_NOMES) {
            Categoria categoria = new Categoria();
            categoria.setNome(nome);
            categoria.setCliente(cliente);
            categoriaRepository.save(categoria);
        }
    }

    // --- NOVOS MÉTODOS (Módulo 3: CRUD de Categorias) ---

    /**
     * Endpoint: POST /api/categorias
     * Cria uma nova categoria personalizada (Ex: Academia)
     */
    public CategoriaResponse criarCategoria(CategoriaRequest request) {
        // 1. Buscar o cliente "dono"
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + request.getClienteId()));

        // 2. Criar a nova categoria
        Categoria categoria = new Categoria();
        categoria.setNome(request.getNome());
        categoria.setCliente(cliente);

        // 3. Salvar no banco
        Categoria categoriaSalva = categoriaRepository.save(categoria);

        // 4. Retornar o DTO de resposta
        return new CategoriaResponse(categoriaSalva);
    }

    /**
     * Endpoint: GET /api/categorias/cliente/{clienteId}
     * Lista todas as categorias (padrão + personalizadas) de um cliente
     */
    public List<CategoriaResponse> listarCategoriasPorCliente(String clienteId) {
        // 1. Usar o método novo do repositório
        List<Categoria> categorias = categoriaRepository.findAllByClienteId(clienteId);

        // 2. Converter a lista de Entidade (Categoria) para DTO (CategoriaResponse)
        return categorias.stream()
                .map(CategoriaResponse::new) // (Chama o construtor 'new CategoriaResponse(categoria)')
                .collect(Collectors.toList());
    }

    /**
     * Endpoint: PUT /api/categorias/{id}
     * Atualiza o nome de uma categoria personalizada
     */
    public CategoriaResponse atualizarCategoria(String id, CategoriaRequest request) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com id: " + id));

        // REGRA DE NEGÓCIO: Não permitir alterar categorias padrão
        if (isCategoriaPadrao(categoria.getNome())) {
            throw new RuntimeException("Não é permitido atualizar uma categoria padrão.");
        }

        categoria.setNome(request.getNome());
        Categoria categoriaAtualizada = categoriaRepository.save(categoria);
        return new CategoriaResponse(categoriaAtualizada);
    }

    /**
     * Endpoint: DELETE /api/categorias/{id}
     * Deleta uma categoria personalizada
     */
    public void deletarCategoria(String id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com id: " + id));

        // REGRA DE NEGÓCIO: Não permitir deletar categorias padrão
        if (isCategoriaPadrao(categoria.getNome())) {
            throw new RuntimeException("Não é permitido deletar uma categoria padrão.");
        }

        // (Regra Futura: verificar se a categoria tem transações)

        categoriaRepository.delete(categoria);
    }

    /**
     * Método auxiliar privado para checar a regra de negócio
     */
    private boolean isCategoriaPadrao(String nomeCategoria) {
        // Compara o nome da categoria (ignorando maiúsculas/minúsculas)
        return CATEGORIAS_PADRAO_NOMES.stream()
                .anyMatch(nomePadrao -> nomePadrao.equalsIgnoreCase(nomeCategoria));
    }
}