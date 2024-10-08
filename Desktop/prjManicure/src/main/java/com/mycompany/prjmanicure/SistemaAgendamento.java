package com.mycompany.prjmanicure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SistemaAgendamento {
    private List<Agendamento> agendamentos = new ArrayList<>();
    private Connection connection;

    public SistemaAgendamento() {
        conectarBanco();
        criarTabela();
        carregarAgendamentos(); // Carrega agendamentos existentes do banco de dados
    }

    private void conectarBanco() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/MATHEUS/Desktop/AgendaManicure.db");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    private void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS agendamentos ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " cliente_nome TEXT NOT NULL,"
                + " cliente_telefone TEXT NOT NULL,"
                + " manicure_nome TEXT NOT NULL,"
                + " data_hora TEXT NOT NULL"
                + ");";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela: " + e.getMessage());
        }
    }

    private void carregarAgendamentos() {
        String sql = "SELECT * FROM agendamentos";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String nomeCliente = rs.getString("cliente_nome");
                String telefoneCliente = rs.getString("cliente_telefone");
                String nomeManicure = rs.getString("manicure_nome");
                LocalDateTime dataHora = LocalDateTime.parse(rs.getString("data_hora"));

                Cliente cliente = new Cliente(nomeCliente, telefoneCliente);
                Manicure manicure = new Manicure(nomeManicure);
                Agendamento agendamento = new Agendamento(cliente, manicure, dataHora);
                agendamentos.add(agendamento);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao carregar agendamentos: " + e.getMessage());
        }
    }

    public void agendar(Cliente cliente, Manicure manicure, LocalDateTime dataHora) {
        Agendamento agendamento = new Agendamento(cliente, manicure, dataHora);
        agendamentos.add(agendamento);
        salvarAgendamento(agendamento);
        System.out.println("Agendamento realizado com sucesso!");
    }

    private void salvarAgendamento(Agendamento agendamento) {
        String sql = "INSERT INTO agendamentos (cliente_nome, cliente_telefone, manicure_nome, data_hora) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, agendamento.getCliente().getNome());
            stmt.setString(2, agendamento.getCliente().getTelefone());
            stmt.setString(3, agendamento.getManicure().getNome());
            stmt.setString(4, agendamento.getDataHora().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao salvar no banco: " + e.getMessage());
        }
    }

    public void exibirAgendamentos() {
        for (Agendamento agendamento : agendamentos) {
            System.out.println("Agendamento: " + agendamento.getCliente().getNome() +
                    " com " + agendamento.getManicure().getNome() +
                    " às " + agendamento.getDataHora());
        }
    }

    public Agendamento localizarAgendamento(String nomeCliente) {
        for (Agendamento agendamento : agendamentos) {
            if (agendamento.getCliente().getNome().equalsIgnoreCase(nomeCliente)) {
                return agendamento;
            }
        }
        return null; // Retorna null se não encontrar
    }

    // Método modificado para aceitar LocalDateTime
    public void excluirAgendamento(LocalDateTime dataHora) {
        String sql = "DELETE FROM agendamentos WHERE data_hora = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dataHora.toString()); // Converter LocalDateTime para String
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                agendamentos.removeIf(a -> a.getDataHora().isEqual(dataHora)); // Remove da lista em memória
                System.out.println("Agendamento excluído com sucesso!");
            } else {
                System.out.println("Nenhum agendamento encontrado para a data e hora fornecidas.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao excluir agendamento: " + e.getMessage());
        }
    }

    public void atualizarAgendamento(String nomeCliente, Manicure novaManicure, LocalDateTime novaDataHora) {
        String sql = "UPDATE agendamentos SET manicure_nome = ?, data_hora = ? WHERE cliente_nome = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, novaManicure.getNome());
            stmt.setString(2, novaDataHora.toString());
            stmt.setString(3, nomeCliente);
            stmt.executeUpdate();

            // Atualiza a lista em memória
            Agendamento agendamento = localizarAgendamento(nomeCliente);
            if (agendamento != null) {
                agendamento.setManicure(novaManicure);
                agendamento.setDataHora(novaDataHora);
            }

            System.out.println("Agendamento atualizado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar agendamento: " + e.getMessage());
        }
    }

    // Método para configurar a conexão com o banco de dados
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    // Método para obter a lista de agendamentos
    public List<Agendamento> getAgendamentos() {
        return agendamentos;
    }
}
