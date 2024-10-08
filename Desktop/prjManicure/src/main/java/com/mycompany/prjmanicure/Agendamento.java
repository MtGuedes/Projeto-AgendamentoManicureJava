package com.mycompany.prjmanicure;

import java.time.LocalDateTime;

public class Agendamento {
    private Cliente cliente;
    private Manicure manicure;
    private LocalDateTime dataHora;

    public Agendamento(Cliente cliente, Manicure manicure, LocalDateTime dataHora) {
        this.cliente = cliente;
        this.manicure = manicure;
        this.dataHora = dataHora;
    }

    // Métodos getters
    public Cliente getCliente() {
        return cliente;
    }

    public Manicure getManicure() {
        return manicure;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    // Métodos setters
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setManicure(Manicure manicure) {
        this.manicure = manicure;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
