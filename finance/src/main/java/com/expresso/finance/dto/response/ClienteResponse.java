package com.expresso.finance.dto.response;

import java.time.LocalDateTime;

// Esta classe define os dados que enviamos de volta ao frontend
// Note que NUNCA enviamos a senha de volta.
public class ClienteResponse {

    private String id;
    private String nome;
    private String email;
    private LocalDateTime dataCriacao;

    // Um construtor para facilitar a criação deste objeto
    public ClienteResponse(String id, String nome, String email, LocalDateTime dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dataCriacao = dataCriacao;
    }

    // --- Getters e Setters Manuais ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}