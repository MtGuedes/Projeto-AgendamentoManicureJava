package com.mycompany.prjmanicure;

public class Cliente {
    private String nome;
    private String telefone;

    public Cliente(String nome, String telefone) {
        this.nome = nome;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) { // Adicionando setter para o nome
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) { // Adicionando setter para o telefone
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return "Cliente: " + nome + ", Telefone: " + telefone;
    }
}
