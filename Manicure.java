package com.mycompany.prjmanicure;

public class Manicure {
    private String nome;

    public Manicure(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) { // Adicionando um setter para expandir a flexibilidade
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Manicure: " + nome;
    }
}
