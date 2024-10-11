package com.mycompany.prjmanicure;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AgendamentoApp {
    private SistemaAgendamento sistemaAgendamento;
    private static final String DB_URL = "jdbc:sqlite:C:/Users/MATHEUS/Desktop/AgendaManicure.db";  // Conexão com o SQLite
    private Connection connection;

    public AgendamentoApp() {
        sistemaAgendamento = new SistemaAgendamento();
        try {
            connection = DriverManager.getConnection(DB_URL); // Conectar ao banco de dados
            sistemaAgendamento.setConnection(connection); // Passa a conexão para o sistema de agendamento
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage());
            System.exit(1); // Encerra o aplicativo se não puder conectar
        }
        criarInterface();
    }

    private void criarInterface() {
        JFrame frame = new JFrame("Agendamento de Manicure");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());

        // Painel para incluir e atualizar agendamentos
        JPanel incluirPanel = new JPanel();
        incluirPanel.setLayout(new GridBagLayout());
        incluirPanel.setBorder(BorderFactory.createTitledBorder("Incluir/Atualizar Agendamento"));
        frame.add(incluirPanel, BorderLayout.NORTH);

        // GridBagConstraints para o painel de inclusão
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Campos de entrada para incluir e atualizar
        JTextField nomeField = new JTextField(20);
        JTextField telefoneField = new JTextField(20);
        JTextField manicureField = new JTextField(20);
        JTextField dataHoraField = new JTextField(20);

        // Adicionando os componentes ao painel de inclusão
        gbc.gridx = 0; gbc.gridy = 0; incluirPanel.add(new JLabel("Nome do Cliente:"), gbc);
        gbc.gridx = 1; incluirPanel.add(nomeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; incluirPanel.add(new JLabel("Telefone do Cliente:"), gbc);
        gbc.gridx = 1; incluirPanel.add(telefoneField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; incluirPanel.add(new JLabel("Nome da Manicure:"), gbc);
        gbc.gridx = 1; incluirPanel.add(manicureField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; incluirPanel.add(new JLabel("Data e Hora (HH:mm dd/MM/yyyy):"), gbc);
        gbc.gridx = 1; incluirPanel.add(dataHoraField, gbc);
        
        // Botões de ação
        JButton agendarButton = new JButton("Agendar");
        JButton atualizarButton = new JButton("Atualizar");
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; incluirPanel.add(agendarButton, gbc);
        gbc.gridy = 5; incluirPanel.add(atualizarButton, gbc);

        // Painel para buscar e excluir agendamentos
        JPanel buscarPanel = new JPanel();
        buscarPanel.setLayout(new GridBagLayout());
        buscarPanel.setBorder(BorderFactory.createTitledBorder("Buscar/Excluir Agendamento"));
        frame.add(buscarPanel, BorderLayout.CENTER);

        // Campos de entrada para buscar e excluir
        JTextField nomeBuscarField = new JTextField(20);
        JTextField dataHoraExcluirField = new JTextField(20);

        // Adicionando os componentes ao painel de busca
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 0; buscarPanel.add(new JLabel("Buscar Agendamento (Nome):"), gbc);
        gbc.gridx = 1; buscarPanel.add(nomeBuscarField, gbc);
        JButton buscarButton = new JButton("Buscar");
        gbc.gridy = 1; buscarPanel.add(buscarButton, gbc);

        gbc.gridx = 0; gbc.gridy = 2; buscarPanel.add(new JLabel("Excluir Agendamento (Data e Hora):"), gbc);
        gbc.gridx = 1; buscarPanel.add(dataHoraExcluirField, gbc);
        JButton excluirButton = new JButton("Excluir");
        gbc.gridy = 3; buscarPanel.add(excluirButton, gbc);

        // Área de texto para exibir agendamentos
        JTextArea textArea = new JTextArea(10, 40);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Agendamentos"));
        frame.add(scrollPane, BorderLayout.SOUTH);

        // Ações dos botões
        agendarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                String telefone = telefoneField.getText();
                String manicureNome = manicureField.getText();
                String dataHoraTexto = dataHoraField.getText();

                if (nome.isEmpty() || telefone.isEmpty() || manicureNome.isEmpty() || dataHoraTexto.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Por favor, preencha todos os campos.");
                    return;
                }

                try {
                    LocalDateTime dataHora = LocalDateTime.parse(dataHoraTexto, DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
                    Cliente cliente = new Cliente(nome, telefone);
                    Manicure manicure = new Manicure(manicureNome);
                    sistemaAgendamento.agendar(cliente, manicure, dataHora);

                    textArea.setText(formatarAgendamentos(sistemaAgendamento.getAgendamentos())); // Exibir agendamentos
                    JOptionPane.showMessageDialog(frame, "Agendamento realizado com sucesso!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Erro ao agendar: " + ex.getMessage());
                }
            }
        });

        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeCliente = nomeField.getText();
                String novaManicureNome = manicureField.getText();
                String novaDataHoraTexto = dataHoraField.getText();

                if (nomeCliente.isEmpty() || novaManicureNome.isEmpty() || novaDataHoraTexto.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Por favor, preencha todos os campos.");
                    return;
                }

                try {
                    LocalDateTime novaDataHora = LocalDateTime.parse(novaDataHoraTexto, DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
                    Manicure novaManicure = new Manicure(novaManicureNome);
                    sistemaAgendamento.atualizarAgendamento(nomeCliente, novaManicure, novaDataHora);
                    textArea.setText(formatarAgendamentos(sistemaAgendamento.getAgendamentos())); // Atualiza a exibição
                    JOptionPane.showMessageDialog(frame, "Agendamento atualizado com sucesso!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Erro ao atualizar agendamento: " + ex.getMessage());
                }
            }
        });

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeCliente = nomeBuscarField.getText();
                Agendamento agendamento = sistemaAgendamento.localizarAgendamento(nomeCliente);
                if (agendamento != null) {
                    String dataHoraFormatada = agendamento.getDataHora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    textArea.setText("Agendamento encontrado:\n" +
                            "Cliente: " + agendamento.getCliente().getNome() +
                            ", Telefone: " + agendamento.getCliente().getTelefone() +
                            ", Manicure: " + agendamento.getManicure().getNome() +
                            ", Data e Hora: " + dataHoraFormatada);
                } else {
                    textArea.setText("Nenhum agendamento encontrado para o cliente " + nomeCliente);
                }
            }
        });

        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dataHoraTexto = dataHoraExcluirField.getText();

                if (dataHoraTexto.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Por favor, preencha a data e hora.");
                    return;
                }

                try {
                    LocalDateTime dataHoraExcluir = LocalDateTime.parse(dataHoraTexto, DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
                    sistemaAgendamento.excluirAgendamento(dataHoraExcluir);
                    textArea.setText(formatarAgendamentos(sistemaAgendamento.getAgendamentos())); // Atualiza a exibição
                    JOptionPane.showMessageDialog(frame, "Agendamento excluído com sucesso!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Erro ao excluir agendamento: " + ex.getMessage());
                }
            }
        });

        frame.setVisible(true);
    }

    private String formatarAgendamentos(List<Agendamento> agendamentos) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Formato de data

        for (Agendamento agendamento : agendamentos) {
            String dataHoraFormatada = agendamento.getDataHora().format(formatter); // Formatar LocalDateTime
            sb.append("Agendamento: ")
              .append(agendamento.getCliente().getNome())
              .append(" com ")
              .append(agendamento.getManicure().getNome())
              .append(" às ")
              .append(dataHoraFormatada) // Agora está correto
              .append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AgendamentoApp::new);
    }
}
